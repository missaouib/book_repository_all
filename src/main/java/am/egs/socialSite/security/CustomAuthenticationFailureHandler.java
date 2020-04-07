package am.egs.socialSite.security;

import am.egs.socialSite.service.UserService;
import am.egs.socialSite.util.ResponseStatus;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Custom Authentication Failure Handler handles a failed authentication attempt.
 * The  implemented logic  depends on the type of the exception.
 */
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    /**
     * LOGGER
     */
    private final Logger LOGGER = LoggerFactory.getLogger(CustomAuthenticationFailureHandler.class);

    /**
     * Constructor
     */

    private UserService userService;

    @Autowired
    public CustomAuthenticationFailureHandler(final UserService userService) {
        this.userService = userService;
    }

    public CustomAuthenticationFailureHandler() {
        LOGGER.debug("Auth Failed");
    }

    /**
     * Called when an authentication attempt fails.
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException, ServletException {
        final String methodName = "onAuthenticationFailure -> request: " + request + "; response: " + response;
        LOGGER.debug(methodName);
        LOGGER.debug("invalid login");

        int errorCode;
        String message = "";

        if (exception.getClass().equals(BadCredentialsException.class)
//                || exception.getClass().equals(ProviderNotFoundException.class)
//                || exception.getClass().equals(UsernameNotFoundException.class)
        ) {

            String email = request.getParameter("username");
            if (StringUtils.isNotEmpty(email)) {
                boolean exists = userService.exists(email);
                if (exists) {
                    userService.tryNumberIncrement(email);
                }
            }
            message = "Bad Credentials";
            errorCode = ResponseStatus.USER_BAD_CREDENTIALS.getCode();

        } else if (exception.getClass().equals(LockedException.class)) {
            errorCode = ResponseStatus.USER_LOCKED.getCode();
            message = "locked";

//        } else if (exception.getClass().equals(UsernameNotFoundException.class)) {
//            errorCode = ResponseStatus.USER_NOT_FOUND.getCode();
//            message = "Cannot find a user";
        } else if (exception.getClass().equals(AccountExpiredException.class)) {
            errorCode = ResponseStatus.UNAUTHORIZED_REQUEST.getCode();
            message = "Cannot find a user";
            String email = request.getParameter("username");
            userService.expired(email);
        } else if (exception.getClass().equals(DisabledException.class)) {
            errorCode = ResponseStatus.NOT_VERIFIED.getCode();
            message = "Email not verified";

        } else {
            message = "Bad Credentials";
            errorCode = ResponseStatus.USER_BAD_CREDENTIALS.getCode();
        }
        System.out.println(message);
        LOGGER.debug(message);

        request.getRequestDispatcher(String.format("/user/login-error?error=%s", errorCode)).forward(request, response);
    }
}

