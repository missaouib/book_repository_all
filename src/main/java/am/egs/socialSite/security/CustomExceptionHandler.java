package am.egs.socialSite.security;

import am.egs.socialSite.exception.DuplicateUserException;
import am.egs.socialSite.exception.UserNotFoundException;
import am.egs.socialSite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;

import static am.egs.socialSite.util.Constant.*;

@SuppressWarnings({"unchecked", "rawtypes"})
@ControllerAdvice
public class CustomExceptionHandler {

    private UserService userService;


    @Autowired
    public CustomExceptionHandler(final UserService userService) {
        this.userService = userService;
    }

    @ExceptionHandler({
            UserNotFoundException.class,
            DuplicateUserException.class,
            LockedException.class,
            AccountExpiredException.class,
            BadCredentialsException.class
    })
    protected String customExceptionHandler(Exception ex, WebRequest wr) throws Exception {

//        if (ex instanceof UserNotFoundException) {
//            return ERROR_404;
//        }
        if (ex instanceof DuplicateUserException) {
            return ERROR_DUPLICATE_USER;
        }
//        } else if (ex instanceof LockedException) {
//            return ERROR_423;
//        } else if (ex instanceof AccountExpiredException) {
//            String email = wr.getParameter("email");
//            userService.expired(email);
//            return ERROR_401;
//        }
//        else if (ex instanceof BadCredentialsException) {
//            String email = wr.getParameter("email");
//            userService.tryNumberIncrement(email);
//            return ERROR_403;
//        }
        else {
            return String.valueOf(ex);
        }
    }

}