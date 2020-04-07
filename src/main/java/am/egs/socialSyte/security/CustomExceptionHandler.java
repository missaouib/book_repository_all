package am.egs.socialSyte.security;

import am.egs.socialSyte.exception.DuplicateUserException;
import am.egs.socialSyte.exception.EmailNotValidException;
import am.egs.socialSyte.exception.UserLockedException;
import am.egs.socialSyte.exception.UserNotFoundException;
import am.egs.socialSyte.payload.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked", "rawtypes"})
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value
            = {DuplicateUserException.class})
    protected ResponseEntity<Object> handleException(DuplicateUserException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorDto error = new ErrorDto(" There is already a user with this email", details);
        // return new ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value
            = {UserLockedException.class})
    protected ResponseEntity<Object> handleException(UserLockedException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorDto error = new ErrorDto(" User is locked!", details);
        return new ResponseEntity(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value
            = {EmailNotValidException.class})
    protected ResponseEntity<Object> handleException(EmailNotValidException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorDto error = new ErrorDto(" Email is not valid.", details);
        return new ResponseEntity(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value
            = {UserNotFoundException.class})
    protected ResponseEntity<Object> handleException(UserNotFoundException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorDto error = new ErrorDto(" There are not exist this user.", details);
        // return new ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value
            = {BadCredentialsException.class})
    protected ResponseEntity<Object> handleException(BadCredentialsException ex) {

        // stex mi tex kanchel tryNumber increment anox metod@

        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorDto error = new ErrorDto(" Your email or password are wrong"
                + "\n"
                + " Please enter a valid email and password ", details);
        return new ResponseEntity(error, HttpStatus.CONFLICT);
    }
}