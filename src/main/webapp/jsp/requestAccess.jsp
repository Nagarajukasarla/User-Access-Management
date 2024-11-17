<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%
    if (session == null || session.getAttribute("username") == null) {
        response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
        return;
    }

    if (((String)session.getAttribute("role")).equalsIgnoreCase("manager")) {
        response.sendRedirect(request.getContextPath() + "/approve-request");
        return;
    }

%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>PAM360 - Privileged Identity Management</title>
    <link rel="stylesheet" href="../css/requestAccess.css">
    <link rel="stylesheet" href="../css/header.css">
</head>
<body>
<header class="main-header">
    <div class="header-left">
        <h1 class="main-title">U A M</h1>
        <span class="dashboard-text">Dashboard</span>
    </div>
    <div class="header-right">
        <form action="<%= request.getContextPath() %>/logout" method="post">
            <button type="submit" class="logout-btn">Logout</button>
        </form>
    </div>
</header>
<div class="table-container">
    <h2>Latest Software's</h2>
    <table class="software-table">
        <thead>
        <tr>
            <th>Name</th>
            <th>Description</th>
            <th>Access Level</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <%
            List<Map<String, Object>> softwareListWithStatus = (List<Map<String, Object>>) request.getAttribute("softwareListWithStatus");
            if (!softwareListWithStatus.isEmpty()) {
                for (Map<String, Object> software : softwareListWithStatus) {
        %>
        <tr>
            <td><%= software.get("Name")%>
            </td>
            <td><%= software.get("Description")%>
            </td>
            <td><%= software.get("AccessLevel")%>
            </td>
            <td>
                <%
                    String status = (String) software.get("Status");
                    if (status == null) {
                %>
                <form class="request-access-form" action="<%=request.getContextPath()%>/request-access" method="post">
                    <input type="hidden" name="softwareId" value="<%= software.get("Id")%>" />
                    <input type="hidden" name="accessLevel" value="<%= software.get("AccessLevel")%>" />
                    <textarea name="reason" placeholder="Enter reason " required></textarea>
                    <button type="submit" class="request-btn">Request Access</button>
                </form>
                <% } else if (status.equalsIgnoreCase("pending")) { %>
                <span>Pending</span>
                <% } else if (status.equalsIgnoreCase("approved")) { %>
                <span>Approved</span>
                <% } else {%>
                <span>Rejected</span>
                <% } %>
            </td>
        </tr>
        <%
            }
        } else {
        %>
        <tr>
            <td colspan="4">No Software available.</td>
        </tr>
        <%
            }
        %>
        </tbody>
    </table>
</div>
</body>
<script src="../js/requestAccept.js"></script>
</html>