<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Account - UAM</title>
    <link rel="stylesheet" href="../css/signupPage.css">
</head>
<body>
<div class="signup-container">
    <h1>Create Account</h1>
    <form class="signup-form" action="<%=request.getContextPath()%>/signup" method="post">
        <input class="username" type="text" placeholder="Username or Email" name="username" required>
        <p><%=request.getContextPath()%>
        </p>
        <div class="password-field">
            <input type="password" id="password" placeholder="Password" name="password" required>
            <img src="../img/hide-eye.png" id="hideEye" class="eye-icon" width="24" height="24" alt="hide password">
            <img src="../img/show-eye.png" id="showEye" class="eye-icon hide" width="24" height="24"
                 alt="show password">
        </div>
        <button type="submit" class="signup-btn">Sign Up</button>
    </form>

    <p class="login-link">
        Already registered? <a href="login.jsp">Login</a>
    </p>
</div>
<script src="../js/passwordField.js">
</script>
</body>
</html>