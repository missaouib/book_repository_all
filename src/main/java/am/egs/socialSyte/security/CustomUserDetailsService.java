package am.egs.socialSyte.security;

import am.egs.socialSyte.exception.UserNotFoundException;
import am.egs.socialSyte.model.User;
import am.egs.socialSyte.repository.UserRepository;
import am.egs.socialSyte.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserService userService;

    public CustomUserDetailsService(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findUserByEmail(email);
        if (user.isAccountNonExpired()) {
            if (!user.isAccountNonLocked()) {
                userService.userUnLocked(email);
            }
        }
        return UserPrincipal.create(user);
    }
}
