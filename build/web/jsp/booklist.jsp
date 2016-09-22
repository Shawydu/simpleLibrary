<%-- 
    Document   : titleList
    Created on : Jan 15, 2014, 1:45:55 PM
    Author     : yeshang
--%>

<%@page import="Model.TitleInfo"%>
<%@page import="java.util.HashMap"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>List of books</title>
        <link rel="stylesheet" href="css/booklist.css" />
    </head>
    <body>
            <h4>
                <%
                  if(session.getAttribute("borrowFails")!=null){
                     out.print(session.getAttribute("borrowFails").toString());                   
                     session.removeAttribute("borrowFails");
                  }    
                %>
            </h4>
        <div class="ShoppingList">
            <h1 align="center">Books Available</h1>
            <form action="SimpleLibraryController?action=borrow" method=post>
                <table cellpadding="0" cellspacing="0" border="0" id="table" class="sortable">
                    <thead>
                        <tr>
                            <th width="80"><h3></h3></th>
                            <th width="350"><h3>Title</h3></th>
                            <th width="180"><h3>Author</h3></th>
                            <th width="160"><h3>ISBN</h3></th>
                            <th width="120"><h3>Number of Copies</h3></th>
                        </tr>
                    </thead>
                    <tbody>
                        <% HashMap<TitleInfo, Integer> bookList = (HashMap<TitleInfo, Integer>) request.getAttribute("books");
                           for (TitleInfo title : bookList.keySet()){
                        int numOfCopies = bookList.get(title);%>
                        <tr>
                            <td><% if (numOfCopies > 0) {%>
                                <input name="selectedTitles" type="checkbox" value="<%=title.getIsbn()%>|<%=title.getTitleName()%>"></input>
                                <%}%></td>
                            <td><%=title.getTitleName()%></td>
                            <td><%=title.getAuthor()%></td>
                            <td align="center"><%=title.getIsbn()%></td>
                            <td><%=numOfCopies%></td>
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
                <br /><br /><br />
                <input type="submit" value="Confirm Loan"/>&nbsp;&nbsp;&nbsp;<a href="SimpleLibraryController?action=back">Back</a>
            </form>
                   
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
                            sorter.init("table", 1);
        </script>
    </body>
</html>