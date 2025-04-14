<%@ page import="com.joshlukin.nhldailyschedule.Main" %><%--
  Created by IntelliJ IDEA.
  User: jbluk
  Date: 2/24/2025
  Time: 3:13 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
    <title>Game Info</title>
</head>
<body>
<h1>
    <style>
        Info About The Game
    </style>
</h1>
<p>
    <%=
            Main.generateGameHTML(Integer.parseInt(request.getAttribute("id").toString()), "#1B2B3F", "#EAE9E3", 100)

    %>
</p>
</body>
</html>
