package utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class ServletHelper {

    public static boolean checkUserAuthorization(HttpServletRequest request, List<String> roles) {
        HttpSession session = request.getSession();
        if (session == null || session.getAttribute("username") == null || session.getAttribute("role") == null) {
            return false;
        }
        String userRole = (String) session.getAttribute("role");
        return roles.stream().anyMatch(userRole::equalsIgnoreCase);
    }

    public static void showErrorDialogue(HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.getWriter().write("<script>alert('Internal Server Error!');</script>");
    }
}
