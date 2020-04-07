package am.egs.socialSyte.service.impl;

import am.egs.socialSyte.exception.DuplicateUserException;
import am.egs.socialSyte.exception.UserNotFoundException;
import am.egs.socialSyte.model.Role;
import am.egs.socialSyte.model.User;
import am.egs.socialSyte.payload.UserDto;
import am.egs.socialSyte.payload.auth.AuthResponse;
import am.egs.socialSyte.payload.auth.SignInRequest;
import am.egs.socialSyte.repository.RoleRepository;
import am.egs.socialSyte.repository.UserRepository;
import am.egs.socialSyte.service.UserService;
import am.egs.socialSyte.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final MailSender mailSender;
    private User user;

    private LocalDateTime currentDateTime;
    private UserDto userDto;
    private int count;

    @Autowired
    public UserServiceImpl(RoleRepository roleRepository, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, MailSender mailSender) {
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
        mailSender.send(user);
        userRepository.save(user);
    }

    public AuthResponse signIn(SignInRequest inRequest) {
        user = userRepository.getUserByEmail(inRequest.getEmail()).orElseThrow(
                () -> new UserNotFoundException());

            if (!passwordEncoder.matches(inRequest.getPassword(), user.getPassword())) {
                throw  new UserNotFoundException();
            }

        return AuthResponse.builder()
                .build();

//            if (!isLockeTimeExpired()) {
//                throw new UserLockedException(" Account is locked! ");
//            } else {
//                if (!passwordEncoder.matches(password, user.getPassword())) {
//                    count = user.getTryNumber();
//                    count++;
//                    user.setTryNumber(count);
//                    userRepository.save(user);
//                    if (count == 3) {
//                        user.setAccountNotLocked(false);
//                        currentDateTime = LocalDateTime.now();
//                        user.setLokedTime(currentDateTime);
//                        userRepository.save(user);
//                        throw new UserLockedException(" You entered your username or password incorrectly for the third time. "
//                                + "\n" +
//                                " You can try login after 12 hours. ");
//                    }
//                    throw new UserNotFoundException("Wrong email or password");
//                }

//                count = 0;
//                user.setTryNumber(count);
//                userRepository.save(user);
//                return user;

    }


    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.getUserByEmail(email).orElseThrow(
                () -> new UserNotFoundException());
        return userDto;
    }


//
//
////    public boolean isLockeTimeExpired() {
////        while (user.isAccountNotLocked() == false) {
////            LocalDateTime notLockedTime = user.getLokedTime().plusHours(12);
////            currentDateTime = LocalDateTime.now();
////            if (notLockedTime.equals(currentDateTime)) {
////                user.setAccountNotLocked(true);
////                userRepository.save(user);
////            } else {
////                user.setAccountNotLocked(false);
////                userRepository.save(user);
////            }
////        }
////        return true;
////    }
//
//    @Override
//    public List<User> findAllUsers()
//    //          throws UserNotFoundException
//    {
//        return userRepository.findAll();
//    }
//
//    @Override
//    public void delete(String email)
//    // throws UserNotFoundException
//    {
//        userRepository.deleteByEmail(email);
//    }
//
//    public List<User> userList(Role roleName) {
//        List<User> userList = userRepository.findAllByRoles(roleName);
//        return userList;
//    }
//
//    @Override
//    public User update(User user) {
//        Optional<User> persitedUser = userRepository.getUserByEmail(user.getEmail());
//        if (persitedUser == null) {
//            return null;
//        }
//        return userRepository.save(user);
//    }
}