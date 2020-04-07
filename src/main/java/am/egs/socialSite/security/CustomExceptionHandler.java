package am.egs.socialSite.security;

import am.egs.socialSite.exception.DuplicateUserException;
import am.egs.socialSite.exception.UserNotFoundException;
import am.egs.socialSite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import static am.egs.socialSite.util.Constant.*;

@SuppressWarnings({"unchecked", "rawtypes"})
@ControllerAdvice
public class CustomExceptionHandler {

    @Autowired
    private UserService userService;

    @ExceptionHandler({
            UserNotFoundException.class,
            DuplicateUserException.class,
            LockedException.class,
            AccountExpiredException.class,
            BadCredentialsException.class
    })
    protected String customExceptionHandler(Exception ex, WebRequest wr) throws Exception {

        if (ex instanceof UserNotFoundException) {
            return ERROR_404;
        } else if (ex instanceof DuplicateUserException) {
            return ERROR_DUPLICATE_USER;
        } else if (ex instanceof LockedException) {
            return ERROR_423;
        } else if (ex instanceof AccountExpiredException) {
            String email = wr.getParameter("email");
            userService.expired(email);
            return ERROR_401;
        } else if (ex instanceof BadCredentialsException) {
            String email = wr.getParameter("email");
            String password = wr.getParameter("password");
            userService.tryNumberIncrement(email, password);
            return ERROR_403;
        } else {
            return String.valueOf(ex);
        }
    }
}