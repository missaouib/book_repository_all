package am.egs.bookRepository.repository;


import am.egs.bookRepository.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface BookRepository extends JpaRepository<Book, Long> {
    Book findBookById(Long id);

    List<Book> findByTitleLike(String search);

    List<Book> findByInfo(String info);
}
