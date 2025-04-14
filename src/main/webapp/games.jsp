<%@ page import="com.joshlukin.nhldailyschedule.Main" %>
<%@ page import="com.joshlukin.nhldailyschedule.Game" %>
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

    .games-table {
      width: 90%;
      margin: 20px auto;
      border-collapse: collapse;
      border-spacing: 0;
    }

    .games-table th {
      background-color: var(--header-bg);
      color: var(--bg-color);
      padding: 10px;
      font-weight: bold;
      text-align: center;
      border-bottom: 2px solid var(--highlight-text-color);
    }

    .games-table td {
      padding: 12px 10px;
      text-align: center;
      border-bottom: 1px solid rgba(131, 135, 153, 0.3);
    }

    .games-table tr:hover {
      background-color: rgba(131, 135, 153, 0.1);
    }

    .division-rivalry {
      background-color: rgba(133, 91, 107, 0.4);
    }

    .division-rivalry:hover {
      background-color: rgba(133, 91, 107, 0.6) !important;
    }

    .game-info-link {
      display: inline-block;
      padding: 5px 10px;
      background-color: var(--header-bg);
      color: var(--text-color);
      text-decoration: none;
      border-radius: 4px;
      font-size: 14px;
      transition: background-color 0.2s;
    }

    .game-info-link:hover {
      background-color: var(--highlight-text-color);
    }

    .legend {
      width: 90%;
      margin: 30px auto 10px;
      text-align: left;
      padding: 15px;
      background-color: rgba(131, 135, 153, 0.1);
      border-radius: 8px;
      display: flex;
      align-items: center;
    }

    .legend-color {
      display: inline-block;
      width: 20px;
      height: 20px;
      margin-right: 10px;
      background-color: rgba(133, 91, 107, 0.4);
      border-radius: 4px;
    }

    .network-list {
      display: block;
      font-size: 12px;
      color: rgba(234, 233, 227, 0.8);
      margin-top: 3px;
      text-align: center;
      max-width: 150px;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
      margin-left: auto;
      margin-right: auto;
    }

    .network-list:hover {
      overflow: visible;
      white-space: normal;
    }

    .vs-text {
      color: var(--highlight-text-color);
      font-weight: bold;
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

<!-- Structured Table for Games -->
<table class="games-table">
  <thead>
  <tr>
    <th>Time (ET)</th>
    <th>Away Team</th>
    <th>Score</th>
    <th>Home Team</th>
    <th>Networks (hover to see more)</th>
    <th>Details</th>
  </tr>
  </thead>
  <tbody>
  <%
    try {
      // Execute getGamesForDate to load games
      Main.getGamesForDate(selectedDate);

      // Access the games from tdGameArr
      Game[] games = Main.tdGameArr;

      if (games == null || games.length == 0) {
  %>
  <tr>
    <td colspan="6">No games scheduled for this date.</td>
  </tr>
  <%
  } else {
    for (Game game : games) {
      String rowClass = game.isDivisionRivalry() ? "division-rivalry" : "";
  %>
  <tr class="<%= rowClass %>">
    <td><%= game.getTime() %></td>
    <td>
      <%= game.getAwayTeamImageHTML() %>
      <%= game.getAwayTeam().getName() %>
    </td>
    <td><%= game.getScoreHTML() %></td>
    <td>
      <%= game.getHomeTeamImageHTML() %>
      <%= game.getHomeTeam().getName() %>
    </td>
    <td>
      <span class="network-list"><%= game.getNetworksHTML() %></span>
    </td>
    <td><%= game.getGameInfoLinkHTML() %></td>
  </tr>
  <%
      }
    }
  } catch (Exception e) {
  %>
  <tr>
    <td colspan="6">Error loading games for this date: <%= e.getMessage() %></td>
  </tr>
  <%
    }
  %>
  </tbody>
</table>

<!-- Division Rivalry Legend -->
<div class="legend">
  <span class="legend-color"></span>
  <span>- Division Rivalry Games</span>
</div>

<footer style="background-color: var(--bg-color); color: var(--header-bg); text-align: center; padding: 10px; font-size: 14px; margin-top: 20px;">
  <p>NHL logos and team names are trademarks of the National Hockey League and their respective teams.</p>
  <p>This project is not affiliated with or endorsed by the NHL.</p>
</footer>

</body>
</html>