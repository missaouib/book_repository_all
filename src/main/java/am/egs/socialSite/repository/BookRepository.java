package am.egs.socialSite.repository;


import am.egs.socialSite.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface BookRepository extends JpaRepository<Book, Long> {
    Book findBookById(Long id);
}
