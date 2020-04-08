package am.egs.bookRepository.controller;

import am.egs.bookRepository.mappers.BookMapper;
import am.egs.bookRepository.model.Book;
import am.egs.bookRepository.model.Role;
import am.egs.bookRepository.model.User;
import am.egs.bookRepository.payload.BookDto;
import am.egs.bookRepository.security.UserPrincipal;
import am.egs.bookRepository.service.BookService;
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
import java.util.List;

import static am.egs.bookRepository.util.Constant.READ;

@Controller
@RequestMapping("/book")
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    private BookService bookService;
    private BookMapper bookMapper;
    private UserService userService;

    @Autowired
    public BookController(BookService bookService, BookMapper bookMapper, UserService userService) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
        this.userService = userService;
    }

    @GetMapping(READ)
    public ModelAndView readBooksList(ModelAndView modelAndView, @AuthenticationPrincipal UserPrincipal principal) {
        logger.info(" User getting books read.");
        final List<Book> bookList = bookService.findAllBooks();
        modelAndView.addObject("books", bookList);
        modelAndView.addObject("control", showRole(principal));
        modelAndView.setViewName("books-list");
        logger.info(" User successfully read list of all books.");
        return modelAndView;
    }

    @GetMapping("/showBookCreateForm")
    public ModelAndView showBookCreateForm(@AuthenticationPrincipal UserPrincipal principal, BookDto bookDto) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("control", showRole(principal));
        modelAndView.addObject("book", bookDto);
        modelAndView.setViewName("book_add");
        return modelAndView;
    }

    @RequestMapping(value = "/saveNewBook", method = RequestMethod.POST)
    public ModelAndView saveNewBook(@AuthenticationPrincipal UserPrincipal principal,
                                    @Valid @ModelAttribute("book") BookDto bookDto, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        List<Book> book = bookService.findByInfo(bookDto.getInfo());
        if(!book.isEmpty()){
            bindingResult.rejectValue("info", " ", "*There is already this book!");
            modelAndView.addObject("control", showRole(principal));
            modelAndView.addObject("book",bookDto);
            modelAndView.setViewName("book_add");
            return modelAndView;
        }
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("book", bookDto);
            modelAndView.addObject("control", showRole(principal));
            modelAndView.setViewName("book_add");
        }else {
            Book newBook = bookMapper.map(bookDto, Book.class);
            logger.info("User getting creat book " + newBook);
            bookService.addBook(newBook);
            modelAndView.addObject("control", showRole(principal));
            final List<Book> bookList = bookService.findAllBooks();
            modelAndView.addObject("books", bookList);
            modelAndView.addObject("process", "SUCCESS");
            modelAndView.addObject("pw_success", "Well done! You successfully  create this book.");
            modelAndView.setViewName("books-list");
            logger.info(" User successfully create book." + newBook);
        }
        return modelAndView;
    }

//start>>>for create book using modal form
//    @PostMapping("/create")
//    public ModelAndView creatNewBook(@Valid BookDto bookDto, @AuthenticationPrincipal UserPrincipal principal) {
//        Book book = bookMapper.map(bookDto, Book.class);
//        logger.info("User getting creat book " + book);
//        bookService.addBook(book);
//        ModelAndView modelAndView = new ModelAndView();
//        final List<Book> bookList = bookService.findAllBooks();
//        modelAndView.addObject("books", bookList);
//        modelAndView.addObject("control", showRole(principal));
//        modelAndView.addObject("process", "SUCCESS");
//        modelAndView.addObject("pw_success", "Well done! You successfully  create this book.");
//        modelAndView.setViewName("books-list");
//        logger.info(" User successfully create book." + book);
//        return modelAndView;
//    }
//end>>>

    @GetMapping(value = "/delete/{id}")
    public ModelAndView deleteBook(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal principal) {
        logger.info("User getting delete book " + id);
        bookService.deleteBook(id);
        ModelAndView modelAndView = new ModelAndView();
        final List<Book> bookList = bookService.findAllBooks();
        modelAndView.addObject("books", bookList);
        modelAndView.addObject("control", showRole(principal));
        modelAndView.addObject("process", "SUCCESS");
        modelAndView.addObject("pw_success", "Well done! You successfully  delete this book.");
        modelAndView.setViewName("books-list");
        logger.info(" You successfully delete this book.");
        return modelAndView;
    }

    @RequestMapping("/getOne")
    @ResponseBody
    public BookDto getOne(Long id) {
        Book book = bookService.getOne(id);
        System.out.println(book.toString());
        BookDto bookDto = bookMapper.map(book, BookDto.class);
        return bookDto;
    }

    @RequestMapping(value = "/update", method = {RequestMethod.PUT, RequestMethod.GET})
    public ModelAndView update(BookDto bookDto, @AuthenticationPrincipal UserPrincipal principal) {
        logger.info(" User getting update this book.");
        bookService.update(bookDto);
        ModelAndView modelAndView = new ModelAndView();
        final List<Book> bookList = bookService.findAllBooks();
        modelAndView.addObject("books", bookList);
        modelAndView.addObject("control", showRole(principal));
        modelAndView.addObject("process", "SUCCESS");
        modelAndView.addObject("pw_success", "Well done! You successfully  updated this book.");
        modelAndView.setViewName("books-list");
        logger.info(" You successfully updated this book.");
        return modelAndView;
    }

    @GetMapping(value = "/info/{id}")
    public ModelAndView bookInfo(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView();
        Book book = bookService.getOne(id);
        modelAndView.addObject("book", book);
        modelAndView.setViewName("book_info");
        return modelAndView;
    }

    @GetMapping(value = "/search")
    public ModelAndView search(@RequestParam(required = false, value = "search") String search,
                               @AuthenticationPrincipal UserPrincipal principal) {
        ModelAndView modelAndView = new ModelAndView();

        List<Book> searchResult = bookService.findByTitleLike(search);
        if (searchResult.isEmpty()) {
            final List<Book> bookList = bookService.findAllBooks();
            modelAndView.addObject("books", bookList);
            modelAndView.addObject("control", showRole(principal));
            modelAndView.addObject("process", "ERROR");
            modelAndView.addObject("pw_error", "Error : Oops, no result!");
            modelAndView.setViewName("books-list");
        } else {
            modelAndView.addObject("control", showRole(principal));

            modelAndView.addObject("process", "SUCCESS");
            modelAndView.addObject("pw_success", "Well done! Enjoy!");
            modelAndView.addObject("searchResult", searchResult);
            modelAndView.setViewName("search");
        }
        return modelAndView;
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
