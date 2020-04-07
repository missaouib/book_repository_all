package am.egs.socialSyte.controller;

import am.egs.socialSyte.model.User;
import am.egs.socialSyte.payload.auth.AuthResponse;
import am.egs.socialSyte.service.UserService;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private AuthResponse authResponse;

    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/activate/{code}")
    public ResponseEntity activate(@PathVariable String code) {
        userService.activateUser(code);
        authResponse = AuthResponse.builder()
                .message("Your email was successfully confirmed")
                .build();
        logger.info("User successful loged.");
        return ResponseEntity.ok().body(authResponse);
    }

    @PostMapping("/signUp")
    public ResponseEntity registerUser(@Valid @RequestBody User user) {
        logger.info("New user", user);
        userService.addUser(user);
        authResponse = AuthResponse.builder()
                .message("User successfully registered.")
                .build();
        logger.info("User successful registered.");
        return ResponseEntity.ok().body(authResponse);
    }

    @PostMapping("/signIn")
    public ResponseEntity loginUser(@RequestParam("email") String email, @RequestParam("password") String password) {
        final Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        authResponse = userService.signIn(email, password);
        authResponse = AuthResponse.builder()
                .message("User successfully loged.")
                .build();
        logger.info("User successful loged.");
        return ResponseEntity.ok().body(authResponse);
    }

    @PostMapping("/update")
    public ResponseEntity update(@RequestBody User user) {
       userService.update(user);
         return new ResponseEntity("User successful updated!", HttpStatus.MOVED_PERMANENTLY);
    }
}
