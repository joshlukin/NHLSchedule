<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>NHL Schedule Viewer</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
    <style>
        .date-form {
            margin: 20px auto;
            padding: 15px;
            max-width: 400px;
            background-color: var(--bg-color);
            border: 1px solid var(--header-bg);
            border-radius: 10px;
        }
        .date-form input[type="date"] {
            padding: 8px;
            background-color: var(--bg-color);
            color: var(--text-color);
            border: 1px solid var(--header-bg);
            border-radius: 4px;
            margin-right: 10px;
        }
        .date-form input[type="submit"] {
            padding: 8px 16px;
            background-color: var(--header-bg);
            color: var(--text-color);
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        .date-form input[type="submit"]:hover {
            background-color: var(--highlight-text-color);
        }
        .quick-links {
            margin-top: 20px;
            display: flex;
            justify-content: center;
            gap: 10px;
        }
        .quick-links a {
            padding: 8px 16px;
            background-color: var(--bg-color);
            color: var(--text-color);
            text-decoration: none;
            border-radius: 4px;
            border: 1px solid var(--header-bg);
        }
        .quick-links a:hover {
            background-color: var(--highlight-text-color);
            color: var(--text-color);
        }
    </style>
</head>
<body>
<h1 style="color: var(--header-bg);"><%= "NHL Schedule Viewer" %></h1>

<div class="date-form">
    <form action="${pageContext.request.contextPath}/games" method="get">
        <label for="gameDate" style="color: var(--text-color);">Select Date: </label>
        <input type="date" id="gameDate" name="date" value="<%= java.time.LocalDate.now() %>" required>
        <input type="submit" value="View Games">
    </form>
</div>

<div class="quick-links">
    <a href="${pageContext.request.contextPath}/games?date=<%= java.time.LocalDate.now().minusDays(1) %>">Yesterday's Games</a>
    <a href="${pageContext.request.contextPath}/games?date=<%= java.time.LocalDate.now() %>">Today's Games</a>
    <a href="${pageContext.request.contextPath}/games?date=<%= java.time.LocalDate.now().plusDays(1) %>">Tomorrow's Games</a>
</div>

<footer style="background-color: var(--bg-color); color: var(--header-bg); text-align: center; padding: 10px; font-size: 14px; margin-top: 20px;">
    <p>NHL logos and team names are trademarks of the National Hockey League and their respective teams.</p>
    <p>This project is not affiliated with or endorsed by the NHL.</p>
</footer>
</body>
</html>