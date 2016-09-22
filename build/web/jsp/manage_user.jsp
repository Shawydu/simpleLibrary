<%@page import="Model.UserInfo"%>
<%@page import="Model.TitleInfo"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html"; charset="utf-8" />
<title>User Management</title>
<link rel="stylesheet" href="css/booklist.css" />
</head>
<body>
    <h1 align="center">User Management</h1>
    <h4>${ruf}</h4>
    <div class="ShoppingList">
    <table cellpadding="0" cellspacing="0" border="0" id="table" class="sortable">
        <thead>
            <tr>
                <th width="300"><h3>User ID</h3></th>
                <th width="300"><h3>FirstName</h3></th>
                <th width="300"><h3>LastName</h3></th>
                <th width="300"><h3>Current User?</h3></th>
                <th width="300"></th>
            </tr>
        </thead>
        <tbody>               
              <%
                    UserInfo uu[] = (UserInfo[])request.getAttribute("userlist");
                    Boolean f[] = (Boolean[])request.getAttribute("flag");
                    int i = uu.length;
                    for(int count = 0; count < i; count++){                 
              %>
            
            <tr>
                <td><%=uu[count].getUserID()%></td>
                <td><%=uu[count].getFirstName()%></td>
                <td><%=uu[count].getLastName()%></td>
                <td><% if(f[count] == false){out.print("Yes");}
                else{out.print("No");}%>
                </td>
                <td align="center">
                    <form action="SimpleLibraryController?action=removeuser" method="POST">
                        <input type="hidden" name="uid" value="<%=uu[count].getUserID()%>"/>
                        <input id="remove_user_<%=uu[count].getUserID()%>" type="submit" value="Remove" style="width:80px;text-align:center;"/>
                    </form>
                </td>                    
            </tr>           
            </form
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
        <a href="SimpleLibraryController?action=backadmin">Back</a>
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