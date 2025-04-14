package com.joshlukin.nhldailyschedule;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import java.time.OffsetDateTime;
import java.time.ZoneId;
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
    @Override
    public String toString() {
        String htmlAwayImg = "<img src=\"images/" + awayTeam.getImg() + "\" width=\"39\" height=\"26\" style=\"vertical-align: middle;\">";
        String htmlHomeImg = "<img src=\"images/" + homeTeam.getImg() + "\" width=\"39\" height=\"26\" style=\"vertical-align: middle;\">";

        String scoreLink = "<html><head><style>"
                + "a:link { color: #EC7774; }"
                + "a:visited { color: #838799; }"
                + "a:hover { color: #ff3f3b; }"
                + "a:active { color: #ff3f3b; }"
                + "</style></head><body>"
                + "<a href=\"/GameID?id=" + ID + "\">Game Info</a>"
                + "</body></html>";


        String content = this.getTime() + " | " + htmlAwayImg + " " + awayTeam + " @ " + htmlHomeImg + " " + homeTeam + " " + scoreLink;

        return isDivRival
                ? "<span style=\"background-color: #855b6b; padding: 2px 4px; border-radius: 4px;\">" + content + "</span>"
                : content;
    }


    public int getHomeScore() {
        return homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }
}
