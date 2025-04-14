package com.joshlukin.nhldailyschedule;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.net.HttpURLConnection;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
public class Main {

    public static LocalTime now = LocalTime.now();
    public static LocalDate todayLD = LocalDate.now();
    public static Game[] tdGameArr;
    public static String main() throws URISyntaxException, IOException {
        //<editor-fold desc = "Ping Test">
        boolean isPing = true;
        String pingContent = parseURL("https://api.nhle.com/stats/rest/ping").toString();
        if (!(pingContent.equals("{}"))) {
            throw new RuntimeException("Ping unsucessful, check connection");
        } else {
            System.out.println("Ping Successful");
        }
        //</editor-fold>
        String today = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE);
        JsonObject todayWeek = getSchedule(today).getAsJsonObject();

        int numGames = Integer.parseInt(todayWeek.get("gameWeek").getAsJsonArray().get(0).getAsJsonObject().get("numberOfGames").getAsString());
        System.out.println("Number of games: " + numGames);
        JsonArray todayGames = todayWeek.get("gameWeek").getAsJsonArray().get(0).getAsJsonObject().get("games").getAsJsonArray();
        tdGameArr = new Game[numGames];
        for (int i = 0; i < numGames; i++) {
            Game g = new Game(todayGames.get(i).getAsJsonObject().get("id").getAsInt());
            int numNetworks = todayGames.get(i).getAsJsonObject().get("tvBroadcasts").getAsJsonArray().size();
            List<String> networks = new ArrayList<>();
            for (int j = 0; j < numNetworks; j++) {
                networks.add(todayGames.get(i).getAsJsonObject().get("tvBroadcasts").getAsJsonArray().get(j).getAsJsonObject().get("network").getAsString());
            }
            g.setNetworks(networks);
            tdGameArr[i] = g;
        }
        StringBuilder info = new StringBuilder();
        for (Game g : tdGameArr) {
            info.append(g + "  " + g.getNetworks().toString() + "<br> \n");
        }
        return info.toString();

    }

    public static String generateGameHTML(int id, String bgColor, String textColor, int logoSize) throws URISyntaxException, IOException {
        now = LocalTime.now();

        Game game = new Game(id); // Create Game object, which autopopulates goals
        Team homeTeam = game.getHomeTeam();
        Team awayTeam = game.getAwayTeam();

        StringBuilder sb = new StringBuilder();

        // Apply CSS styles inline (overriding external CSS if needed)
        sb.append("<style>");
        sb.append("body { background-color: " + bgColor + "; color: " + textColor + "; text-align: center; }");
        sb.append(".game-container { display: flex; flex-direction: column; align-items: center; }");
        sb.append(".game-table { border-collapse: collapse; margin: auto; width: 60%;}");
        sb.append(".game-table th, .game-table td { padding: 10px; border: 1px solid " + textColor + "; }");
        sb.append(".team-logo { width: " + logoSize + "px; height: auto; }");
        sb.append(".score-cell { font-size: 24px; font-weight: bold; border-left: none; border-right: none; text-align: center; }"); // Score larger, no vertical borders
        sb.append(".top-row { display: flex; align-items: center; justify-content: center; gap: 10px; }"); // Align logos and scores
        sb.append(".goals-container { display: flex; width: 100%; }");
        sb.append(".team-goals { flex: 1; text-align: left; padding: 10px; }");
        sb.append(".team-column { width: 50%; text-align: left; }");
        sb.append("</style>");

        sb.append("<div class='game-container'>");
        sb.append("<h2>" + homeTeam.getName() + " vs " + awayTeam.getName() + "</h2>");
        sb.append("<p>Time: " + game.getTime() + "</p>");

        sb.append("<table class='game-table'>");

        // Single row for logos and scores
        sb.append("<tr>");
        sb.append("<th colspan='2' class='score-cell'>");
        sb.append("<div class='top-row'>");
        sb.append("<img class='team-logo' src='images/" + homeTeam.getImg() + "' alt='" + homeTeam.getName() + " Logo'>");
        sb.append(game.getHomeScore() + " - " + game.getAwayScore());
        sb.append("<img class='team-logo' src='images/" + awayTeam.getImg() + "' alt='" + awayTeam.getName() + " Logo'>");
        sb.append("</div>");
        sb.append("</th>");
        sb.append("</tr>");

        if(!game.hasStarted()) {
            System.out.println("ALERT ALERT ALERT  ");
            sb.append("<tr>");
            sb.append("<td colspan='2'>Game has not started</td>");
            sb.append("</tr>");
        }
        else if(game.getAwayScore()==0 && game.getHomeScore()==0){
            sb.append("<tr>");
            sb.append("<td colspan='2'>No scoring yet</td>");
            sb.append("</tr>");
        }
        else{
            // Second row for goal information
            sb.append("<tr>");

            // Home Team Goals
            sb.append("<td class='team-column'>");
            if (game.getHomeGoals().isEmpty()) {
                sb.append("No goals yet");
            } else {
                for (Goal goal : game.getHomeGoals()) {
                    sb.append(goal.getTime() + " - " + goal.getPlayerName());
                    if (goal.getAssists() != null && goal.getAssists().length > 0) {
                        sb.append(" (");
                        sb.append(String.join(", ", goal.getAssists()));
                        sb.append(")");
                    }
                    sb.append("<br>");
                }
            }
            sb.append("</td>");

            // Away Team Goals
            sb.append("<td class='team-column'>");
            if (game.getAwayGoals().isEmpty()) {
                sb.append("No goals yet");
            } else {
                for (Goal goal : game.getAwayGoals()) {
                    sb.append(goal.getTime() + " - " + goal.getPlayerName());
                    if (goal.getAssists() != null && goal.getAssists().length > 0) {
                        sb.append(" (");
                        sb.append(String.join(", ", goal.getAssists()));
                        sb.append(")");
                    }
                    sb.append("<br>");
                }
            }
            sb.append("</td>");

            sb.append("</tr>");
        }

        sb.append("</table>");
        sb.append("</div>");

        return sb.toString();
    }

    private static String formatGoalInfo(List<Goal> goals, Team team, int index) {
        List<Goal> teamGoals = goals.stream()
                .filter(g -> g.getTeam() == team)
                .toList();

        if (index >= teamGoals.size()) return "";

        Goal goal = teamGoals.get(index);
        String assistText = (goal.getAssists() != null && goal.getAssists().length > 0)
                ? " (" + String.join(", ", goal.getAssists()) + ")"
                : "";

        return goal.getPlayerName() + assistText + " - " + goal.getType() + " - " + goal.getTime();
    }

    /**
     *
     * @param date in format "YYYY-MM-DD"
     */
    static JsonElement getSchedule(String date) throws URISyntaxException, IOException {
        return parseURL("https://api-web.nhle.com/v1/schedule/"+date);
    }

    public static JsonElement parseURL(String URLName) throws URISyntaxException, IOException {
        URI u = new URI(URLName);
        HttpURLConnection connection = (HttpURLConnection) u.toURL().openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder content = new StringBuilder();
        String line;
        while((line = br.readLine())!=null){
            content.append(line + "\n");
        }
        br.close();
        return JsonParser.parseString(content.toString());
    }

    public static String getDate(){
        return java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE);
    }

    /**
     *
     * @param date in "YYYY-MM-DD" format
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
    public static String getGamesForDate(String date) throws URISyntaxException, IOException {
        JsonObject dateWeek = getSchedule(date).getAsJsonObject();

        // Check if there are games for this date in the response
        if (dateWeek.get("gameWeek").getAsJsonArray().size() == 0) {
            return "No games scheduled for this date.";
        }

        int numGames;
        try {
            numGames = Integer.parseInt(dateWeek.get("gameWeek").getAsJsonArray().get(0).getAsJsonObject().get("numberOfGames").getAsString());
        } catch (Exception e) {
            return "No games scheduled for this date.";
        }

        if (numGames == 0) {
            return "No games scheduled for this date.";
        }

        JsonArray dateGames = dateWeek.get("gameWeek").getAsJsonArray().get(0).getAsJsonObject().get("games").getAsJsonArray();
        tdGameArr = new Game[numGames];

        for (int i = 0; i < numGames; i++) {
            Game g = new Game(dateGames.get(i).getAsJsonObject().get("id").getAsInt());
            int numNetworks = dateGames.get(i).getAsJsonObject().get("tvBroadcasts").getAsJsonArray().size();
            List<String> networks = new ArrayList<>();
            for (int j = 0; j < numNetworks; j++) {
                networks.add(dateGames.get(i).getAsJsonObject().get("tvBroadcasts").getAsJsonArray().get(j).getAsJsonObject().get("network").getAsString());
            }
            g.setNetworks(networks);
            tdGameArr[i] = g;
        }

        StringBuilder info = new StringBuilder();
        for (Game g : tdGameArr) {
            info.append(g + "  " + g.getNetworks().toString() + "<br> \n");
        }
        return info.toString();
    }
}