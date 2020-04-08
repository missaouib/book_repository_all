package am.egs.bookRepository.service.impl;

import am.egs.bookRepository.mappers.BookMapper;
import am.egs.bookRepository.model.Book;
import am.egs.bookRepository.payload.BookDto;
import am.egs.bookRepository.repository.BookRepository;
import am.egs.bookRepository.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private BookMapper bookMapper;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    public List<Book> findAllBooks() {
        return bookRepository.findAll();

    }

    @Override
    public void addBook(Book book) {
        bookRepository.save(book);
    }

    @Override
    public void deleteBook(Long id) {
        bookRepository.findById(id)
                .ifPresent(book -> bookRepository.delete(book));
    }

    public Book getOne(Long id) {
        return bookRepository.findBookById(id);
    }

    public void update(BookDto bookDto) {
        Book book = bookMapper.map(bookDto, Book.class);
        bookRepository.save(book);
    }

    @Override
    public List<Book> findByTitleLike(String search) {
        return bookRepository.findByTitleLike("%" + search + "%");
    }

    @Override
    public List<Book> findByInfo(String info) {
        return bookRepository.findByInfo(info);
    }

}
