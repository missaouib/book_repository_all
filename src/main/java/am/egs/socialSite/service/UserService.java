package am.egs.socialSite.service;

import am.egs.socialSite.model.User;
import am.egs.socialSite.payload.UserDto;
import am.egs.socialSite.security.UserPrincipal;

import java.util.List;

public interface UserService {

    void activateUser(String code);

    void addUser(UserDto userDto);

    User signInSuccess(String email);

    void delete(Long id);

    List<User> findAllUsers();

 //   User update(User user);

    void expired(String email);

    void userUnLocked(String email);

    void tryNumberIncrement(String email);

    boolean exists(final String email);

    User getUserById(Long id) throws Exception;

    User updateUser(User entity, UserPrincipal principal);


}
