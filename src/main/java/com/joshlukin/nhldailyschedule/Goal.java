package com.joshlukin.nhldailyschedule;


public class Goal {
    private final Team team;
    //private int playerID;
    private final String playerName;
    private final int period;
    private final String time;
    private final String type;
    private final String[] assists;


    Goal(Team team, String playerName, String time, int period, String type, String[] assists) { //TODO add player ID functoinality
        this.team = team;
        //this.playerID = playerID;
        this.playerName = playerName;
        this.time = time;
        this.type = type;
        this.assists = assists;
        this.period = period;
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

    /**
     *
     * @return The period the goal was scored in starting at index 0
     */
    public int getPeriod() {
        return period;
    }
    public String getPeriodAsString() {
        return (period == 3) ? "OT" : (period + 1) + (period == 0 ? "st" : period == 1 ? "nd" : "rd");
    }
}
