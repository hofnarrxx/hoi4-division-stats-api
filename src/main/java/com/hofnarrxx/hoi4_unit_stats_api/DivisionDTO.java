package com.hofnarrxx.hoi4_unit_stats_api;

import java.util.ArrayList;

import com.hofnarrxx.hoi4_unit_stats_api.parser.Unit;

public class DivisionDTO {
    private ArrayList<String> battalions;
    private ArrayList<String> supportCompanies;
    private int year;
    //doctrine?
    
    public int getBattalionCount() {
        //return (int) this.battalions.stream().filter(battalion -> !battalion.getId().isBlank()).count();
        return (int) this.battalions.stream().filter(battalion -> !battalion.isBlank()).count();
    }

    public int getSupportCompaniesCount() {
        //return (int) this.support.stream().filter(support -> !support.getId().isBlank()).count();
        return (int) this.supportCompanies.stream().filter(support -> !support.isBlank()).count();
    }

    public ArrayList<String> getBattalions() {
        return battalions;
    }

    public void setBattalions(ArrayList<String> battalions) {
        this.battalions = battalions;
    }

    public ArrayList<String> getSupportCompanies() {
        return supportCompanies;
    }

    public void setSupportCompanies(ArrayList<String> supportCompanies) {
        this.supportCompanies = supportCompanies;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
