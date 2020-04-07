package am.egs.socialSite.service;

import am.egs.socialSite.model.Book;
import am.egs.socialSite.payload.BookDto;

import java.util.List;

public interface BookService {

    List<Book> findAllBooks();

    void addBook(Book book);

    void deleteBook(Long id);

    Book getOne(Long id);

    void update(BookDto bookDto);
}
