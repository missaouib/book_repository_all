package am.egs.bookRepository.service;

import am.egs.bookRepository.model.Book;
import am.egs.bookRepository.payload.BookDto;

import java.util.List;

public interface BookService {

    List<Book> findAllBooks();

    void addBook(Book book);

    void deleteBook(Long id);

    Book getOne(Long id);

    void update(BookDto bookDto);

    List<Book> findByTitleLike(String search);

    List<Book> findByInfo(String info);

}
