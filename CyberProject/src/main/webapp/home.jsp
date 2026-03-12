<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
    String user = (String) session.getAttribute("user"); //retrieves username stored in session

    if(user == null){ //without logging in user is not allowed,redirect to login
        response.sendRedirect("front.jsp"); //user sent back to login
        return; //stops jsp execution
    }
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Home Page</title>

<!-- ✅ Linking external CSS -->
<link rel="stylesheet" href="home.css">

</head>
<body>


<div class="container">
    <h1>Login Successful 🎉</h1>
    <p>Welcome, <span class="username"><%= user %></span></p>
    

    <a href="LogoutServlet" class="logout-btn">Logout</a>
</div>

</body>
</html>