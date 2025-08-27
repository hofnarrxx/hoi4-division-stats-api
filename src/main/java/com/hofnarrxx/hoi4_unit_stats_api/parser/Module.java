package com.hofnarrxx.hoi4_unit_stats_api.parser;

import java.util.Map;

public class Module {
    String name;
    String id;
    String category;
    Map<String, Double> addStats;
    Map<String, Double> multiplyStats;
    public Module(String name){
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public Map<String, Double> getAddStats() {
        return addStats;
    }
    public void setAddStats(Map<String, Double> addStats) {
        this.addStats = addStats;
    }
    public Map<String, Double> getMultiplyStats() {
        return multiplyStats;
    }
    public void setMultiplyStats(Map<String, Double> multiplyStats) {
        this.multiplyStats = multiplyStats;
    }
    
}
