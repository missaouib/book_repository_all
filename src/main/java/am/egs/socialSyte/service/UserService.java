package am.egs.socialSyte.service;

import am.egs.socialSyte.model.User;
import am.egs.socialSyte.payload.UserDto;
import am.egs.socialSyte.payload.auth.AuthResponse;

import java.util.List;

public interface UserService {

    void activateUser(String code);

    void addUser(User user);

    AuthResponse signIn(String email, String password);

    void delete(String email);

    UserDto getUserByEmail(String email);

    void tryNuymberIncrement(String email, String password);

    List<User> findAllUsers();

    User update(User user);

    void isUserNonLocked(String email, String password);

}
