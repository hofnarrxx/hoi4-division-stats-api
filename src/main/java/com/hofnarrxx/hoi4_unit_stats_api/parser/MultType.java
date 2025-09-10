package com.hofnarrxx.hoi4_unit_stats_api.parser;

public class MultType {
    String stat;
    Boolean additive;
    public MultType(){
        additive = false;
    }
    public MultType(String stat, Boolean additive){
        this.stat = stat;
        this.additive = additive;
    }
    public String getStat() {
        return stat;
    }
    public void setStat(String stat) {
        this.stat = stat;
    }
    public Boolean isAdditive() {
        return additive;
    }
    public void setAdditive(Boolean additive) {
        this.additive = additive;
    }
    
}
