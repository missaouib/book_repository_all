package am.egs.bookRepository.repository;

import am.egs.bookRepository.model.Book;
import am.egs.bookRepository.model.FavoriteBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface FavoriteBookRepository extends JpaRepository<FavoriteBook, Long> {
    Book findBookById(Long id);
}



