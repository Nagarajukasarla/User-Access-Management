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

@WebServlet(name = "ApprovalServlet", urlPatterns = "/approve-request")
public class ApprovalServlet extends HttpServlet {

    Logger logger = Logger.getLogger(ApprovalServlet.class.getName());

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        fetchRequests(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        manipulateRequest(request, response);
    }

    private void fetchRequests(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            if (ServletHelper.checkUserAuthorization(request, Arrays.asList("admin", "manager"))) {
                final String sql =
                        "SELECT R.id AS Id, U.username AS Username, S.name AS SoftwareName, R.access_type AS AccessLevel, R.reason AS Reason " +
                                "FROM _requests R " +
                                "JOIN _user U ON U.id = R.user_id " +
                                "JOIN _software S ON S.id = R.software_id " +
                                "WHERE LOWER(R.status) = 'pending';";

                connection = DatabaseConnection.getConnection();
                statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();

                List<Map<String, Object>> approvalRequests = new ArrayList<>();
                while (resultSet.next()) {
                    Map<String, Object> approvalRequest = new HashMap<>();
                    approvalRequest.put("RequestId", resultSet.getLong("Id"));
                    approvalRequest.put("Username", resultSet.getString("Username"));
                    approvalRequest.put("SoftwareName", resultSet.getString("SoftwareName"));
                    approvalRequest.put("AccessLevel", resultSet.getString("AccessLevel"));
                    approvalRequest.put("Reason", resultSet.getString("Reason"));
                    approvalRequests.add(approvalRequest);
                }

                request.setAttribute("ApprovalRequests", approvalRequests);
                request.getRequestDispatcher("/jsp/approveRequest.jsp").forward(request, response);

            } else {
                response.setContentType("text/html");
                response.getWriter().write("<script>alert('Access Denied 65@AS'); window.location.href='" +
                        request.getContextPath() + "/jsp/login.jsp';</script>");
            }

        } catch (SQLException e) {
            logger.warning("An error occurred while executing the SQL query: " + e);
            ServletHelper.showErrorDialogue(response);
        } catch (ServletException e) {
            logger.warning("An error occurred while dispatching servlet: " + e);
            ServletHelper.showErrorDialogue(response);
        } catch (IOException e) {
            logger.warning("Unexpected error occurred: " + e);
            ServletHelper.showErrorDialogue(response);
        } finally {
            DatabaseConnection.closeConnection(statement, connection);
        }
    }

    private void manipulateRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            if (ServletHelper.checkUserAuthorization(request, Arrays.asList("admin", "manager"))) {
                String requestType = request.getParameter("action");
                long requestId = Long.parseLong(request.getParameter("requestId"));

                if (requestType != null) {
                    connection = DatabaseConnection.getConnection();
                    final String sql = "UPDATE _requests SET status = ? WHERE id = ?;";

                    statement = connection.prepareStatement(sql);
                    statement.setString(1, requestType.equals("approve") ? "Approved" : "Rejected");
                    statement.setLong(2, requestId);

                    int rowsAffected = statement.executeUpdate();

                    response.setContentType("text/html");
                    if (rowsAffected > 0) {
                        response.getWriter().write("<script>alert('Changes made.')</script>");
                        response.sendRedirect(request.getContextPath() + "/approve-request");
                    } else {
                        response.getWriter().write("<script>alert('No record updated.')</script>");
                        response.sendRedirect(request.getContextPath() + "/approve-request");
                    }
                } else {
                    response.setContentType("text/html");
                    response.getWriter().write("<script>alert('Invalid request parameters.'); window.location.href='" +
                            request.getContextPath() + "/jsp/requestAccess.jsp';</script>");
                }
            } else {
                response.setContentType("text/html");
                response.getWriter().write("<script>alert('Access Denied @117 AS'); window.location.href='" +
                        request.getContextPath() + "/jsp/login.jsp';</script>");
            }
        } catch (SQLException e) {
            logger.info("An error occurred while executing SQL query: " + e);
            ServletHelper.showErrorDialogue(response);
        } catch (IOException e) {
            logger.info("Unexpected error occurred: " + e);
            ServletHelper.showErrorDialogue(response);
        } finally {
            DatabaseConnection.closeConnection(statement, connection);
        }
    }
}
