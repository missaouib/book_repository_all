package am.egs.bookRepository.service;

import am.egs.bookRepository.model.User;
import am.egs.bookRepository.payload.UserDto;
import am.egs.bookRepository.security.UserPrincipal;

import java.util.List;

public interface UserService {

    void activateUser(String code);

    void addUser(UserDto userDto);

    User signInSuccess(String email);

    void delete(Long id);

    List<User> findAllUsers();

    void expired(String email);

    void userUnLocked(String email);

    void tryNumberIncrement(String email);

    boolean exists(final String email);

    User getUserById(Long id) throws Exception;

    User updateUser(UserDto userDto, UserPrincipal principal);


}
