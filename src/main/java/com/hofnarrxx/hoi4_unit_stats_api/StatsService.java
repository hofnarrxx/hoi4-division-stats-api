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
import com.hofnarrxx.hoi4_unit_stats_api.parser.TerrainModifier;
import com.hofnarrxx.hoi4_unit_stats_api.parser.TerrainType;
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
        // for (int i = 0; i < 5; i++) {
        // int fi = i;
        // Unit battalion = unitList.stream().filter(u ->
        // u.getId().equals(div.getBattalions().get(fi * 5)))
        // .findFirst().orElse(null);
        // String columnGroup = battalion.getGroup();
        // for (int j = 1; j < 5; j++) {
        // int fj =j;
        // if(div.getBattalions().get(fi * 5 + fj).equals("")) continue;
        // Unit anotherBattalion = unitList.stream()
        // .filter(u -> u.getId().equals(div.getBattalions().get(fi * 5 +
        // fj))).findFirst().orElse(null);
        // if (!anotherBattalion.getGroup().equals(columnGroup)) {
        // // error
        // }

        // }
        // }
        // check battalions
        for (int i = 0; i < 5; i++) {
            ArrayList<String> column = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                column.add(div.getBattalions().get(5 * i + j));
            }
            for (int k = 0; k < 5; k++) {
                if (column.get(k).equals(""))
                    continue;
                int fk = k;
                Unit battalion = unitList.stream().filter(u -> u.getId().equals(div.getBattalions().get(fk)))
                        .findFirst()
                        .orElse(null);
                for (int h = k + 1; h < 5; h++) {
                    if (column.get(h).equals(""))
                        continue;
                    int fh = h;
                    Unit anotherBattalion = unitList.stream()
                            .filter(u -> u.getId().equals(div.getBattalions().get(fh)))
                            .findFirst().orElse(null);
                    if (!anotherBattalion.getGroup().equals(battalion.getGroup())) {
                        // error
                    }
                }
            }
        }
        // check support
        for (int i = 0; i < 5; i++) {
            if(div.getSupportCompanies().get(i).equals("")) continue;
            int fi = i;
            Unit support = unitList.stream().filter(u -> u.getId().equals(div.getSupportCompanies().get(fi)))
                    .findFirst()
                    .orElse(null);
            for (int j = i + 1; j < 5; j++) {
                if(div.getSupportCompanies().get(j).equals("")) continue;
                int fj = j;
                Unit anotherSupport = unitList.stream().filter(u -> u.getId().equals(div.getSupportCompanies().get(fj)))
                        .findFirst().orElse(null);
                        System.out.println(support+"..."+anotherSupport);
                if (support.getSupportTypeBlock() != null && anotherSupport.getName().contains(support.getSupportTypeBlock())) {
                    // error
                }
            }
        }
        // convert list of strings into list of units
        ArrayList<Unit> battalions = new ArrayList<>();
        ArrayList<Unit> supportCompanies = new ArrayList<>();
        for (String unitId : div.getBattalions()) {
            if (!unitId.isBlank()) {
                battalions.add(unitList.stream()
                        .filter(unit -> unit.getId().equals(unitId) && !unit.getGroup().equals("support")).findFirst()
                        .orElse(/* exception */null));
            }
        }
        for (String unitId : div.getSupportCompanies()) {
            if (!unitId.isBlank()) {
                supportCompanies.add(unitList.stream()
                        .filter(unit -> unit.getId().equals(unitId) && unit.getGroup().equals("support")).findFirst()
                        .orElse(/* exception */null));
            }
        }
        // calculate and return stats
        DivStatsDTO divStatsDTO = new DivStatsDTO();
        int battalionCount = div.getBattalionCount();
        double recoveryRateSum = 0;
        double apAttackSum = 0, apAttackMax = 0;
        double hardnessSum = 0;
        double armorSum = 0, armorMax = 0;

        for (int i = 0; i < 25; i++) {
            Unit battalion = battalions.get(i);
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
                        .filter(eq -> eq.getId().contains(entry.getKey()) && eq.getYear() == div.getYear())
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
            // terrain
            for (Map.Entry<TerrainType, TerrainModifier> entry : battalion.getTerrainModifiers().entrySet()) {
                divStatsDTO.getTerrainModifiers().put(entry.getKey(),
                        divStatsDTO.getTerrainModifiers().get(entry.getKey()).add(entry.getValue()));
            }
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
