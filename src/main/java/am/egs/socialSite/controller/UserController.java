package am.egs.socialSite.controller;

import am.egs.socialSite.mappers.UserMapper;
import am.egs.socialSite.model.User;
import am.egs.socialSite.payload.UserDto;
import am.egs.socialSite.repository.UserRepository;
import am.egs.socialSite.security.UserPrincipal;
import am.egs.socialSite.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

import java.time.LocalDateTime;
import java.util.Optional;

import static am.egs.socialSite.util.Constant.*;

@Controller
@RequestMapping(value = USER)
public class UserController {

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

//    @PostMapping("/signIn")
//    public String loginUser(@RequestParam("email") String email, @RequestParam("password") String password) {
//        final Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
//        SecurityContextHolder.getContext().setAuthentication(authenticate);
//        userService.signIn(email, password);
//        logger.info(" User successful logged.");
//        return "redirect:/user/userProfile";
//    }


    @RequestMapping(value = "/userProfile", method = RequestMethod.GET)
    public ModelAndView currentUserName(@AuthenticationPrincipal UserPrincipal principal) {
        String email = principal.getUsername();
        System.out.println(email);
        User user = userRepository.findUserByEmail(email);
        UserDto userDto = userMapper.map(user, UserDto.class);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("time", LocalDateTime.now());
        modelAndView.addObject("user", userDto);
        modelAndView.setViewName("user-Profile");
        return modelAndView;
    }


    @RequestMapping(value = "/admin-Profile", method = RequestMethod.GET)
    public ModelAndView currentAdminName(@AuthenticationPrincipal UserPrincipal principal) {
        String email = principal.getUsername();
        System.out.println(email);
        User user = userRepository.findUserByEmail(email);
        UserDto userDto = userMapper.map(user, UserDto.class);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("time", LocalDateTime.now());
        modelAndView.addObject("user", userDto);
        modelAndView.setViewName("admin-Profile");
        return modelAndView;
    }

    @RequestMapping(path = {"/edit", "/edit/{id}"})
    public String editEmployeeById(Model model, @PathVariable("id") Optional<Long> id) throws Exception {
        if (id.isPresent()) {
            User entity = userService.getEmployeeById(id.get());
            model.addAttribute("employee", entity);
//        } else {
//            model.addAttribute("employee", new User());
//        }
        }
        return "edit-user";

    }

    @RequestMapping(path = "/createEmployee", method = RequestMethod.POST)
    public String createOrUpdateEmployee(User employee) {
        userService.createOrUpdateEmployee(employee);
        return "redirect:/admin/read";
    }

    @PostMapping(UPDATE)
    public ResponseEntity update(@RequestBody User user) {
        userService.update(user);
        logger.info(" User account was successful updated.");
        return new ResponseEntity("User successful updated!", HttpStatus.MOVED_PERMANENTLY);
    }


    @RequestMapping(value = "/loginFailed")
    public String loginFailed() {
        return "error-403";
    }

    @RequestMapping(value = "/error-423")
    public String userLocked() {
        return "error-423";
    }

    @RequestMapping(value = "/error-401")
    public String userExpired() {
        return "error-401";
    }

    @RequestMapping(value = "/error-404")
    public String usernameNotFound() {
        return "error-404";
    }


}
