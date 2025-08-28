package com.hofnarrxx.hoi4_unit_stats_api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public DivStatsDTO calculateDivisionStats(DivisionDTO div) {
        // check
        for (int i = 0; i < 5; i++) {
            String columnGroup = div.getBattalions().get(i * 5).getGroup();
            for (int j = 0; i < 5; j++) {
                if (!div.getBattalions().get(i * 5 + j).getGroup().equals(columnGroup)) {
                    // error
                }
                
            }
        }
        for(int i = 0; i < 5; i++){
            Unit support = div.getSupport().get(i);
            for(int j = i+1;j < 5; j++){
                if(div.getSupport().get(j).getName().contains(support.getSupportTypeBlock())) {
                    //error
                }
            }
        }
        // if good calculate and return stats
        DivStatsDTO divStatsDTO = new DivStatsDTO();
        int battalionCount = div.getBattalionCount();
        double recoveryRateSum = 0;
        double apAttackSum = 0, apAttackMax = 0;
        double hardnessSum = 0;
        double armorSum = 0, armorMax = 0;

        for (int i = 0; i < 25; i++) {
            Unit battalion = div.getBattalions().get(i);
            if (battalion.getId().isBlank())
                continue;
            // base
            divStatsDTO.setHp(divStatsDTO.getHp() + battalion.getHp());
            divStatsDTO.setOrg(divStatsDTO.getOrg() + battalion.getOrg());
            recoveryRateSum += battalion.getRecoveryRate();
            divStatsDTO.setWeight(divStatsDTO.getWeight() + battalion.getWeight());
            divStatsDTO.setSupplyConsumption(divStatsDTO.getSupplyConsumption() + battalion.getSupplyConsumption());
            divStatsDTO.setCombatWidth(divStatsDTO.getCombatWidth() + battalion.getCombatWidth());
            // find matching equipment
            List<Equipment> battalionEquipment = new ArrayList<>();
            Map<Equipment, Integer> battalionEquipmentMap = new HashMap<>();
            for (Map.Entry<String, Integer> entry : battalion.getEquipment().entrySet()) {
                battalionEquipment = equipmentList.stream()
                        .filter(eq -> eq.getArchetype().equals(entry.getKey()) && eq.getYear() == div.getYear())
                        .collect(Collectors.toList());

                for (Equipment eq : battalionEquipment) {
                    battalionEquipmentMap.put(eq, entry.getValue());
                }
            }
            // stats from equipment
            for (Equipment eq : battalionEquipment) {
                if (divStatsDTO.getMaximumSpeed() > eq.getMaximumSpeed()) {
                    divStatsDTO.setMaximumSpeed(eq.getMaximumSpeed());
                }
                divStatsDTO.setSoftAttack(divStatsDTO.getSoftAttack() + eq.getSoftAttack());
                divStatsDTO.setHardAttack(divStatsDTO.getHardAttack() + eq.getHardAttack());
                divStatsDTO.setAirAttack(divStatsDTO.getAirAttack() + eq.getAirAttack());
                apAttackSum += eq.getApAttack();
                if (eq.getApAttack() > apAttackMax)
                    apAttackMax = eq.getApAttack();
                divStatsDTO.setDefense(divStatsDTO.getDefense() + eq.getDefense());
                divStatsDTO.setBreakthrough(divStatsDTO.getBreakthrough() + eq.getBreakthrough());
                hardnessSum += eq.getHardness();
                armorSum += eq.getArmorValue();
                if (eq.getArmorValue() > armorMax)
                    armorMax = eq.getArmorValue();
            }
            // cost
            for (Map.Entry<Equipment, Integer> entry : battalionEquipmentMap.entrySet()) {
                divStatsDTO.setBuildCostIc(
                        divStatsDTO.getBuildCostIc() + entry.getKey().getBuildCostIc() * entry.getValue());
            }
            divStatsDTO.setManpower(divStatsDTO.getManpower() + battalion.getManpower());
            if (battalion.getTrainingTime() > divStatsDTO.getTrainingTime())
                divStatsDTO.setTrainingTime(battalion.getTrainingTime());

        }
        divStatsDTO.setRecoveryRate(recoveryRateSum / battalionCount);
        // A division's piercing is equal to 40% of the highest piercing in the division
        // plus 60% of the average piercing of all battalions and companies in the
        // division.
        divStatsDTO.setApAttack(apAttackSum / battalionCount * 0.6 + apAttackMax * 0.4);
        divStatsDTO.setHardness(hardnessSum / battalionCount);
        divStatsDTO.setArmorValue(armorSum / battalionCount * 0.6 + armorMax * 0.4);
        return divStatsDTO;
    }
}
