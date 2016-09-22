<%@page import="Model.TitleInfo"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html"; charset="utf-8" />
<title>Inventory Management</title>
<link rel="stylesheet" href="css/booklist.css" />
</head>
<body>
    <h1 align="center">Inventory Management</h1>
    <div class="ShoppingList">
        <h4>${flag}</h4><br />
        <h4>${rtw}</h4>
        <a href="SimpleLibraryController?action=addtitlepage">Add Title</a>
    <table cellpadding="0" cellspacing="0" border="0" id="table" class="sortable">
        <thead>
            <tr>
                <th width="120"><h3>Title</h3></th>
                <th width="120"><h3>Author</h3></th>
                <th width="80"><h3>ISBN</h3></th>
                <th width="100"><h3>Number of Item</h3></th>
                <th width="100"><h3>Add One Item</h3></th>
                <th width="100"><h3>Remove One Item</h3></th>
                <th width="100"><h3>Remove Title</h3></th>
               
            </tr>
        </thead>
        <tbody>
            <%!
             int[] one;
             int i;
             TitleInfo[] three;
            %>
            <%
             one = (int[])request.getAttribute("last");
             i = Integer.parseInt(request.getAttribute("time").toString());
             three = (TitleInfo[])request.getAttribute("three");
             for(int count = 0; count < i; count ++){
               
            %>      
               
            <tr>
                <td><%=three[count].getTitleName()%></td>
                <td><%=three[count].getAuthor()%></td>
                <td><%=three[count].getIsbn()%></td>
                <td><%=one[count]%></td>
                
                <td><form action="SimpleLibraryController?action=additem" method="POST">
                    <input type="hidden" name="isncode" value="<%=three[count].getIsbn()%>"/><input id="add_item_<%=three[count].getIsbn()%>" type="submit" value="+" width="150"/>
                 </form>
                </td>
                
                
                <td><form action="SimpleLibraryController?action=removeitem" method="POST">
                    <input type="hidden" name="isncode" value="<%=three[count].getIsbn()%>"/><input id="remove_item_<%=three[count].getIsbn()%>" type="submit" value="-" width="150"/> 
                 </form>
                </td>
                 
                <td><form action="SimpleLibraryController?action=removetitle" method="POST">
                <input type="hidden" name="isncode" value="<%=three[count].getIsbn()%>"/><input id="remove_title_<%=three[count].getIsbn()%>" type="submit" value="Remove" style="width:80px;text-align:center;"/>
                </form>
                </td>
            
            </tr>
            </form>
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

  <br /><br />
  
</body>
</html>