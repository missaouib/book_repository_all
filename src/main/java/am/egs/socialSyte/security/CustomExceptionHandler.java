package am.egs.socialSyte.security;

import am.egs.socialSyte.controller.UserController;
import am.egs.socialSyte.exception.DuplicateUserException;
import am.egs.socialSyte.exception.UserNotFoundException;
import am.egs.socialSyte.payload.ErrorDto;
import am.egs.socialSyte.payload.auth.SignInRequest;
import am.egs.socialSyte.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked", "rawtypes"})
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @ExceptionHandler(value
            = {UserNotFoundException.class})
    protected ResponseEntity<Object> handleException(UserNotFoundException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorDto error = new ErrorDto(" There are not exist this user.", details);
        logger.error(" There are not exist this user.");
        return new ResponseEntity(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value
            = {DuplicateUserException.class})
    protected ResponseEntity<Object> handleException(DuplicateUserException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorDto error = new ErrorDto(" There is already a user with this email.", details);
        logger.error(" There is already a user with this email.");
        return new ResponseEntity(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value
            = {LockedException.class})
    protected Object handleException(LockedException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorDto error = new ErrorDto("  User is locked!" + "\n"
                + " You can try to login after 12 hours. ", details);
        logger.error("User is locked! And user can try to login after 12 hours.");
        return new ResponseEntity(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value
            = {AccountExpiredException.class})
    protected ResponseEntity<Object> handleException(AccountExpiredException ex, WebRequest wr) {
        String email = wr.getParameter("email");
        userService.expired(email);
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorDto error = new ErrorDto(" User account has expired.", details);
        logger.error(" User account has expired.");
        return new ResponseEntity(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value
            = {BadCredentialsException.class})
    protected ResponseEntity<Object> handleException(BadCredentialsException ex,WebRequest wr) {

        String email = wr.getParameter("email");
        String password = wr.getParameter("password");

        userService.tryNumberIncrement(email, password);
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorDto error = new ErrorDto(" Your email or password are wrong"
                + "\n"
                + " Please enter a valid email and password ", details);
        logger.error(" User email or password are wrong.");
        return new ResponseEntity(error, HttpStatus.CONFLICT);
    }
}