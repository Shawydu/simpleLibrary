﻿<!DOCTYPE html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>User Main</title>
	<link rel="stylesheet" type="text/css" href="css/main.css"/>
	
	<script type="text/javascript" src="js/jquery.js"></script>
	<script type="text/javascript" src="js/jquery-color.js"></script>
	<script type="text/javascript" src="js/main.js"></script>
</head>

<body>
	
	<div id="pageWrap">
		<div id="pageBody">
			
			<h1>Welcome to admin portal, ${username}</h1>
			
                        <a class="hoverBtn" href="SimpleLibraryController?action=inventory">Inventory Management</a>
                        <a class="hoverBtn" href="SimpleLibraryController?action=usermanage">User Management</a>
			<a class="hoverBtn" href="SimpleLibraryController?action=monitor">Monitor System</a>
			<a class="hoverBtn" href="SimpleLibraryController?action=logout">Log Out</a>
			
                        <div class="clear"></div>
			
		</div>
	</div>

<div style="text-align:center;clear:both">
    <br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br />
    <p>_________________________________________________________________________________</p>
    <p>If you have any questions or</p>
    <p>problems, please consult the Library info</p>
    <p>service or call 613-890-7699.</p>

</div>
</body>
</html>