package am.egs.socialSyte.repository;

import am.egs.socialSyte.model.Role;
import am.egs.socialSyte.model.User;
import am.egs.socialSyte.payload.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> getUserByEmail(String email);

    Optional<User> findByActivationCode(String code);

 //   User findUserByEmail(String email);
//
//    List<User> findAll();
//
//    boolean existsByEmail(String email);
//
//    List<User> findAllByRoles(Role roleName);
//
//    void deleteByEmail(String email);
}
