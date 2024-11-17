<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    if(session == null || session.getAttribute("username") == null) {
        response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
        return;
    }

    if (((String) session.getAttribute("role")).equalsIgnoreCase("manager")) {
        response.sendRedirect(request.getContextPath() + "/pending-requests");
        return;
    }

    if(((String) session.getAttribute("role")).equalsIgnoreCase("employee")) {
        response.sendRedirect(request.getContextPath() + "/request-access");
        return;
    }

%>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="../css/header.css">
    <link rel="stylesheet" href="../css/createSoftware.css">
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
    <div class="table-header">
        <h2>Software's</h2>
        <button class="create-btn">Create New</button>
    </div>
    <div class="create-software hide-create-container">
        <form action="<%=request.getContextPath()%>/create-software" method="post">
            <table>
                <tr>
                    <th>
                        <input type="text" placeholder="Software name" name="softwareName" required>
                    </th>
                    <th>
                        <input type="text" placeholder="Description" name="description">
                    </th>
                    <th>
                        <select class="access-level-select" name="accessLevel">
                            <option value="read">Read</option>
                            <option value="write">Write</option>
                            <option value="admin">Admin</option>
                        </select>
                    </th>
                    <th id="submit-btn-th">
                        <button type="submit" class="save-btn">Save</button>
                    </th>
                    <th id="cancel-btn-th">
                        <button class="cancel-btn">Cancel</button>
                    </th>
                </tr>
            </table>
        </form>
    </div>
    <table class="software-table">
        <thead>
        <tr>
            <th>Name</th>
            <th>Description</th>
            <th>Access Level</th>
        </tr>
        </thead>
        <tbody>
        <%
            List<Map<String, String>> softwareList = (List<Map<String, String>>) request.getAttribute("softwareList");
            if (softwareList != null) {
                for (Map<String, String> software : softwareList) {
        %>
        <tr>
            <td><%= software.get("name")%></td>
            <td><%= software.get("description")%></td>
            <td><%= software.get("accessLevel")%></td>
        </tr>
        <%
                }
            } else {
        %>
        <tr>
            <td colspan="3">No Software available</td>
        </tr>
        <% } %>
        </tbody>
    </table>
</div>
</body>
<script src="../js/createSoftware.js"></script>
</html>
