<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%-- 
    Document   : DisplayTest2
    Created on : Jan 21, 2014, 4:53:37 PM
    Author     : kezhang
--%>

<%@page import="java.sql.ResultSet"%>
<%@page import="DB.DAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Late Payment</title>
        <link rel="stylesheet" href="css/booklist.css" />
    </head>
    <body>
        <h4> 
        <%
          String name = request.getAttribute("checkname").toString();
          String id = request.getAttribute("idd").toString();
          String queryPlaceholder = "SELECT * FROM loan WHERE UserID = %s AND LatePayment > 0";
          String query = String.format(queryPlaceholder, id);          
        %>
        Hi <%=name%>, if there is any record here, you should pay the late 
        fees before you borrow any other books:
       </h4>
      
       <table border="1">
           <thead>
               <tr>
                   <th>LoanID</th>
                   <th>ItemID</th>
                   <th>UserID</th>
                   <th>LatePayment</th>
                   <th>StartTime</th>
                   <th>ReturnTime</th>
                   <th>DueTime</th>
                   <th>RenewTimes</th>
                   <th></th>
               </tr>
           </thead>
           <tbody>
               <%
                 DAO dao = DAO.getInstance();
                 ResultSet rs = dao.getQueryResult(query);
                 while(rs.next()){
               %>
               <form action="SimpleLibraryController?action=makepay" method="POST">
               <tr>
                   <td><%=rs.getInt("LoanID")%><input type="hidden" name="id" value="<%=rs.getInt("LoanID")%>"/></td>
                   <td><%=rs.getInt("ItemID")%></td>
                   <td><%=rs.getInt("UserID")%></td>
                   <td><%=rs.getFloat("LatePayment")%></td>
                   <td><%=rs.getTime("StartTime")%></td>
                   <td><%=rs.getTime("ReturnTime")%></td>
                   <td><%=rs.getTime("DueTime")%></td>
                   <td><%=rs.getInt("RenewTimes")%></td>
                   <td><input id="makepay_loanId_<%=rs.getInt("LoanID")%>" type="submit" value="Make Payment"/></td>
               </tr>
               </form>
               <% 
                }
               %>
           </tbody>         
       </table>
     </form>
           <br /><br />
           <a href="SimpleLibraryController?action=back">Back</a>
    </body>
</html>
