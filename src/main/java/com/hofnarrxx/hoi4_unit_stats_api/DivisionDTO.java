package com.hofnarrxx.hoi4_unit_stats_api;

import java.util.ArrayList;

import com.hofnarrxx.hoi4_unit_stats_api.parser.Unit;

public class DivisionDTO {
    ArrayList<Unit> battalions;
    ArrayList<String> support;
    int year;
    //doctrine?
    public int getBattalionCount() {
        return (int) this.battalions.stream().filter(battalion -> !battalion.getId().isBlank()).count();
    }
}
