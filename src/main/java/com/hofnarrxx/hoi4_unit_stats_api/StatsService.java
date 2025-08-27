package com.hofnarrxx.hoi4_unit_stats_api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hofnarrxx.hoi4_unit_stats_api.parser.Equipment;
import com.hofnarrxx.hoi4_unit_stats_api.parser.EquipmentParser;
import com.hofnarrxx.hoi4_unit_stats_api.parser.FileUtil;
import com.hofnarrxx.hoi4_unit_stats_api.parser.Module;
import com.hofnarrxx.hoi4_unit_stats_api.parser.ModuleParser;
import com.hofnarrxx.hoi4_unit_stats_api.parser.Unit;
import com.hofnarrxx.hoi4_unit_stats_api.parser.UnitParser;

@Service
public class StatsService {
    List<Unit> unitList = new ArrayList<>();
    List<Equipment> equipmentList = new ArrayList<>();
    List<Module> moduleList = new ArrayList<>();

    public StatsService() {
        String unitsFolderPath = "src/main/resources/data/units";
        try {
            List<String> unitFiles = FileUtil.readFolder(unitsFolderPath);
            unitFiles.forEach(file -> unitList.addAll(UnitParser.parseUnits(file)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        String equipmentFolderPath = "src/main/resources/data/equipment";
        try {
            List<String> equipmentFiles = FileUtil.readFolder(equipmentFolderPath);
            equipmentFiles.forEach(file -> equipmentList.addAll(EquipmentParser.parseEquipments(file)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        String modulesFolderPath = "src/main/resources/data/modules";
        try {
            List<String> modulesFiles = FileUtil.readFolder(modulesFolderPath);
            modulesFiles.forEach(file -> moduleList.addAll(ModuleParser.parseModules(file)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Unit> getUnitList() {
        return unitList;
    }

    public List<Equipment> getEquipmentList() {
        return equipmentList;
    }

    public List<Module> getModuleList() {
        return moduleList;
    }

    public void calculateDivisionStats(DivisionDTO div) {
        // check
        for (int i = 0; i < 5; i++) {
            String columnGroup = div.battalions.get(i * 5).getGroup();
            for (int j = 0; i < 5; j++) {
                if (!div.battalions.get(i * 5 + j).getGroup().equals(columnGroup)) {
                    // error
                }
            }
        }
        // if good calculate and return stats
        DivStatsDTO divStatsDTO = new DivStatsDTO();
        double recoveryRateSum = 0;
        for (int i = 0; i < 25; i++) {
            Unit battalion = div.battalions.get(i);
            // base 
            divStatsDTO.setHp(divStatsDTO.getHp() + battalion.getHp());
            divStatsDTO.setOrg(divStatsDTO.getOrg() + battalion.getOrg());
            recoveryRateSum += battalion.getRecoveryRate();
            divStatsDTO.setWeight(divStatsDTO.getWeight() + battalion.getWeight());
            divStatsDTO.setSupplyConsumption(divStatsDTO.getSupplyConsumption() + battalion.getSupplyConsumption());
            // combat
            divStatsDTO.setCombatWidth(divStatsDTO.getCombatWidth() + battalion.getCombatWidth());
            // cost
            divStatsDTO.setManpower(divStatsDTO.getManpower() + battalion.getManpower());
            if (battalion.getTrainingTime() > divStatsDTO.getTrainingTime())
                divStatsDTO.setTrainingTime(battalion.getTrainingTime());

        }
    }
}
