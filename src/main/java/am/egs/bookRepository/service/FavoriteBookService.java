package am.egs.bookRepository.service;

import am.egs.bookRepository.model.Book;
import am.egs.bookRepository.model.FavoriteBook;

import java.util.List;
import java.util.Optional;

public interface FavoriteBookService {

    Book getOne(Long id);

    void save(FavoriteBook favoriteBook);
}
