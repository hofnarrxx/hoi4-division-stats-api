package com.hofnarrxx.hoi4_unit_stats_api;

import java.util.ArrayList;

import com.hofnarrxx.hoi4_unit_stats_api.parser.Unit;

public class DivisionDTO {
    private ArrayList<Unit> battalions;
    private ArrayList<Unit> support;
    private int year;
    //doctrine?
    
    public int getBattalionCount() {
        return (int) this.battalions.stream().filter(battalion -> !battalion.getId().isBlank()).count();
    }

    public int getSupportCount() {
        return (int) this.support.stream().filter(support -> !support.getId().isBlank()).count();
    }

    public ArrayList<Unit> getBattalions() {
        return battalions;
    }

    public void setBattalions(ArrayList<Unit> battalions) {
        this.battalions = battalions;
    }

    public ArrayList<Unit> getSupport() {
        return support;
    }

    public void setSupport(ArrayList<Unit> support) {
        this.support = support;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
