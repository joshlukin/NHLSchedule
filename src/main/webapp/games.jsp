<%@ page import="com.joshlukin.nhldailyschedule.Main" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
  <title>NHL Schedule</title>
</head>
<body style="background-color:#1b2b3f;">
<h1>
  <span style="color: #838799">
    <%= "Here are the games on " + Main.getDate() %>
  </span>
</h1>

<!-- Centered Table with Left-Justified Text -->
<table class="centered-table">
  <%
    String[] gameData = Main.main().split("\n"); // Assuming Main.main() returns game data separated by newlines
    for (String game : gameData) {
  %>
  <tr>
    <td><%= game %></td>
  </tr>
  <%
    }
  %>
</table>

<footer style="background-color: #1b2b3f; color: #838799; text-align: center; padding: 10px; font-size: 14px; margin-top: 20px;">
  <p>NHL logos and team names are trademarks of the National Hockey League and their respective teams.</p>
  <p>This project is not affiliated with or endorsed by the NHL.</p>
</footer>

</body>
</html>
