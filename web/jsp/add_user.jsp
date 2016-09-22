<!DOCTYPE html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<link rel="stylesheet" type="text/css" href="css/add_user.css" />
<!--<script type="text/javascript" src ="../js/add_user.js"></script>-->

<title>Register</title>
    
</head>
<body>

 <h1>Register Your Library Account</h1>
 
 <p>${warning}</p><p>${warn}</p>
 <form name="registerInfo" action="SimpleLibraryController?action=register" method="post">
  <ul>
        <li class="first">
            <h3>Given Name</h3>
            <p><input type="text" id="gn" name="gname" /></p>
        </li>
        <li>
            <h3>SurName</h3>
            <p><input type="text" id="sn" name="sname"  /></p>
        </li>
        <li>
            <h3>Student Number</h3>
            <p><input type="text" id="snum" name="snumber"  /></p>
        </li>
        <li>
            <h3>Password</h3>
            <p><input type="password" id="pw" name="pwd" /></p>
        </li>
        <li class="last">
            <br />
            <input type="submit" value="Submit" id="sm" name="submit" />
            <!--<input type="button" value="Back" name="Back" href="../index.jsp"/>-->
            <a id="back" href="/SimpleLibrary/index.jsp">Back</a>       
        </li>
    </ul>
   </form>  
  
</body>
</html>