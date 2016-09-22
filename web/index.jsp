<%-- 
    Document   : index
    Created on : Jan 13, 2014, 9:16:00 PM
    Author     : kezhang
--%>
<%@page import="DB.DAO"%>
<%@page import="javax.naming.Context"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.Connection"%>
<%@page import="javax.sql.DataSource"%>
<%@page import="javax.naming.InitialContext"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="css/index.css" />
        <meta http-equiv="Content-T
      ype" content="text/html; charset=UTF-8">
        <title>Welcome Page</title>
    </head>
    <body>
        
        <h2 align="center">Team 7 Simple Library System</h2>
        
    <form id="login" action="SimpleLibraryController?action=login" method="post">
                        
    <h1>Log In</h1>

    <fieldset id="inputs">

        <input id="userid" type="text" name="uid" placeholder="StuID" autofocus required>   

        <input id="password" type="password" name="pwd" placeholder="Password" required>

    </fieldset>

    <fieldset id="actions">

        <input type="submit" id="submit" value="Log in">

        <a id="register" href="SimpleLibraryController?action=signup">Haven't register yet?</a><a href="#">Forgot your password?</a>
        
    </fieldset>
    <p>${warning}</p>
    

</form>

    </body>

</html>
