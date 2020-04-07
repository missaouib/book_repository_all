package am.egs.socialSyte.service;

import am.egs.socialSyte.model.User;
import am.egs.socialSyte.payload.UserDto;
import am.egs.socialSyte.payload.auth.AuthResponse;
import am.egs.socialSyte.payload.auth.SignInRequest;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface UserService {

    void activateUser(String code);

    void addUser(User user);

    AuthResponse signIn(String email, String password);

    //  AuthResponse signIn(@RequestBody SignInRequest signInRequest);

    void delete(String email);

    UserDto getUserByEmail(String email);

    List<User> findAllUsers();

    User update(User user);

    void expired(String email);

    void userUnLocked(String email);

    void tryNumberIncrement(String email, String password);
}
