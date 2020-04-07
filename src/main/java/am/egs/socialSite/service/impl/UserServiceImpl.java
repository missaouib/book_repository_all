package am.egs.socialSite.service.impl;

import am.egs.socialSite.exception.DuplicateUserException;
import am.egs.socialSite.mappers.UserMapper;
import am.egs.socialSite.model.Role;
import am.egs.socialSite.model.User;
import am.egs.socialSite.payload.UserDto;
import am.egs.socialSite.repository.RoleRepository;
import am.egs.socialSite.repository.UserRepository;
import am.egs.socialSite.security.UserPrincipal;
import am.egs.socialSite.service.UserService;
import am.egs.socialSite.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static am.egs.socialSite.util.UserLocalDateTime.userExpiredDateTime;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MailSender mailSender;
    private LocalDateTime currentLocalDateTime = LocalDateTime.now();
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(RoleRepository roleRepository, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, MailSender mailSender, UserMapper userMapper) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
        this.userMapper = userMapper;
    }

    @Override
    public void activateUser(String code) {
        User user = userRepository.findByActivationCode(code);
        user.setActivationCode(null);
        user.setEmailVerified(true);
        userRepository.save(user);
    }

    @Override
    public void addUser(UserDto userDto) {
        LocalDateTime expireDate;
        Optional<User> currentUser = userRepository.getUserByEmail(userDto.getEmail());
        if (currentUser.isPresent()) {
            throw new DuplicateUserException("There are already user with this email.");
        }
        User user = userMapper.map(userDto, User.class);
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
    public User signInSuccess(String email) {
        User user = userRepository.findUserByEmail(email);
        user.setTryNumber(0);
        userRepository.save(user);
        return user;
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

//    @Override
//    public void delete(Long id) {
//        Optional<User> user = userRepository.getUserById(id);
//        if (user == null) {
//            throw new UserNotFoundException("User not found");
//        }
//        userRepository.deleteByEmail(user);
//    }

    public void delete(Long id) {
        userRepository.findById(id)
                .ifPresent(user -> userRepository.delete(user));
    }

//    @Override
//    public User update(User user) {
//        Optional<User> currentUser = userRepository.getUserById(user.getId());
//        if (currentUser == null) {
//            return null;
//        }
//        return userRepository.save(user);
//    }

    public User updateUser(User entity, @AuthenticationPrincipal UserPrincipal principal) {
        String email = principal.getUsername();
        System.out.println(email);
        User user = userRepository.findUserByEmail(email);
        user.setEmail(entity.getEmail());
        user.setName(entity.getName());
        user.setSurName(entity.getSurName());
        user.setAge(entity.getAge());
        User userUpdated = userRepository.save(user);
        return userUpdated;
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
    public void tryNumberIncrement(String email) {
        LocalDateTime unLockedTime;
        User user = userRepository.findUserByEmail(email);
        int count = user.getTryNumber();
        count++;
        user.setTryNumber(count);
        if (count >= 3) {
            user.setAccountNonLocked(false);
            user.setLockedTime(currentLocalDateTime);
            unLockedTime = currentLocalDateTime.plusHours(24);
            user.setUnLockedTime(unLockedTime);
            userRepository.save(user);
        }
        userRepository.save(user);
    }

    @Override
    public boolean exists(String email) {
        return userRepository.existsUserByEmail(email);
    }


    public User getUserById(Long id) throws Exception {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            return user.get();
        } else {
            throw new Exception("No employee record exist for given id");
        }
    }
}