<%@page import="java.sql.ResultSet"%>
<%@page import="Model.UserInfo"%>
<%@page import="Model.TitleInfo"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html"; charset="utf-8" />
<title>Monitor System</title>
<link rel="stylesheet" href="css/booklist.css" />
</head>
<body>
    <h1 align="center">Monitor System</h1>
    <h4>${info}</h4>
    <div class="ShoppingList">
    <table cellpadding="0" cellspacing="0" border="0" id="table" class="sortable">
        <thead>
            <tr>
                <th width="100"><h3>LogID</h3></th>
                <th width="100"><h3>Event Type</h3></th>
                <th width="400"><h3>Message</h3></th>
                <th width="200"><h3>Event Time</h3></th>
            </tr>
        </thead>
        <tbody>                         
            <%
              ResultSet rs = (ResultSet)request.getAttribute("logrs");
              while(rs.next()){
            %>
            <tr>
                <td><%=rs.getInt("LogID")%></td>
                <td><%=rs.getString("EventType")%></td>
                <td><%=rs.getString("Message")%></td>
                <td><%=rs.getTime("EventTime")%></td>                    
            </tr>           
            <%}
            %>

        </tbody>
    </table>

    <div class="controls">
      <div class="perpage"><span></span>
        <select onchange="sorter.size(this.value)">
          <option value="5">5</option>
          <option value="10" selected="selected">10</option>
          <option value="20">20</option>
          <option value="50">50</option>
          <option value="100">100</option>
        </select>		
      </div>
        <div class="navigation">
            <img src="img/first.gif" width="16" height="16" alt="First Page" onclick="sorter.move(-1,true)" />
            <img src="img/previous.gif" width="16" height="16" alt="First Page" onclick="sorter.move(-1)" />
            <img src="img/next.gif" width="16" height="16" alt="First Page" onclick="sorter.move(1)" />
            <img src="img/last.gif" width="16" height="16" alt="Last Page" onclick="sorter.move(1,true)" />
      </div>
        <div class="page">Page: <span id="currentpage"></span> / <span id="pagelimit"></span></div>
    </div>
        <br /><br /><br />
        <a href="SimpleLibraryController?action=backadmin">Back</a>&nbsp;&nbsp;&nbsp;
        <a href="SimpleLibraryController?action=export">Export</a>
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
	sorter.init("table",1);
</script>
  
</body>
</html>