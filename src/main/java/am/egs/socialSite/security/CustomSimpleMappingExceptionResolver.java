//package am.egs.socialSite.security;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.lang.Nullable;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//@Configuration
//public class CustomSimpleMappingExceptionResolver extends SimpleMappingExceptionResolver {
//
//    private static final String ERROR_PAGE = "error-423";
//
//    @Override
//    @Nullable
//    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, @Nullable Object handler, Exception ex) {
//        ModelAndView mav = new ModelAndView(ERROR_PAGE);
//        return mav;
//    }
//}
