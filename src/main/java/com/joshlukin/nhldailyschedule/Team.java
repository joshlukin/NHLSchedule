package com.joshlukin.nhldailyschedule;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public enum Team {
    //<editor-fold desc = "Enumerations for all NHL teams">
    ANAHEIM_DUCKS("ANA", "Pacific"),
    BOSTON_BRUINS("BOS", "Atlantic"),
    BUFFALO_SABRES("BUF", "Atlantic"),
    CAROLINA_HURRICANES("CAR", "Metropolitan"),
    COLUMBUS_BLUE_JACKETS("CBJ", "Metropolitan"),
    CALGARY_FLAMES("CGY", "Pacific"),
    CHICAGO_BLACKHAWKS("CHI", "Central"),
    COLORADO_AVALANCHE("COL", "Central"),
    DALLAS_STARS("DAL", "Central"),
    DETROIT_RED_WINGS("DET", "Atlantic"),
    EDMONTON_OILERS("EDM", "Pacific"),
    FLORIDA_PANTHERS("FLA", "Atlantic"),
    LOS_ANGELES_KINGS("LAK", "Pacific"),
    MINNESOTA_WILD("MIN", "Central"),
    MONTREAL_CANADIENS("MTL", "Atlantic"),
    NEW_JERSEY_DEVILS("NJD", "Metropolitan"),
    NASHVILLE_PREDATORS("NSH", "Central"),
    NEW_YORK_ISLANDERS("NYI", "Metropolitan"),
    NEW_YORK_RANGERS("NYR", "Metropolitan"),
    OTTAWA_SENATORS("OTT", "Atlantic"),
    PHILADELPHIA_FLYERS("PHI", "Metropolitan"),
    PITTSBURGH_PENGUINS("PIT", "Metropolitan"),
    SEATTLE_KRAKEN("SEA", "Pacific"),
    SAN_JOSE_SHARKS("SJS", "Pacific"),
    ST_LOUIS_BLUES("STL", "Central"),
    TAMPA_BAY_LIGHTNING("TBL", "Atlantic"),
    TORONTO_MAPLE_LEAFS("TOR", "Atlantic"),
    UTAH_HOCKEY_CLUB("UTA", "Central"),
    VANCOUVER_CANUCKS("VAN", "Pacific"),
    VEGAS_GOLDEN_KNIGHTS("VGK", "Pacific"),
    WINNIPEG_JETS("WPG", "Central"),
    WASHINGTON_CAPITALS("WSH", "Metropolitan");
    //</editor-fold>
    private final String NAME;
    private final String ABREV;
    private final String DIV;
    // <editor-fold desc="Abbreviation dictionary mapping team codes to full names"
    private final Map<String, String> abrevDictionary = Map.ofEntries(
            Map.entry("ANA", "Anaheim Ducks"),
            Map.entry("BOS", "Boston Bruins"),
            Map.entry("BUF", "Buffalo Sabres"),
            Map.entry("CAR", "Carolina Hurricanes"),
            Map.entry("CBJ", "Columbus Blue Jackets"),
            Map.entry("CGY", "Calgary Flames"),
            Map.entry("CHI", "Chicago Blackhawks"),
            Map.entry("COL", "Colorado Avalanche"),
            Map.entry("DAL", "Dallas Stars"),
            Map.entry("DET", "Detroit Red Wings"),
            Map.entry("EDM", "Edmonton Oilers"),
            Map.entry("FLA", "Florida Panthers"),
            Map.entry("LAK", "Los Angeles Kings"),
            Map.entry("MIN", "Minnesota Wild"),
            Map.entry("MTL", "Montreal Canadiens"),
            Map.entry("NJD", "New Jersey Devils"),
            Map.entry("NSH", "Nashville Predators"),
            Map.entry("NYI", "New York Islanders"),
            Map.entry("NYR", "New York Rangers"),
            Map.entry("OTT", "Ottawa Senators"),
            Map.entry("PHI", "Philadelphia Flyers"),
            Map.entry("PIT", "Pittsburgh Penguins"),
            Map.entry("SEA", "Seattle Kraken"),
            Map.entry("SJS", "San Jose Sharks"),
            Map.entry("STL", "St. Louis Blues"),
            Map.entry("TBL", "Tampa Bay Lightning"),
            Map.entry("TOR", "Toronto Maple Leafs"),
            Map.entry("UTA", "Utah Hockey Club"),
            Map.entry("VAN", "Vancouver Canucks"),
            Map.entry("VGK", "Vegas Golden Knights"),
            Map.entry("WPG", "Winnipeg Jets"),
            Map.entry("WSH", "Washington Capitals")
    );
    //</editor-fold>

    private List<String[]> goalScorers;
    private final String IMGNAME;
    Team(String abrev, String division) {
        this.ABREV = abrev;
        this.NAME = abrevDictionary.get(abrev);
        this.DIV = division;
        this.IMGNAME = ABREV + "_light.svg";
    }

    public String getImg(){
        return IMGNAME;
    }
    public String getName() {
        return NAME;
    }

    public String getAbbreviation() {
        return ABREV;
    }

    public String getDivision() {
        return DIV;
    }

    public static Team getTeamFromAbbreviation(String a) {
        for(Team team : values()){
            if(team.ABREV.equalsIgnoreCase(a)){
                return team;
            }
        }
        throw new IllegalArgumentException("Invalid team abbreviation: " + a);
    }


    @Override
    public String toString(){
        return ABREV;
    }

}
