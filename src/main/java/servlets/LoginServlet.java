package servlets;

import utils.DatabaseConnection;
import utils.PasswordUtil;
import utils.ServletHelper;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

@WebServlet(name = "LoginServlet", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
    private final static Logger logger = Logger.getLogger(LoginServlet.class.getName());

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        validateUser(request, response);
    }

    private void validateUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String username = request.getParameter("username");
        final String password = request.getParameter("password");

        if (username == null || password == null) {
            response.setContentType("text/html");
            response.getWriter().write("<script>alert('Username and password required!');</script>");
            return;
        }

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT id, username, password, role FROM _user WHERE username = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String storedHash = resultSet.getString("password");
                String role = resultSet.getString("role");

                if (PasswordUtil.verifyPassword(password, storedHash)) {
                    HttpSession session = request.getSession();
                    session.setAttribute("id", resultSet.getLong("id"));
                    session.setAttribute("username", username);
                    session.setAttribute("role", role);
                    session.setMaxInactiveInterval(2 * 60);

                    if (role.equalsIgnoreCase("manager")) {
                        response.sendRedirect(request.getContextPath() + "/approve-request");
                    } else if (role.equalsIgnoreCase("admin")) {
                        response.sendRedirect(request.getContextPath() + "/create-software");
                    } else {
                        response.sendRedirect(request.getContextPath() + "/request-access");
                    }
                } else {
                    response.setContentType("text/html");
                    response.getWriter().write("<script>alert('Invalid credentials!'); window.location.href='"+
                            request.getContextPath() + "/jsp/login.jsp'</script>");
                }
            } else {
                response.setContentType("text/html");
                response.getWriter().write("<script>alert('User not found!');</script>");
            }
        } catch (SQLException e) {
            logger.info("An error occurred while executing sql query: " + e);
            ServletHelper.showErrorDialogue(response);
        } catch (IOException e) {
            logger.info("Unexpected error occurred: " + e);
            ServletHelper.showErrorDialogue(response);
        }
        finally {
            DatabaseConnection.closeConnection(statement, connection);
        }
    }
}
