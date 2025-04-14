<%@ page import="com.joshlukin.nhldailyschedule.Main" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
  <title>NHL Schedule</title>
  <style>
    .back-link {
      margin: 10px 0;
      display: inline-block;
      color: var(--highlight-text-color);
      text-decoration: none;
    }
    .back-link:hover {
      color: var(--header-bg);
      text-decoration: underline;
    }
  </style>
</head>
<body>
<%
  // Get the selected date from request attributes
  String selectedDate = (String)request.getAttribute("selectedDate");
  if (selectedDate == null) {
    selectedDate = Main.getDate(); // Default to today if not set
  }

  // Format date for display (convert from YYYY-MM-DD to a more readable format)
  java.time.LocalDate parsedDate = java.time.LocalDate.parse(selectedDate);
  String formattedDate = parsedDate.format(java.time.format.DateTimeFormatter.ofPattern("MMMM d, yyyy"));
%>
<h1>
  <span style="color: var(--header-bg)">
    <%= "NHL Games for " + formattedDate %>
  </span>
</h1>

<!-- Back to home link -->
<a href="${pageContext.request.contextPath}/" class="back-link">← Back to Home</a>

<!-- Centered Table with Left-Justified Text -->
<table class="centered-table">
  <%
    String[] gameData;
    try {
      gameData = Main.getGamesForDate(selectedDate).split("\n");
    } catch (Exception e) {
      gameData = new String[]{"Error loading games for this date."};
    }

    for (String game : gameData) {
  %>
  <tr>
    <td><%= game %></td>
  </tr>
  <%
    }
  %>
</table>

<footer style="background-color: var(--bg-color); color: var(--header-bg); text-align: center; padding: 10px; font-size: 14px; margin-top: 20px;">
  <p>NHL logos and team names are trademarks of the National Hockey League and their respective teams.</p>
  <p>This project is not affiliated with or endorsed by the NHL.</p>
</footer>

</body>
</html>