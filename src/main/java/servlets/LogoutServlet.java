package servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.logging.Logger;

@WebServlet(name = "LogoutServlet", urlPatterns = "/logout")
public class LogoutServlet extends HttpServlet {

    Logger logger = Logger.getLogger(LogoutServlet.class.getName());

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logout(request, response);
    }

    private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String username = (String) request.getSession().getAttribute("username");
        logger.warning("Username: " + username + "@22");
        Collections.list(request.getSession().getAttributeNames())
                .forEach(attributeName -> {
                    Object attributeValue = request.getSession().getAttribute(attributeName);
                    logger.warning(attributeName + ": " + attributeValue);
                });
        request.getSession().invalidate();
        response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
    }
}
