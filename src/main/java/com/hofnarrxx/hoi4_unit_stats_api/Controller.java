package com.hofnarrxx.hoi4_unit_stats_api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hofnarrxx.hoi4_unit_stats_api.parser.Equipment;
import com.hofnarrxx.hoi4_unit_stats_api.parser.Unit;

@RestController
public class Controller {
    StatsService statsService;
    Controller(StatsService ss){
        this.statsService = ss;
    }
    @GetMapping("/units")
    public ResponseEntity<List<Unit>> getUnits(){
        List<Unit> stats = statsService.getStatsForUnits();
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }    

    @GetMapping("/equipment")
    public ResponseEntity<List<Equipment>> getEquipment(){
        List<Equipment> stats = statsService.getStatsForEquipment();
        System.out.println(stats);
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }   
}
