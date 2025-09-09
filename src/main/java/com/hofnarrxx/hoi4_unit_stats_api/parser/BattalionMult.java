package com.hofnarrxx.hoi4_unit_stats_api.parser;

import java.util.HashMap;
import java.util.Map;

public class BattalionMult {
    String category;
    Map<String, Double> multipliers;
    public BattalionMult(){
        multipliers = new HashMap<>();
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public Map<String, Double> getMultipliers() {
        return multipliers;
    }
    public void setMultipliers(Map<String, Double> multipliers) {
        this.multipliers = multipliers;
    }
    public void addMultiplier(String parameter, double value){
        multipliers.put(parameter, value);
    }

    public void combine(BattalionMult other){
        other.getMultipliers().forEach((k,v)->this.getMultipliers().merge(k, v, Double::sum));
    }
    @Override
    public String toString(){
        return category+": "+multipliers;
    }
}
