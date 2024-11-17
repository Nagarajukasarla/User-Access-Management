package servlets;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {
    private static final Set<String> ALLOWED_PATHS = new HashSet<>(Arrays.asList(
            "/login", "/signup", "/jsp/login.jsp", "/jsp/signup.jsp",
            "/css", "/img", "js", "/"
    ));
    private static final Logger logger = Logger.getLogger(AuthenticationFilter.class.getName());


    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain
    ) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        HttpSession session = httpRequest.getSession(false);

        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
        logger.info("Requested: " + path);
        boolean isAllowed = ALLOWED_PATHS.stream()
                .anyMatch(path::startsWith);

        if(isAllowed || session != null && session.getAttribute("email") != null) {
            filterChain.doFilter(servletRequest, servletResponse);
        }
        else {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/jsp/login.jsp");
        }
    }

}
