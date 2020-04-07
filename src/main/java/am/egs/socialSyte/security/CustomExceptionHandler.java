package am.egs.socialSyte.security;

import am.egs.socialSyte.exception.DuplicateUserException;
import am.egs.socialSyte.exception.UserNotFoundException;
import am.egs.socialSyte.payload.ErrorDto;
import am.egs.socialSyte.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked", "rawtypes"})
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private UserService userService;

    @ExceptionHandler(value
            = {UserNotFoundException.class})
    protected ResponseEntity<Object> handleException(UserNotFoundException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorDto error = new ErrorDto(" There are not exist this user.", details);
        return new ResponseEntity(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value
            = {DuplicateUserException.class})
    protected ResponseEntity<Object> handleException(DuplicateUserException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorDto error = new ErrorDto(" There is already a user with this email", details);
        return new ResponseEntity(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value
            = {LockedException.class})
    protected ResponseEntity<Object> handleException(LockedException ex,WebRequest wr) {

        String email = wr.getParameter("email");
        String password = wr.getParameter("password");

        userService.isUserNonLocked(email, password);
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorDto error = new ErrorDto("  User is locked!" + "\n"
                + " You can try to login after 12 hours. ", details);
        return new ResponseEntity(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value
            = {BadCredentialsException.class})
    protected ResponseEntity<Object> handleException(BadCredentialsException ex, WebRequest wr) {
        String email = wr.getParameter("email");
        String password = wr.getParameter("password");
        userService.tryNuymberIncrement(email, password);
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorDto error = new ErrorDto(" Your email or password are wrong"
                + "\n"
                + " Please enter a valid email and password ", details);
        return new ResponseEntity(error, HttpStatus.CONFLICT);
    }
}

//        @ExceptionHandler(value
//            = {UserLockedException.class})
//    public ResponseEntity<Object> handleException1(UserLockedException ex) throws Exception {
//        List<String> details = new ArrayList<>();
//        details.add(ex.getLocalizedMessage());
//        ErrorDto error = new ErrorDto(" User is locked!", details);
//        return new ResponseEntity(error, HttpStatus.CONFLICT);
//    }
//
//    @ExceptionHandler(value
//            = {EmailNotValidException.class})
//    protected ResponseEntity<Object> handleException(EmailNotValidException ex) {
//        List<String> details = new ArrayList<>();
//        details.add(ex.getLocalizedMessage());
//        ErrorDto error = new ErrorDto(" Email is not valid.", details);
//        return new ResponseEntity(error, HttpStatus.CONFLICT);
//    }