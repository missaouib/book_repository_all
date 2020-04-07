package am.egs.bookRepository.controller;

import am.egs.bookRepository.model.Book;
import am.egs.bookRepository.model.FavoriteBook;
import am.egs.bookRepository.model.User;
import am.egs.bookRepository.payload.BookDto;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

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
    public String addFavoriteBook(
//            @Valid BookDto bookDto, BindingResult bindingResult,
            @PathVariable Long id, @AuthenticationPrincipal UserPrincipal principal) {
        FavoriteBook favoriteBook = new FavoriteBook();

        String email = principal.getUsername();
        System.out.println(email);
        User user = userService.findByEmail(email);
        Book book = bookService.getOne(id);

        favoriteBook.setUser(user);
        favoriteBook.setBook(book);
        favoriteBookService.save(favoriteBook);


//        ModelAndView modelAndView = new ModelAndView();

//        if (favoriteBook.) {
//            modelAndView.addObject("process", "ERROR");
//            modelAndView.addObject("pw_error", "Error : Check your old password!");
//        }else{
//            modelAndView.addObject("process", "SUCCESS");
//            modelAndView.addObject("pw_success", "Well done! You successfully change your password.");
//            modelAndView.addObject("book-list");
//        }

//        List<Book> favoriteBook = Collections.singletonList(book.get());
//        user.setFavoriteBooks(favoriteBook);
//        userRepository.save(user);
//        favoriteBookService.save();
//
//        logger.info(" User successfully delete book.");
        return "redirect:/book/read";
//        return modelAndView;
    }

//    @RequestMapping(value = "/save", method = RequestMethod.POST)
//    public ModelAndView saveUserTask(@Valid UserTask userTask, BindingResult bindingResult) {
//        ModelAndView modelAndView = new ModelAndView("redirect:/admin/user-task/all");
//        modelAndView.addObject("auth", getUser());
//        modelAndView.addObject("control", getUser().getRole().getRole());
//        userTaskService.save(userTask);
//        return modelAndView;
//    }

}
