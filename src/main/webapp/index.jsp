<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>The SQL Gateway</title>
    <link rel="stylesheet" href="styles/main.css" type="text/css"/>
</head>
<body>
    
    <c:if test="${sqlStatement == null}">
        <c:set var="sqlStatement" value="select * from User" />
    </c:if>
       
    <h1>The SQL Gatewayy</h1>
    <p>Enter an SQL statement and click the Execute button.</p>
    
    <p><b>SQL statement:</b></p>
    <form action="sqlGateway" method="post">
        <textarea name="sqlStatement" cols="60" rows="8">${sqlStatement}</textarea>
        <br>
        <input type="submit" value="Execute">
    </form>

    <p><b>SQL result:</b></p>
    ${sqlResult}

</body>
</html>