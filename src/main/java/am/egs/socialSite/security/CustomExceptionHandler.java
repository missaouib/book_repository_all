package am.egs.socialSite.security;

import am.egs.socialSite.exception.DuplicateUserException;
import am.egs.socialSite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static am.egs.socialSite.util.Constant.ERROR_DUPLICATE_USER;

@SuppressWarnings({"unchecked", "rawtypes"})
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    private UserService userService;

    @Autowired
    public CustomExceptionHandler(final UserService userService) {
        this.userService = userService;
    }

    @ExceptionHandler({DuplicateUserException.class})
    protected String customExceptionHandler(Exception ex) throws Exception {
        if (ex instanceof DuplicateUserException) {
            return ERROR_DUPLICATE_USER;
        } else {
            return String.valueOf(ex);
        }
    }
}