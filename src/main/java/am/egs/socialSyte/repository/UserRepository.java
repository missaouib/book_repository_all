package am.egs.socialSyte.repository;

import am.egs.socialSyte.model.Role;
import am.egs.socialSyte.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> getUserByEmail(String email);

    User findUserByEmail(String email);

    User findByActivationCode(String code);

    void deleteByEmail(String email);

    List<User> findAllByRoles(Role roleName);
}
