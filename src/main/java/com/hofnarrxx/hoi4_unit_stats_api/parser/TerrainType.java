package com.hofnarrxx.hoi4_unit_stats_api.parser;

public enum TerrainType {
    FOREST,
    HILLS,
    MOUNTAIN,
    PLAINS,
    MARSH,
    JUNGLE,
    FORT,
    RIVER,
    AMPHIBIOUS,
    DESERT;
    public static boolean isValidTerrain(String terrain){
        for(TerrainType tr : TerrainType.values()){
            if(tr.name().equalsIgnoreCase(terrain)){
                return true;
            }
        }
        return false;
    }
}
