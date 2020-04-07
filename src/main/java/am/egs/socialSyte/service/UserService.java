package am.egs.socialSyte.service;

import am.egs.socialSyte.model.User;
import am.egs.socialSyte.payload.UserDto;
import am.egs.socialSyte.payload.auth.AuthResponse;
import am.egs.socialSyte.payload.auth.SignInRequest;

public interface UserService {

    void activateUser(String code);

    void addUser(User user);

    AuthResponse signIn(SignInRequest inRequest);

    UserDto getUserByEmail(String email);
//
//
//
//    List<User> findAllUsers();
//
//    User update(User user);
//
//    void delete(String email);
//
//    List<User> userList(Role role);
}
