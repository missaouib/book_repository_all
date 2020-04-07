package am.egs.socialSite.service.impl;

import am.egs.socialSite.model.Book;
import am.egs.socialSite.model.User;
import am.egs.socialSite.repository.BookRepository;
import am.egs.socialSite.service.BookService;
import org.apache.catalina.util.ErrorPageSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> findAllBooks() {
        return bookRepository.findAll();

    }
}
