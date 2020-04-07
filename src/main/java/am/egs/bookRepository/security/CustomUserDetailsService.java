package am.egs.bookRepository.security;

import am.egs.bookRepository.model.User;
import am.egs.bookRepository.repository.UserRepository;
import am.egs.bookRepository.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
        if(user==null){
            throw new UsernameNotFoundException(" User not authorized. ");
        }
        if (user.isAccountNonExpired()) {
            if (!user.isAccountNonLocked()) {
                userService.userUnLocked(email);
            }
        }
        return UserPrincipal.create(user);
    }
}
