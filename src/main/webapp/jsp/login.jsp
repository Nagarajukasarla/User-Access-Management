<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="../css/loginPage.css">
</head>
<body>
<div class="signin-container">
    <h1>Sign in</h1>
    <p class="subtitle">Stay updated on your system</p>

    <form class="signin-form" action="<%=request.getContextPath()%>/login" method="post">
        <input type="text" placeholder="Username" name="username" required>
        <div class="password-field">
            <input type="password" id="password" placeholder="Password" name="password" required>
            <img src="../img/hide-eye.png" id="hideEye" class="eye-icon" width="24" height="24" alt="hide password">
            <img src="../img/show-eye.png" id="showEye" class="eye-icon hide" width="24" height="24" alt="show password">
        </div>
        <button type="submit" class="signin-btn">Sign in</button>
    </form>
<p>${pageContext.request.contextPath}</p>
    <div class="divider">
        <span>or</span>
    </div>

    <p class="create-account">
        New to UAM? <a href="${pageContext.request.contextPath}/jsp/signup.jsp">Create an Account</a>
    </p>
</div>
</body>
<script src="../js/passwordField.js"></script>
</html>
