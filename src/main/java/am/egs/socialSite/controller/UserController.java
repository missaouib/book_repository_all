package am.egs.socialSite.controller;

import am.egs.socialSite.model.User;
import am.egs.socialSite.payload.UserDto;
import am.egs.socialSite.repository.UserRepository;
import am.egs.socialSite.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static am.egs.socialSite.util.Constant.*;

@Controller
@RequestMapping(value = USER)
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserService userService, AuthenticationManager authenticationManager, UserRepository userRepository) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    @PostMapping(ACTIVATE_CODE)
    public String activate(@PathVariable String code) {
        userService.activateUser(code);
        logger.info(" Activation code successful sended to user and successfully confirmed");
        return ACTIVATION_CODE;
    }

    @GetMapping(HOME_PAGE)
    public String indexPage() {
        return INDEX;
    }


    @GetMapping(SIGN_UP)
    public String registerPage(Model model,@ModelAttribute("user") UserDto userDto) {
        model.addAttribute("user", new UserDto());
        return REGISTER;
    }

    @PostMapping(SIGN_UP)
    public String registerNewUser( @Valid @ModelAttribute("user") @RequestBody UserDto userDto,
                                  BindingResult result) {
        logger.info("New user {}", userDto);
        if(result.hasErrors()){
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

    @GetMapping(SIGN_IN)
    public String loginPage() {
        return LOGIN;
    }

    @PostMapping(SIGN_IN)
    public String loginUser(@RequestParam("email") String email, @RequestParam("password") String password) {
        final Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        userService.signIn(email, password);
        logger.info(" User successful loged.");
        return USER_PROFILE;
    }

    @PostMapping(UPDATE)
    public ResponseEntity update(@RequestBody User user) {
        userService.update(user);
        logger.info(" User account was successful updated.");
        return new ResponseEntity("User successful updated!", HttpStatus.MOVED_PERMANENTLY);
    }
}
