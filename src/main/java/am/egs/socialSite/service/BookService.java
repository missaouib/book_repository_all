package am.egs.socialSite.service;

import am.egs.socialSite.model.Book;

import java.util.List;

public interface BookService {

    List<Book> findAllBooks();

    void addBook(Book book);

    void deleteBook(Long id);
}
