package am.egs.bookRepository.controller;

import am.egs.bookRepository.mappers.BookMapper;
import am.egs.bookRepository.model.Book;
import am.egs.bookRepository.payload.BookDto;
import am.egs.bookRepository.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static am.egs.bookRepository.util.Constant.READ;

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
    public String creatNewBook(@Valid BookDto bookDto) {
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

    @RequestMapping("/getOne")
    @ResponseBody
    public Book getOne(Long id) {
        return bookService.getOne(id);
    }

    @RequestMapping(value="/update", method = {RequestMethod.PUT, RequestMethod.GET})
    public String update(BookDto bookDto) {
        bookService.update(bookDto);
        return "redirect:/book/read";
    }

}
