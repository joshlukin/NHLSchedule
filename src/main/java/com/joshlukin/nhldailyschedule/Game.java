package com.joshlukin.nhldailyschedule;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

import java.time.format.DateTimeFormatter;
import java.time.LocalTime;

public class Game {
    private Team homeTeam;
    private Team awayTeam;
    private final int ID;
    private String date;
    private List<String> networks;
    private boolean isDivRival;
    private boolean isRivalSpecial;
    private LocalTime time;
    private String imgName;
    private List<Goal> homeGoals;
    private List<Goal> awayGoals;
    private int homeScore;
    private int awayScore;


    Game(int id) throws URISyntaxException, IOException {
        homeGoals = new ArrayList<>();
        awayGoals = new ArrayList<>();
        this.ID = id;
        JsonElement gameDataElement = Main.parseURL("https://api-web.nhle.com/v1/gamecenter/" +ID+ "/landing");
        JsonObject gameDataObject = gameDataElement.getAsJsonObject();

        date = gameDataObject.get("gameDate").getAsString();
        homeTeam = Team.getTeamFromAbbreviation(gameDataObject.get("homeTeam").getAsJsonObject().get("abbrev").getAsString());
        awayTeam = Team.getTeamFromAbbreviation(gameDataObject.get("awayTeam").getAsJsonObject().get("abbrev").getAsString());
        isDivRival = homeTeam.getDivision().equals(awayTeam.getDivision());

        String utcTime = gameDataObject.get("startTimeUTC").getAsString();
        time = LocalTime.parse(OffsetDateTime.parse(utcTime).atZoneSameInstant(ZoneId.of("America/New_York")).format(DateTimeFormatter.ofPattern("h:mm a")), DateTimeFormatter.ofPattern("h:mm a"));

        JsonArray scoringArray;
        try{
            scoringArray = gameDataObject.get("summary").getAsJsonObject().get("scoring").getAsJsonArray();
        }catch(NullPointerException e){
            scoringArray = new JsonArray();
        }


        for(int i = 0; i<scoringArray.size(); i++){ //iterate through periods of game
            JsonObject periodObject = scoringArray.get(i).getAsJsonObject();
            JsonArray goalsArr = periodObject.get("goals").getAsJsonArray();
            for(int j = 0; j<goalsArr.size(); j++){
                JsonObject goalObject = goalsArr.get(j).getAsJsonObject();
                Team team = Team.getTeamFromAbbreviation(goalObject.get("teamAbbrev").getAsJsonObject().get("default").getAsString());
                //int playerID = goalObject.get("playerID").getAsInt();
                String name = goalObject.get("name").getAsJsonObject().get("default").getAsString();
                String time = goalObject.get("timeInPeriod").getAsString();
                String type;
                try{
                    type = goalObject.get("shotType").getAsString();
                }catch(NullPointerException e){
                    type = "No data for type";
                }
                JsonArray assistsArr = goalObject.get("assists").getAsJsonArray();

                String[] assistsNamesArr = new String[assistsArr.size()];
                for(int k = 0; k<assistsArr.size(); k++){
                    JsonObject assistObject = assistsArr.get(k).getAsJsonObject();
                    assistsNamesArr[k] = assistObject.get("name").getAsJsonObject().get("default").getAsString();
                }
                Goal goal = new Goal(team, name, time, type, assistsNamesArr); //TODO Add player ID functionality
                if(goal.getTeam() == homeTeam){
                    homeGoals.add(goal);
                } else if(goal.getTeam() == awayTeam){
                    awayGoals.add(goal);
                }
                else{
                    throw new RuntimeException("Error with goal teams");
                }


            }
        }
        homeScore = homeGoals.size();
        awayScore = awayGoals.size();

    }

    public boolean hasStarted(){
        return this.getDateLD().isBefore(Main.todayLD) || (this.getDateLD().isEqual(Main.todayLD) && this.getTimeLT().isBefore(Main.now));
    }
    public void setNetworks(List<String> networks){
        this.networks = networks;
    }

    public List<String> getNetworks(){
        return networks;
    }

    public List<Goal> getHomeGoals(){
        return this.homeGoals;
    }

    public List<Goal> getAwayGoals(){
        return this.awayGoals;
    }

    public LocalDate getDateLD(){
        return LocalDate.parse(date);
    }
    /**
     *
     * @return the start time in "h:mm AM/PM"
     */
    public String getTime(){
        return time.format(DateTimeFormatter.ofPattern("h:mm a"));
    }
    public LocalTime getTimeLT(){
        return time;
    }

    public Team getHomeTeam(){
        return homeTeam;
    }
    public Team getAwayTeam(){
        return awayTeam;
    }

    public int getID(){
        return ID;
    }

    // Removed old toString() method

    public String getAwayTeamImageHTML() {
        return "<img src=\"images/" + awayTeam.getImg() + "\" width=\"39\" height=\"26\" style=\"vertical-align: middle;\">";
    }

    public String getHomeTeamImageHTML() {
        return "<img src=\"images/" + homeTeam.getImg() + "\" width=\"39\" height=\"26\" style=\"vertical-align: middle;\">";
    }

    public String getGameInfoLinkHTML() {
        return "<a href=\"/GameID?id=" + ID + "\" class=\"game-info-link\">Game Info</a>";
    }

    public String getNetworksHTML() {
        if (networks == null || networks.isEmpty()) {
            return "N/A";
        }
        return String.join(", ", networks);
    }

    public String getScoreHTML() {
        LocalTime currentTime = LocalTime.now();
        if (!this.hasStarted()) {
            return "Has not started";
        }
        return awayScore + " - " + homeScore;
    }

    public boolean isDivisionRivalry() {
        return isDivRival;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }
}