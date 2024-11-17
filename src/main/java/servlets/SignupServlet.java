package servlets;

import utils.DatabaseConnection;
import utils.PasswordUtil;
import utils.ServletHelper;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;
@WebServlet(name = "SignupServlet", urlPatterns = "/signup")
public class SignupServlet extends HttpServlet {

    Logger logger = Logger.getLogger(SignupServlet.class.getName());

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
        createUser(request, response);
    }

    private void createUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            final String username = request.getParameter("username");
            final String password = request.getParameter("password");

            if (username == null || password == null) {
                response.setContentType("text/html");
                response.getWriter().write("<script>alert('All fields are required!'); window.location.href='" +
                        request.getContextPath() + "/jsp/signup.jsp';</script>");
                return;
            }

            String hashedPassword = PasswordUtil.hashPassword(password);
            connection = DatabaseConnection.getConnection();
            String sql = "INSERT INTO _user(username, password, role) VALUES(?, ?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, hashedPassword);
            statement.setString(3, "employee");

            statement.executeUpdate();

            response.setContentType("text/html");
            response.getWriter().write("<script>alert('Account created successfully!'); window.location.href='" +
                    request.getContextPath() + "/jsp/login.jsp';</script>");
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
