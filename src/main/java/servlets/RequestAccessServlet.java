package servlets;

import utils.DatabaseConnection;
import utils.ServletHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.logging.Logger;

@WebServlet(name = "RequestAccessServlet", urlPatterns = "/request-access")
public class RequestAccessServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(RequestAccessServlet.class.getName());

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        fetchAllSoftwareWithStatus(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        requestAccessOfSoftware(request, response);
    }

    private void fetchAllSoftwareWithStatus(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            if(ServletHelper.checkUserAuthorization(request, Arrays.asList("admin", "employee"))) {
                final String loadSoftwareList = "SELECT S.id, S.name, S.description, S.access_level, R.status, R.user_id " +
                        "FROM _software S " +
                        "LEFT JOIN _requests R ON R.software_id = S.id AND R.user_id = ?";

                Long userId = (Long) request.getSession().getAttribute("id");
                connection = DatabaseConnection.getConnection();
                statement = connection.prepareStatement(loadSoftwareList);
                statement.setLong(1, userId);
                ResultSet resultSet = statement.executeQuery();

                List<Map<String, Object>> softwareList = new ArrayList<>();
                while (resultSet.next()) {
                    Map<String, Object> software = new HashMap<>();
                    software.put("Id", resultSet.getLong("id"));
                    software.put("Name", resultSet.getString("name"));
                    software.put("Description", resultSet.getString("description"));
                    software.put("AccessLevel", resultSet.getString("access_level"));
                    software.put("Status", resultSet.getString("status"));
                    softwareList.add(software);
                }
                request.setAttribute("softwareListWithStatus", softwareList);
                request.getRequestDispatcher("/jsp/requestAccess.jsp").forward(request, response);
            }
            else {
                response.setContentType("text/html");
                response.getWriter().write("<script>alert('Access Denied@59RS'); window.location.href='"+
                        request.getContextPath() + "/jsp/login.jsp';</script>");
            }
        } catch (SQLException e) {
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
    }

    private void requestAccessOfSoftware(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            final long userId = (Long) request.getSession().getAttribute("id");
            final long softwareId = Long.parseLong(request.getParameter("softwareId"));
            final String softwareStatus =  request.getParameter("accessLevel");
            final String reason = request.getParameter("reason");

            final String createSoftwareRequestSql = "INSERT INTO _requests(user_id, software_id, access_type, reason)" +
                    "VALUES (?, ?, ?, ?)";
            connection = DatabaseConnection.getConnection();
            statement = connection.prepareStatement(createSoftwareRequestSql);

            statement.setLong(1, userId);
            statement.setLong(2, softwareId);
            statement.setString(3, softwareStatus);
            statement.setString(4, reason);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                response.sendRedirect(request.getContextPath() + "/request-access");
            } else {
                response.getWriter().write("<script>Failed to submit access request.</script>");
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
