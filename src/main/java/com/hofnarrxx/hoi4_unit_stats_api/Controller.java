package com.hofnarrxx.hoi4_unit_stats_api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hofnarrxx.hoi4_unit_stats_api.parser.Module;
import com.hofnarrxx.hoi4_unit_stats_api.parser.equipment_parser.Equipment;
import com.hofnarrxx.hoi4_unit_stats_api.parser.tech_parser.Technology;
import com.hofnarrxx.hoi4_unit_stats_api.parser.unit_parser.Unit;

@RestController
public class Controller {
    StatsService statsService;

    Controller(StatsService ss) {
        this.statsService = ss;
    }

    @GetMapping("/units")
    public ResponseEntity<List<Unit>> getUnits() {
        List<Unit> stats = statsService.getUnitList();
        return ResponseEntity.ok(stats);

    }

    @GetMapping("/equipment")
    public ResponseEntity<List<Equipment>> getEquipment() {
        List<Equipment> stats = statsService.getEquipmentList();
        return ResponseEntity.ok(stats);

    }

    @GetMapping("/modules")
    public ResponseEntity<List<Module>> getModules() {
        List<Module> stats = statsService.getModuleList();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/technologies")
    public ResponseEntity<List<Technology>> getTechnologies(){
        List<Technology> stats = statsService.getTechList();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/calculate")
    public ResponseEntity<?> calculateDivisionStats(@RequestBody DivisionDTO div){
        DivStatsDTO stats = statsService.calculateDivisionStats(div);
        return ResponseEntity.ok(stats);
    }
}
