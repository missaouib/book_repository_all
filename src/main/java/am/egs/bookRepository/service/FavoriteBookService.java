package am.egs.bookRepository.service;

import am.egs.bookRepository.model.Book;
import am.egs.bookRepository.model.FavoriteBook;
import am.egs.bookRepository.model.User;

import java.util.List;

public interface FavoriteBookService {

    void save(FavoriteBook favoriteBook);

    FavoriteBook findByUserAndBook(User user, Book book);

//    Object getOne(Long id);

    List<FavoriteBook> findByUser(User user);

}
