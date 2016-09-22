<%-- 
    Document   : borrowedBooks
    Created on : Jan 16, 2014, 12:48:41 PM
    Author     : yeshang
--%>

<%@page import="java.sql.Time"%>
<%@page import="java.util.TreeMap"%>
<%@page import="Model.LoanInfo"%>
<%@page import="Model.TitleInfo"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Your borrowed books</title>
        <link rel="stylesheet" href="css/booklist.css" />
    </head>
    <body>
            <h1><% Date dNow = new Date();%></h1>
 
        
         <div class="ShoppingList">
            <h1>Your borrowed books</h1>
            <table cellpadding="0" cellspacing="0" border="0" id="table" class="sortable">
                <thead>
                <tr>
                <th width="350"><h3>Title</h3></th>
                <th width="100"><h3>Author</h3></th>
                <th width="100"><h3>ISBN</h3></th>
                <th width="120"><h3>Start Time</h3></th>
                <th width="120"><h3>Due Time</h3></th>
                <th width="250"><h3>Renew</h3></th>
                <th width="120"><h3>Return</h3></th>
                </tr>
                </thead>
                <tbody>
                    <% TreeMap<LoanInfo, TitleInfo> borrowedBooks = (TreeMap<LoanInfo, TitleInfo>) request.getAttribute("borrowedBooks");
                        for (LoanInfo loan : borrowedBooks.keySet()) {
                            TitleInfo title = borrowedBooks.get(loan);%>
                    <tr <%
                        Date due = new Date(dNow.getYear(),
                                dNow.getMonth(),
                                dNow.getDate(),
                                loan.getDueTime().getHours(),
                                loan.getDueTime().getMinutes(),
                                loan.getDueTime().getSeconds());
                        if (due.getTime() < dNow.getTime()) {
                            out.print("class=\"expired\"");
                        }%>>
                        <td><%=title.getTitleName()%></td>
                        <td><%=title.getAuthor()%></td> 
                        <td align="center"><%=title.getIsbn()%></td>
                        <td><%=loan.getStartTime()%></td>
                        <!--<td><%=loan.getDueTime()%></td>-->
                        <td id="duetime_loadId_<%=loan.getLoanID()%>"><%=loan.getDueTime()%></td>
                        <td id="renewTD_loadId_<%=loan.getLoanID()%>">
                            <% if (due.getTime() < dNow.getTime()) {
                                long payment = (dNow.getTime() - due.getTime())/30000;
                                String latepay = String.valueOf(payment);
                                out.print("This loan has expired, you owed us "
                                    + latepay + " dollars, please return book immediately, also note that "
                                        + "you can't borrow any book if you don't pay your late "
                                        + "fees under your Late Payment TAB");
       
                                } else if (loan.getRenewTimes() == 3&&(due.getTime() > dNow.getTime())) {
                                    String latepay = String.valueOf(0);
                                    out.print("Cannot renew more than 3 times");
                                } else {%>
                            <a class="renew_loan" id="renew_loanId_<%=loan.getLoanID()%>" href="?action=renew&loanID=<%=loan.getLoanID()%>">Renew</a>
                            <%
                            String latepay = String.valueOf(0);        
                            out.print(loan.getRenewTimes() + "/3");
                            }%>
                        </td>
                       
                         <td><a class="return_loan" id="return_loanId_<%=loan.getLoanID()%>" href="?action=return&loanID=<%=loan.getLoanID()%>&pay=<%=loan.getLatePayment()%>">Return</a></td>
                    </tr>
                    <%}
                    %>
                </tbody>
            </table>

            <div class="controls">
                <div class="perpage"><span>Page Size: </span>
                    <select onchange="sorter.size(this.value)">
                        <option value="5">5</option>
                        <option value="10" selected="selected">10</option>
                        <option value="20">20</option>
                        <option value="50">50</option>
                        <option value="100">100</option>
                    </select>		
                </div>
                <div class="navigation">
                    <img src="img/first.gif" width="16" height="16" alt="First Page" onclick="sorter.move(-1, true)" />
                    <img src="img/previous.gif" width="16" height="16" alt="First Page" onclick="sorter.move(-1)" />
                    <img src="img/next.gif" width="16" height="16" alt="First Page" onclick="sorter.move(1)" />
                    <img src="img/last.gif" width="16" height="16" alt="Last Page" onclick="sorter.move(1, true)" />
                </div>
                <div class="page">Page: <span id="currentpage"></span> / <span id="pagelimit"></span></div>
            </div>
                <br /><br />
                <a href="SimpleLibraryController?action=back">Back</a>
        </div>
        <script type="text/javascript" src="js/booklist.js"></script>
        <script type="text/javascript">
                        var sorter = new TINY.table.sorter("sorter");
                        sorter.head = "head";
                        sorter.asc = "asc";
                        sorter.desc = "desc";
                        sorter.even = "evenrow";
                        sorter.odd = "oddrow";
                        sorter.evensel = "evenselected";
                        sorter.oddsel = "oddselected";
                        sorter.paginate = true;
                        sorter.currentid = "currentpage";
                        sorter.limitid = "pagelimit";
                        //sorter.init("table", 1);
        </script>
    </body>
</html>
