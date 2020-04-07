package am.egs.bookRepository.controller;

import am.egs.bookRepository.mappers.UserMapper;
import am.egs.bookRepository.model.Role;
import am.egs.bookRepository.model.User;
import am.egs.bookRepository.payload.UserDto;
import am.egs.bookRepository.repository.UserRepository;
import am.egs.bookRepository.security.UserPrincipal;
import am.egs.bookRepository.service.UserService;
import am.egs.bookRepository.util.ResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import static am.egs.bookRepository.util.Constant.*;

@Controller
@RequestMapping(value = USER)
public class UserController {

    @Autowired
    private MessageSource messageSource;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final UserRepository userRepository;
    private UserMapper userMapper;

    @Autowired
    public UserController(UserService userService, AuthenticationManager authenticationManager, UserRepository userRepository, UserMapper userMapper) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
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
                                  BindingResult result,Model model) {
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
        return "redirect:/user/userProfile";
    }

    @RequestMapping(value = "/userProfile", method = RequestMethod.GET)
    public ModelAndView currentUserName(@AuthenticationPrincipal UserPrincipal principal) {
        String email = principal.getUsername();
        User user = userRepository.findUserByEmail(email);
        userService.signInSuccess(email);
        UserDto userDto = userMapper.map(user, UserDto.class);
        String name  = userDto.getName();
        String surName  = userDto.getSurName();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", userDto);
        modelAndView.addObject("name", name);
        modelAndView.addObject("tab",  " ");
        modelAndView.addObject("surname", surName);
        modelAndView.setViewName("user-profile");
        return modelAndView;
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String current(@AuthenticationPrincipal UserPrincipal principal) {
        String email = principal.getUsername();
        User user = userRepository.findUserByEmail(email);
        userService.signInSuccess(email);
        List<Role> role = user.getRoles();
        if (role.stream().map(Role::getRole).anyMatch("USER"::equals)) {
            return "redirect:/user/userProfile";
        } else {
            return "redirect:/admin/admin-Profile";
        }
    }

    @RequestMapping("/edit")
    @ResponseBody
    public UserDto editUsersById(Long id) throws Exception {
        User user = userService.getUserById(id);
        UserDto userDto = userMapper.map(user, UserDto.class);
        return userDto;
    }


    @PostMapping(UPDATE)
    public String update(UserDto userDto, @AuthenticationPrincipal UserPrincipal principal) {
        userService.updateUser(userDto, principal);
        logger.info(" User account was successful updated.");
        return "redirect:/user/profile";
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
        return "redirect:/user/login";
    }

    /**
     * Error page.
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
