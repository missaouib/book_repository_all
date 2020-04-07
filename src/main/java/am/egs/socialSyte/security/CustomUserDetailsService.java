package am.egs.socialSyte.security;

import am.egs.socialSyte.exception.UserNotFoundException;
import am.egs.socialSyte.model.User;
import am.egs.socialSyte.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UserNotFoundException {
        User user = userRepository.getUserByEmail(email).orElseThrow(
                () -> new UserNotFoundException());
        return UserPrincipal.create(user);
    }
}
