package am.egs.bookRepository.repository;

import am.egs.bookRepository.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> getUserByEmail(String email);

    User findUserByEmail(String email);

    User findByActivationCode(String code);

    boolean existsUserByEmail(String email);

    void deleteByEmail(Optional<User> email);

    Optional<User> getUserById(Long id);
}
