package com.hofnarrxx.hoi4_unit_stats_api.parser.tech_parser;

import java.util.HashMap;
import java.util.Map;

import com.hofnarrxx.hoi4_unit_stats_api.parser.TerrainModifier;
import com.hofnarrxx.hoi4_unit_stats_api.parser.unit_parser.BattalionMult;

public class Technology {
    private String id;
    private int year;
    private Map<String, Map<String, Double>> effects; // e.g. maintenance_company = {reliability_factor = 0.05}
    private Map<String, BattalionMult> mults; // e.g. maintenance_company = {battalion_mult = {...}}
    private Map<String, TerrainModifier> terrainModifiers;

    public Technology(String id){
        this.id = id;
        effects = new HashMap<>();
        mults = new HashMap<>();
        terrainModifiers = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Map<String, Map<String, Double>> getEffects() {
        return effects;
    }

    public void setEffects(Map<String, Map<String, Double>> effects) {
        this.effects = effects;
    }

    public void addEffect(String receiver, Map<String, Double> effect){
        this.effects.put(receiver, effect);
    }

    public Map<String, BattalionMult> getMults() {
        return mults;
    }

    public void setMults(Map<String, BattalionMult> mults) {
        this.mults = mults;
    }
    
    public void addMult(String receiver, BattalionMult mult){
        this.mults.put(receiver, mult);
    }

    public Map<String, TerrainModifier> getTerrainModifiers() {
        return terrainModifiers;
    }

    public void setTerrainModifiers(Map<String, TerrainModifier> terrainModifiers) {
        this.terrainModifiers = terrainModifiers;
    }

    public void addTerrainModifier(String receiver, TerrainModifier modifier){
        this.terrainModifiers.put(receiver, modifier);
    }
}
