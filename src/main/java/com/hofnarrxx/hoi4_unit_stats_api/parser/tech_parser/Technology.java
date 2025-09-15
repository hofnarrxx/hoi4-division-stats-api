package com.hofnarrxx.hoi4_unit_stats_api.parser.tech_parser;

import java.util.HashMap;
import java.util.Map;

public class Technology {
    private String id;
    private int year;
    private Map<String, Map<String, Double>> effects;

    public Technology(String id){
        this.id = id;
        effects = new HashMap<>();
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
    
}
