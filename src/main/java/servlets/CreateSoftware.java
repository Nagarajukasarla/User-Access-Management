package servlets;

import utils.DatabaseConnection;
import utils.ServletHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

@WebServlet(name = "CreateSoftware", urlPatterns = "/create-software")
public class CreateSoftware extends HttpServlet {

    Logger logger = Logger.getLogger(CreateSoftware.class.getName());

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        fetchAllSoftware(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        createSoftware(request, response);
    }

    private void createSoftware(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            final String softwareName = request.getParameter("softwareName");
            final String description = request.getParameter("description");
            final String accessLevel = request.getParameter("accessLevel");

            if (softwareName == null || accessLevel == null) {
                response.setContentType("text/html");
                response.getWriter().write("<script>alert('All fields are required');</script>");
                return;
            }

            final String sql = "INSERT INTO _software(name, description, access_level) VALUES(?, ?, ?)";

            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, softwareName);
            statement.setString(2, description);
            statement.setString(3, accessLevel.toLowerCase());
            statement.execute();

            response.setContentType("text/html");
            response.getWriter().write("<script>alert('Software created successfully');</script>");
            response.sendRedirect(request.getContextPath() + "/create-software");

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

    private void fetchAllSoftware(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        if (ServletHelper.checkUserAuthorization(request, Collections.singletonList("admin"))) {
            Connection connection = null;
            PreparedStatement statement = null;

            try {
                final String sql = "SELECT name, description, access_level FROM _software ORDER BY id DESC;";
                connection = DatabaseConnection.getConnection();
                statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();

                List<Map<String, String>> softwareList = new ArrayList<>();
                while (resultSet.next()) {
                    Map<String, String> software = new HashMap<>();
                    software.put("name", resultSet.getString("name"));
                    software.put("description", resultSet.getString("description"));
                    software.put("accessLevel", resultSet.getString("access_level"));
                    softwareList.add(software);
                }

                request.setAttribute("softwareList", softwareList);
                request.getRequestDispatcher("/jsp/createSoftware.jsp").forward(request, response);
            }
            catch (SQLException e) {
                logger.info("An error occurred while executing sql query: " + e);
                ServletHelper.showErrorDialogue(response);
            } catch (ServletException e) {
                logger.info("An error occurred while dispatching servlet: " + e);
                ServletHelper.showErrorDialogue(response);
            } catch (IOException e) {
                logger.info("Unexpected error occurred: " + e);
                ServletHelper.showErrorDialogue(response);
            }
            finally {
                DatabaseConnection.closeConnection(statement, connection);
            }
        } else {
            response.setContentType("text/html");
            response.getWriter().write("<script>alert('Access Denied@115CS'); window.location.href='" +
                    request.getContextPath() + "/jsp/login.jsp';</script>");
        }
    }
}
