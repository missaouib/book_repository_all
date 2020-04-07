package am.egs.socialSyte.service.impl;

import am.egs.socialSyte.exception.DuplicateUserException;
import am.egs.socialSyte.model.Role;
import am.egs.socialSyte.model.User;
import am.egs.socialSyte.payload.UserDto;
import am.egs.socialSyte.payload.auth.AuthResponse;
import am.egs.socialSyte.repository.RoleRepository;
import am.egs.socialSyte.repository.UserRepository;
import am.egs.socialSyte.service.UserService;
import am.egs.socialSyte.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static am.egs.socialSyte.util.UserLocalDateTime.userExpiredDateTime;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MailSender mailSender;
    private User user;
    private UserDto userDto;
    private int count;
    private LocalDateTime currentLocalDateTime = LocalDateTime.now();

    @Autowired
    public UserServiceImpl(RoleRepository roleRepository, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, MailSender mailSender) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    @Override
    public void activateUser(String code) {
        User user = userRepository.findByActivationCode(code);
        user.setActivationCode(null);
        user.setEmailVerified(true);
        userRepository.save(user);
    }

    @Override
    public void addUser(User user) {
        LocalDateTime expireDate;
        Optional<User> currentUser = userRepository.getUserByEmail(user.getEmail());
        if (currentUser.isPresent()) {
            throw new DuplicateUserException();
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Optional<Role> role = roleRepository.findByRole("ADMIN");
        if (role.isPresent()) {
            List<Role> roleNameSet = Collections.singletonList(role.get());
            user.setRoles(roleNameSet);
        }
        user.setActivationCode(UUID.randomUUID().toString());
        user.setEnabled(true);
        user.setAccountNonLocked(true);
        user.setTryNumber(0);
        user.setAccountNonExpired(true);
        expireDate = userExpiredDateTime();
        user.setExpireDate(expireDate);
        mailSender.send(user);
        userRepository.save(user);
    }

    @Override
    public AuthResponse signIn(String email, String password) {
        user = userRepository.findUserByEmail(email);
        return AuthResponse.builder()
                .build();
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email);
        return userDto;
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void delete(String email) {
        user = userRepository.findUserByEmail(email);
        userRepository.deleteByEmail(email);
    }

    @Override
    public User update(User user) {
        Optional<User> currentUser = userRepository.getUserByEmail(user.getEmail());
        if (currentUser == null) {
            return null;
        }
        return userRepository.save(user);
    }

    @Override
    public void expired(String email) {
        User user = userRepository.findUserByEmail(email);
        user.setAccountNonExpired(false);
        userRepository.save(user);
    }

    @Override
    public void userUnLocked(String email) {
        User user = userRepository.findUserByEmail(email);
        LocalDateTime unLockedTime = user.getUnLockedTime();
        if (unLockedTime.isBefore(currentLocalDateTime)) {
            user.setTryNumber(0);
            user.setAccountNonLocked(true);
            user.setLockedTime(null);
            user.setUnLockedTime(null);
            userRepository.save(user);
        }
    }

    @Override
    public void tryNumberIncrement(String email, String password) {
        LocalDateTime unLockedTime;
        user = userRepository.findUserByEmail(email);
        count = user.getTryNumber();
        count++;
        user.setTryNumber(count);
        if (count >= 3) {
            user.setAccountNonLocked(false);
            user.setLockedTime(currentLocalDateTime);
            unLockedTime = currentLocalDateTime.plusHours(12);
            user.setUnLockedTime(unLockedTime);
            userRepository.save(user);
        }
        userRepository.save(user);
    }
}