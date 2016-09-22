<head>
    <%@page contentType="text/html" pageEncoding="UTF-8" %>
    <link rel="stylesheet" href="../css/addtitle.css" type="text/css">
    <title>Add Title</title>
</head>

<body>
    <div>
    <form id="add_title" action="../SimpleLibraryController?action=addtitle" method="post">
    <fieldset id="add_title">
        <legend align="center">Add Title</legend>
        <ol>
            <li>
               <label for="title">Title:</label>
                <input type="text" id="title" name="title">
            </li>
            <li>
                <label for="author">Author:</label>
                <input type="text" id="author" name="author">
            </li>
            <li>
                <label for="isbn">ISBN:</label>
                <input type="text" id="isbn" name="isbn">
            </li>
          
        </ol>
    </fieldset>
         <p color="red">${warn}</p>
        <br /><br />
        <input type="submit" value="Add">&nbsp;&nbsp;<a href="../SimpleLibraryController?action=inventory">Back</a>
    </form>
    </div>
</body>