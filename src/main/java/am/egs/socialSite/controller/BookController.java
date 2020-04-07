package am.egs.socialSite.controller;

import am.egs.socialSite.mappers.BookMapper;
import am.egs.socialSite.model.Book;
import am.egs.socialSite.payload.BookDto;
import am.egs.socialSite.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static am.egs.socialSite.util.Constant.READ;

@Controller
@RequestMapping("/book")
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    private BookService bookService;
    private BookMapper bookMapper;

    @Autowired
    public BookController(BookService bookService, BookMapper bookMapper) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }

    @GetMapping(READ)
    public String readBooksList(Model model) {
        logger.info(" User getting books read.");
        final List<Book> bookList = bookService.findAllBooks();
        model.addAttribute("books", bookList);
        logger.info(" User successfully read list of all books.");
        return "books-list";
    }

    @PostMapping("/create")
    public String creatNewBook(BookDto bookDto) {
        Book book = bookMapper.map(bookDto, Book.class);
        logger.info("User getting creat book " + book);
        bookService.addBook(book);
        logger.info(" User successfully create book." + book);
        return "redirect:/book/read";
    }

    @GetMapping(value = "/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        logger.info("User getting delete book " + id);
        bookService.deleteBook(id);
        logger.info(" User successfully delete book.");
        return "redirect:/book/read";
    }
}
