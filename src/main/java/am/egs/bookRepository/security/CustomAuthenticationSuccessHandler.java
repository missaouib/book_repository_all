package am.egs.bookRepository.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Spring Security will send control to AuthenticationSuccessHandler when authentication will get success
 */
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws
            IOException,
            ServletException {
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        if (user instanceof UserPrincipal) {
            UserDetails authUser = (UserDetails) user;
            authUser.getUsername();

            authentication.getAuthorities();

            System.out.println("principal" + authUser.getUsername());

            response.sendRedirect("/userPage/profile");


//            boolean isAdmin = false;
//            Iterator<GrantedAuthority> grantedAuthorityIterator = (Iterator<GrantedAuthority>) authUser.getAuthorities().iterator();
//
//            while (grantedAuthorityIterator.hasNext()) {
//                if (grantedAuthorityIterator.next().getAuthority().equalsIgnoreCase("ROLE_ADMIN")) {
//                    isAdmin = true;
//                }
//            }
//            if (isAdmin) {
//                response.sendRedirect("/admin/admin-Profile");
//            } else {
//                response.sendRedirect("/user/userProfile");
//            }
        }
    }
}

