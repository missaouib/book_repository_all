package am.egs.socialSyte.service.impl;

import am.egs.socialSyte.exception.DuplicateUserException;
import am.egs.socialSyte.exception.UserNotFoundException;
import am.egs.socialSyte.model.Role;
import am.egs.socialSyte.model.User;
import am.egs.socialSyte.payload.UserDto;
import am.egs.socialSyte.payload.auth.AuthResponse;
import am.egs.socialSyte.repository.RoleRepository;
import am.egs.socialSyte.repository.UserRepository;
import am.egs.socialSyte.service.UserService;
import am.egs.socialSyte.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
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
    private final AuthenticationManager authenticationManager;
    private final MailSender mailSender;
    private User user;
    private UserDto userDto;
    private int count;

    @Autowired
    public UserServiceImpl(RoleRepository roleRepository, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager, MailSender mailSender) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.mailSender = mailSender;
    }

    @Override
    public void activateUser(String code) {
        User user = userRepository.findByActivationCode(code).orElseThrow(
                () -> new UserNotFoundException());
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

        expireDate = userExpiredDateTime();
        user.setExpireDate(expireDate);

        mailSender.send(user);
        userRepository.save(user);
    }

    @Override
    public AuthResponse signIn(String email, String password) {
        user = userRepository.getUserByEmail(email).orElseThrow(
                () -> new UserNotFoundException());
        return AuthResponse.builder()
                .build();
    }

    @Override
    public void tryNuymberIncrement(String email, String password) {
        LocalDateTime currentDateTime, unLockedTime;

        user = userRepository.findUserByEmail(email);
        count = user.getTryNumber();
        count++;
        user.setTryNumber(count);
        if (count >= 3) {
            user.setAccountNonLocked(false);
            currentDateTime = LocalDateTime.now();
            user.setLokedTime(currentDateTime);
            unLockedTime = currentDateTime.plusSeconds(10);
            user.setUnLokedTime(unLockedTime);
            userRepository.save(user);
        }
        userRepository.save(user);
    }

    @Override
    public void isUserNonLocked(String email,String password) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        if (currentDateTime.isBefore(user.getUnLokedTime())) {
            user.setTryNumber(0);
            user.setAccountNonLocked(true);
            userRepository.save(user);
            signIn(email, password);
        }
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.getUserByEmail(email).orElseThrow(
                () -> new UserNotFoundException());
        return userDto;
    }

    @Override
    public List<User> findAllUsers() {
        //          throws UserNotFoundException
        return userRepository.findAll();
    }

    @Override
    public void delete(String email) {
        user = userRepository.getUserByEmail(email).orElseThrow(
                () -> new UserNotFoundException());
        userRepository.deleteByEmail(email);
    }

    public List<User> userList(Role roleName) {
        List<User> userList = userRepository.findAllByRoles(roleName);
        return userList;
    }

    @Override
    public User update(User user) {
        Optional<User> persitedUser = userRepository.getUserByEmail(user.getEmail());
        if (persitedUser == null) {
            return null;
        }
        return userRepository.save(user);
    }
}