package am.egs.bookRepository.controller;

import am.egs.bookRepository.model.User;
import am.egs.bookRepository.payload.UserDto;
import am.egs.bookRepository.service.UserService;
import am.egs.bookRepository.util.ResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Locale;

import static am.egs.bookRepository.util.Constant.*;

@Controller
@RequestMapping("/main")
public class MainController {

    @Autowired
    private MessageSource messageSource;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public MainController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping(HOME_PAGE)
    public String indexPage() {
        return INDEX;
    }

    @GetMapping(SIGN_UP)
    public String registerPage(Model model, @ModelAttribute("user") UserDto userDto) {
        model.addAttribute("user", new UserDto());
        return REGISTER;
    }

    @PostMapping(SIGN_UP)
    public String registerNewUser(@Valid @ModelAttribute("user") @RequestBody UserDto userDto,
                                  BindingResult result, Model model) {
        logger.info("New user {}", userDto);

        if (!userDto.getPassword().equals(userDto.getMatchingPassword())) {
            result.rejectValue("password", "", "*Password not matching");
            return REGISTER;
        }
        User existing = userService.findByEmail(userDto.getEmail());
        if (existing != null) {
            result.rejectValue("email", null, "*There is already an account registered with that email");
            return REGISTER;
        }
        if (result.hasErrors()) {
            return REGISTER;
        }
        userService.addUser(userDto);
        logger.info(" User successfully registered.");
        String email = userDto.getEmail();
        model.addAttribute("email", email);
        return ACTIVATION_CODE;
    }

    @GetMapping(ACTIVATE_CODE)
    public String activate(@PathVariable String code) {
        userService.activateUser(code);
        logger.info(" Activation code successful  to user and successfully confirmed");
        return ACTIVATION_CODE;
    }

    @GetMapping(SIGN_IN)
    public String loginPage() {
        return LOGIN;
    }

    @PostMapping("/signed-successfully")
    public String signedSuccessfully() {
        return "redirect:/userPage/profile";
    }

    /**
     * Login form with error.
     */
    @RequestMapping("/login-error")
    public String loginError(@RequestParam(value = PAGE_ERROR, required = false) final int error, Model model, Locale locale) {

        ResponseStatus errorStatus = ResponseStatus.valueOf(error);
        String errorMessage;
        String messageKey;

        if (errorStatus != null) {
            messageKey = errorStatus.getMessageKey();
        } else {
            messageKey = USER_AUTH_FAILED;
        }
        errorMessage = messageSource.getMessage(messageKey, null, locale);
        model.addAttribute("loginError", true);
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("authenticationForm", new User());
        return "error";
    }

    /**
     * Login form with error.
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(Model model) {
        return "redirect:/main/signIn";
    }

    /**
     * Error page.
     * @return
     */
//    @RequestMapping(value = REQUEST_ERROR, method = RequestMethod.GET)
//    public String error(HttpServletRequest request, Model model) {
//        model.addAttribute(ERROR_CODE, ERROR + request.getAttribute("javax.servlet.error.status_code"));
//        StringBuilder errorMessage = new StringBuilder();
//        errorMessage.append("<ul>");
//
//        errorMessage.append("</ul>");
//        model.addAttribute(ERROR_MESSAGE, errorMessage.toString());
//        return PAGE_ERROR;
//    }

}
