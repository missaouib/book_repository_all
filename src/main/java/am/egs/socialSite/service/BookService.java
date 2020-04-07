package am.egs.socialSite.service;

import am.egs.socialSite.model.Book;
import am.egs.socialSite.model.User;

import java.util.List;

public interface BookService {
    List<Book> findAllBooks();
}
