package com.hofnarrxx.hoi4_unit_stats_api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hofnarrxx.hoi4_unit_stats_api.parser.Equipment;
import com.hofnarrxx.hoi4_unit_stats_api.parser.EquipmentParser;
import com.hofnarrxx.hoi4_unit_stats_api.parser.FileUtil;
import com.hofnarrxx.hoi4_unit_stats_api.parser.Unit;
import com.hofnarrxx.hoi4_unit_stats_api.parser.UnitParser;

@Service
public class StatsService {
    UnitParser unitParser;
    EquipmentParser equipmentParser;
    FileUtil fileUtil;

    public StatsService(UnitParser up, EquipmentParser ep, FileUtil fu) {
        this.unitParser = up;
        this.equipmentParser = ep;
        this.fileUtil = fu;
    }

    public List<Unit> getStatsForUnits() {
        List<Unit> unitList = new ArrayList<>();
        String folderPath = "src/main/resources/data/units";
        try {
            List<String> unitFiles = FileUtil.readFolder(folderPath);
            unitFiles.forEach(file -> unitList.addAll(unitParser.parseUnits(file)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return unitList;
    }
    public List<Equipment> getStatsForEquipment() {
        List<Equipment> equipmentList = new ArrayList<>();
        String folderPath = "src/main/resources/data/equipment";
        try {
            List<String> equipmentFiles = FileUtil.readFolder(folderPath);
            equipmentFiles.forEach(file -> equipmentList.addAll(equipmentParser.parseEquipments(file)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return equipmentList;
    }
}
