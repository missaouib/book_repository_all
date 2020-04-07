package am.egs.socialSite.controller;

import am.egs.socialSite.mappers.UserMapper;
import am.egs.socialSite.model.Role;
import am.egs.socialSite.model.User;
import am.egs.socialSite.payload.UserDto;
import am.egs.socialSite.repository.UserRepository;
import am.egs.socialSite.security.UserPrincipal;
import am.egs.socialSite.service.UserService;
import am.egs.socialSite.util.ResponseStatus;
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
import java.util.Optional;

import static am.egs.socialSite.util.Constant.*;

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
                                  BindingResult result) {
        logger.info("New user {}", userDto);
        if (result.hasErrors()) {
            return REGISTER;
        }
        User existing = userRepository.findUserByEmail(userDto.getEmail());
        if (existing != null) {
            result.rejectValue("email", null, "There is already an account registered with that email");
        }
        userService.addUser(userDto);
        logger.info(" User successful registered.");
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
        System.out.println(email);
        User user = userRepository.findUserByEmail(email);
        userService.signInSuccess(email);
        UserDto userDto = userMapper.map(user, UserDto.class);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("time", LocalDateTime.now());
        modelAndView.addObject("user", userDto);
        modelAndView.setViewName("user-profile");
        return modelAndView;
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String current(@AuthenticationPrincipal UserPrincipal principal) {
        String email = principal.getUsername();
        System.out.println(email);
        User user = userRepository.findUserByEmail(email);
        userService.signInSuccess(email);
        List<Role> role = user.getRoles();
        if (role.stream().map(Role::getRole).anyMatch("USER"::equals)) {
            return "redirect:/user/userProfile";
        } else {
            return "redirect:/admin/admin-Profile";
        }
    }

    @RequestMapping(path = {"/edit", "/edit/{id}"})
    public String editUsersById(Model model, @PathVariable("id") Optional<Long> id) throws Exception {
        if (id.isPresent()) {
            User entity = userService.getUserById(id.get());
            model.addAttribute("user", entity);
        } else {
            model.addAttribute("user", new User());
        }
        return "user-edit";
    }


    @PostMapping(UPDATE)
    public String update(User user, @AuthenticationPrincipal UserPrincipal principal) {
        userService.updateUser(user, principal);
        logger.info(" User account was successful updated.");
        return "redirect:/user/profile";
    }

    /**
     * Login form with error.
     */
    @RequestMapping("/login-error")
    public String loginError(@RequestParam(value = PAGE_ERROR, required = false) final int error, Model model, Locale locale){
//                             @RequestParam("email") String email){
//        System.out.println(email);
//        User user = userRepository.findUserByEmail(email);

        ResponseStatus errorStatus = ResponseStatus.valueOf(error);
        String errorMessage;
        String messageKey;

        if (errorStatus != null) {
            messageKey = errorStatus.getMessageKey();

//        }  if (errorStatus != null && !user.getEmailVerified()) {
//            messageKey = NOT_VERIFIED;

        }else {
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
