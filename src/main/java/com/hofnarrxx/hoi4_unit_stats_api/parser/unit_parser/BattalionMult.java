package com.hofnarrxx.hoi4_unit_stats_api.parser.unit_parser;

import java.util.HashMap;
import java.util.Map;

public class BattalionMult {
    String category;
    Map<MultType, Double> multipliers;
    public BattalionMult(){
        multipliers = new HashMap<>();
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public Map<MultType, Double> getMultipliers() {
        return multipliers;
    }
    public void setMultipliers(Map<MultType, Double> multipliers) {
        this.multipliers = multipliers;
    }
    public void addMultiplier(MultType parameter, double value){
        multipliers.put(parameter, value);
    }
    

    public void combine(BattalionMult other){
        other.getMultipliers().forEach((k,v)->this.getMultipliers().merge(k, v, Double::sum));
    }
    @Override
    public String toString(){
        return category+": "+multipliers;
    }

    public static double apply(Map<MultType, Double> multipliers, double value, String stat){
        
        MultType addMultType = new MultType(stat, true);
        MultType multType = new MultType(stat, false);
        double additiveValue = multipliers.getOrDefault(addMultType, 0.0);
        double multValue = multipliers.getOrDefault(multType, 0.0);
        return value * (1 + multValue) + additiveValue;
    }
}
