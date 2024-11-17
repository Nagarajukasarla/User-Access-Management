<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="../css/header.css">
    <link rel="stylesheet" href="../css/approveRequest.css">
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
    <table class="software-table">
        <thead>
        <tr>
            <th>User</th>
            <th>Software</th>
            <th>Reason</th>
            <th>Access Level</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <%
            List<Map<String, Object>> approvalRequests = (List<Map<String, Object>>) request.getAttribute("ApprovalRequests");
            if (approvalRequests != null && !approvalRequests.isEmpty()) {
                for (Map<String, Object> approvalRequest : approvalRequests) {
        %>
        <tr>
            <td><%= approvalRequest.get("Username") %></td>
            <td><%= approvalRequest.get("SoftwareName") %></td>
            <td><%= approvalRequest.get("Reason") %></td>
            <td><%= approvalRequest.get("AccessLevel") %></td>
            <td>
                <form action="<%= request.getContextPath() %>/approve-request" method="post">
                    <input type="hidden" name="requestId" value="<%= approvalRequest.get("RequestId") %>">
                    <button type="submit" class="approval-btn" name="action" value="approve">Approve</button>
                    <button type="submit" class="reject-btn" name="action" value="reject">Reject</button>
                </form>
            </td>
        </tr>
        <%
            }
        } else {
        %>
        <tr>
            <td colspan="5">No Software available</td>
        </tr>
        <% } %>
        </tbody>
    </table>
</div>
</body>
</html>

