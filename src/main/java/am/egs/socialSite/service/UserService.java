package am.egs.socialSite.service;

import am.egs.socialSite.model.User;
import am.egs.socialSite.payload.UserDto;
import am.egs.socialSite.security.UserPrincipal;

import java.util.List;

public interface UserService {

    void activateUser(String code);

    void addUser(UserDto userDto);

    User signIn(String email, String password);

    void delete(Long id);

    List<User> findAllUsers();

 //   User update(User user);

    void expired(String email);

    void userUnLocked(String email);

    void tryNumberIncrement(String email);

    boolean exists(final String email);

    User getEmployeeById(Long id) throws Exception;

    User createOrUpdateEmployee(User entity, UserPrincipal principal);


}
