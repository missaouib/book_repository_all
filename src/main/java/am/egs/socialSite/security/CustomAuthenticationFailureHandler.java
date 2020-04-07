package am.egs.socialSite.security;

import am.egs.socialSite.exception.UserNotFoundException;
import am.egs.socialSite.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private UserService userService;

    @Autowired
    public CustomAuthenticationFailureHandler(final UserService userService) {
        this.userService = userService;
    }

    /**
     * Called when an authentication attempt fails.
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        if (exception.getClass().equals(BadCredentialsException.class)) {
            String email = request.getParameter("username");
            if (StringUtils.isNotEmpty(email)) {
                boolean exists = userService.exists(email);
                if (exists) {
                    userService.tryNumberIncrement(email);
                }
            }
            // write your custom code here
            response.sendRedirect("/user/loginFailed");
        } else if (exception.getClass().equals(LockedException.class)) {
            response.sendRedirect("/user/error-423");
        } else if (exception.getClass().equals(AccountExpiredException.class)) {
            response.sendRedirect("/user/error-401");
        } else if (exception.getClass().equals(UserNotFoundException.class)) {
            response.sendRedirect("/user/error-404");
        }

    }

//    @Override
//    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
//                                        AuthenticationException exception) throws IOException, ServletException {
//
//
//        if (exception.getClass().equals(BadCredentialsException.class)) {
//            String email = request.getParameter("username");
//            userService.tryNumberIncrement(email);
//            response.sendRedirect(ERROR_403);
////            throw new BadCredentialsException("Bad credential, Username or password incorrect");
//        } else if (exception.getClass().equals(LockedException.class)) {
//            //TODO
//        }
//    }
}

