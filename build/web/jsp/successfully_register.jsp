<%-- 
    Document   : successfully_register
    Created on : Jan 21, 2014, 10:13:54 AM
    Author     : kezhang
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Congratulations</title>
        <link rel="stylesheet" href="css/booklist.css" />
    </head>
    <body>
        <h1 align="center">Congratulations, <%=request.getAttribute("name")%>, you have successfully registered on our Simple Library
            System!</h1>
        <br /><br />
        <a id="back" href="index.jsp">Back</a>   
    </body>
</html>
