package com.joshlukin.nhldailyschedule;

import com.google.gson.JsonArray;

public class Goal {
    private Team team;
    //private int playerID;
    private String playerName;
    private String time;
    private String type;
    private String[] assists;


    Goal(Team team, String playerName, String time, String type, String[] assists) { //TODO add player ID functoinality
        this.team = team;
        //this.playerID = playerID;
        this.playerName = playerName;
        this.time = time;
        this.type = type;
        this.assists = assists;
    }

    public Team getTeam() {
        return team;
    }
//    public int getPlayerID() {
//        return playerID;
//    }
    public String getPlayerName() {
        return playerName;
    }
    public String getTime() {
        return time;
    }
    public String getType() {
        return type;
    }
    public String[] getAssists() {
        return assists;
    }
}
