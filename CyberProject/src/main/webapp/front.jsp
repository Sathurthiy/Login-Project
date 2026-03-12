<%
String error = request.getParameter("error"); /*reads value from login servlet  */
String locked = request.getParameter("locked");
String left = request.getParameter("left");

boolean isLocked = "1".equals(locked); /*check account lock,true disable login form  */
%>
<!DOCTYPE html>
<html>
<head>
<title>Secure Login</title>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"> <!--font awesome icon library  -->
<style>
body{
    display:flex;
    justify-content:center;/*horizontal center  */
    align-items:center; /* vertical center */
    height:100vh;
    margin:0;

    background-image:url("https://images.unsplash.com/photo-1550751827-4bd374c3f58b");
    background-size:cover;
    background-position:center;
    background-repeat:no-repeat;

    font-family:Arial, sans-serif;
}

.login-box{ /*creates white login card  */
    background:rgba(255,255,255,0.9);
    padding:40px;
    border-radius:10px;
    box-shadow:0px 0px 20px black;
    width:450px;
    text-align:center;
}

.title{
    font-size:28px;
    margin-bottom:20px;
}

.input-group{ /*each input is wrapped in it  */
    position:relative;
    margin:30px 0;
}

.input-group input{
    width:100%;
    padding:10px 10px 10px 40px;
    font-size:16px;
}

.icon{
    position:absolute;
    left:10px;
    top:50%;
    transform:translateY(-50%);
    color:gray;
}

input[type="submit"]{ /*creates green login btn  */
    width:100%;
    padding:10px;
    font-size:18px;
    background-color:#4CAF50;
    color:white;
    border:none;
    cursor:pointer;
}

input[type="submit"]:hover{
    background-color:#45a049;
}

.error{ /* dsiplay error in red */
    color:red;
    margin-top:10px;
}

*{
    box-sizing:border-box;
}

.logo{
    width:100px;
    display:block;
    margin:auto;
    margin-bottom:20px;
}

.eye-icon{
position:absolute;
right:10px;
top:50%;
transform:translateY(-50%);
cursor:pointer;
color:gray;
}
</style>
</head>

<body>

<form action="LoginServlet" method="post" class="login-box"> <!--form submit to login servlet using post  -->

<img src="Images/logo.png" class="logo" alt="Logo">

<div class="input-group">
<i class="fa-solid fa-user icon"></i>

<input type="text" name="username" placeholder="Username" required <%= isLocked ? "disabled" : "" %>> <!--account locked user cannot type  -->
</div>

<div class="input-group">
<i class="fa-solid fa-lock icon"></i>
<input type="password" id="password" name="password" placeholder="Password" required <%= isLocked ? "disabled" : "" %>>
<i class="fa-solid fa-eye eye-icon" onclick="togglePassword()"></i>
</div>

<% 

if("1".equals(error)){
%>
<p class="error">Invalid username or Password</p>
<p class="error">Attempts left: <%= left %></p>
<%
}

if("1".equals(locked)){
%>
<p class="error">Account locked due to 3 failed attempts</p>
<%
}
%>


<% if(!isLocked){ %> <!--hide login button when locked  -->
<input type="submit" value="Login">
<% } %>
<script>
function togglePassword() { /*shows or hides password  */
    var password = document.getElementById("password"); /* get password */
    var eye = document.querySelector(".eye-icon"); /*get eye icon  */

    if (password.type === "password") { /* if password hidden*/
        password.type = "text"; /*password visible  */
        eye.classList.remove("fa-eye"); /*  change icon*/
        eye.classList.add("fa-eye-slash");
    } else {
        password.type = "password"; /*if visible hide again  */
        eye.classList.remove("fa-eye-slash");
        eye.classList.add("fa-eye");
    }
}
</script>

</form>

</body>
</html>