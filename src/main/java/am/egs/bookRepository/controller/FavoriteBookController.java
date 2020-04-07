package am.egs.bookRepository.controller;

import am.egs.bookRepository.model.Book;
import am.egs.bookRepository.model.FavoriteBook;
import am.egs.bookRepository.model.Role;
import am.egs.bookRepository.model.User;
import am.egs.bookRepository.security.UserPrincipal;
import am.egs.bookRepository.service.BookService;
import am.egs.bookRepository.service.FavoriteBookService;
import am.egs.bookRepository.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping("/favoriteBook")
public class FavoriteBookController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private FavoriteBookService favoriteBookService;
    private BookService bookService;
    private UserService userService;

    @Autowired
    public FavoriteBookController(FavoriteBookService favoriteBookService, BookService bookService, UserService userService) {
        this.favoriteBookService = favoriteBookService;
        this.bookService = bookService;
        this.userService = userService;
    }


    @GetMapping(value = "/add/{id}")
    public ModelAndView addFavoriteBook(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal principal) {
        logger.info(" User getting add favorite book.");
        User user = getUser(principal);
        Book book = bookService.getOne(id);
        FavoriteBook favoriteBookFromDb = favoriteBookService.findByUserAndBook(user, book);
        ModelAndView modelAndView = new ModelAndView();
        final List<Book> bookList = bookService.findAllBooks();
        if (favoriteBookFromDb == null) {
            FavoriteBook favoriteBook = new FavoriteBook();
            favoriteBook.setUser(user);
            favoriteBook.setBook(book);
            favoriteBookService.save(favoriteBook);
            modelAndView.addObject("books", bookList);
            modelAndView.addObject("process", "SUCCESS");
            modelAndView.addObject("pw_success", "Well done! You successfully  make this book favorite.");
        } else {
            modelAndView.addObject("books", bookList);
            modelAndView.addObject("process", "ERROR");
            modelAndView.addObject("pw_error", "Error : This book already favorite for you!");
        }
        modelAndView.setViewName("books-list");
        logger.info(" User successfully added favorite book.");
        return modelAndView;
    }

    @GetMapping("/all")
    public ModelAndView readFavoriteBooksList(ModelAndView modelAndView, @AuthenticationPrincipal UserPrincipal principal) {
        logger.info(" User getting favorite books read.");
        List<Book> books = showFavoriteBooks(principal);
        modelAndView.addObject("favoriteBooks", books);
        String control = showRole(principal);
        modelAndView.addObject("control", control);
        modelAndView.setViewName("favoriteBooks-list");
        logger.info(" User successfully read list of all favorite books.");
        return modelAndView;
    }

    @GetMapping(value = "/delete/{id}")
    public ModelAndView deleteBook(ModelAndView modelAndView, @PathVariable Long id, @AuthenticationPrincipal UserPrincipal principal) {
        logger.info("User getting delete favorite book " + id);
        Book book = bookService.getOne(id);
        User user = getUser(principal);
        favoriteBookService.delete(user, book);
        List<Book> books = showFavoriteBooks(principal);
        modelAndView.addObject("favoriteBooks", books);
        String control = showRole(principal);
        modelAndView.addObject("control", control);
        modelAndView.addObject("process", "SUCCESS");
        modelAndView.addObject("pw_success", "Well done! You successfully  delete this favorite book.");
        modelAndView.setViewName("favoriteBooks-list");
        logger.info(" User successfully delete favorite book.");
        return modelAndView;
    }


    public List<Book> showFavoriteBooks(UserPrincipal principal) {
        User user = getUser(principal);
        final List<FavoriteBook> userFavoriteBooks = favoriteBookService.findByUser(user);
        Iterator<FavoriteBook> iterator = userFavoriteBooks.iterator();
        List<Book> books = new ArrayList<>();
        while (iterator.hasNext()) {
            FavoriteBook favoriteBook = iterator.next();
            Book book = favoriteBook.getBook();
            books.add(book);
        }
        return books;
    }

    public List<Role> getRole(UserPrincipal principal) {
        User user = getUser(principal);
        List<Role> role = user.getRoles();
        return role;
    }

    public User getUser(UserPrincipal principal) {
        String email = principal.getUsername();
        User user = userService.findByEmail(email);
        return user;
    }

    public String showRole(UserPrincipal principal) {
        List<Role> role = getRole(principal);
        if (role.stream().map(Role::getRole).anyMatch("USER"::equals)) {
            return "USER";
        } else
        return "ADMIN";
    }
}