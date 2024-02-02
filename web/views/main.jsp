<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form method="post">
    <label>Phrase:
        <input type="text" name="name"><br />
    </label>
    <button type="submit">Submit</button>
</form>
<div>
        <%
                int counter = 1;
                while(true) {
                    if (request.getAttribute("product" + counter) != null) {
                        out.println("<p>" + request.getAttribute("product" + counter) + "</p>");
                        counter++;
                    }
                    else {
                        break;
                    }
                }

        %>
</div>
</body>
</html>
