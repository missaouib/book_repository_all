package am.egs.bookRepository.controller;

import am.egs.bookRepository.model.Book;
import am.egs.bookRepository.model.FavoriteBook;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
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
    public ModelAndView addFavoriteBook(@Valid FavoriteBook favoriteBook, BindingResult bindingResult,
                                        @PathVariable Long id, @AuthenticationPrincipal UserPrincipal principal) {
        String email = principal.getUsername();
        User user = userService.findByEmail(email);
        Book book = bookService.getOne(id);
        FavoriteBook favoriteBookFromDb = favoriteBookService.findByUserAndBook(user, book);
        ModelAndView modelAndView = new ModelAndView();
        final List<Book> bookList = bookService.findAllBooks();
        if (favoriteBookFromDb == null) {
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
        return modelAndView;
    }
}