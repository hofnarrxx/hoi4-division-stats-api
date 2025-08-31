package com.hofnarrxx.hoi4_unit_stats_api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
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
                        throw new RuntimeException("bad design");
                    }
                }
            }
        }
        // check support
        for (int i = 0; i < 5; i++) {
            if (div.getSupportCompanies().get(i).equals(""))
                continue;
            int fi = i;
            Unit support = unitList.stream().filter(u -> u.getId().equals(div.getSupportCompanies().get(fi)))
                    .findFirst()
                    .orElse(null);
            for (int j = i + 1; j < 5; j++) {
                if (div.getSupportCompanies().get(j).equals(""))
                    continue;
                int fj = j;
                Unit anotherSupport = unitList.stream().filter(u -> u.getId().equals(div.getSupportCompanies().get(fj)))
                        .findFirst().orElse(null);
                if (support.getSupportTypeBlock() != null
                        && anotherSupport.getName().contains(support.getSupportTypeBlock())) {
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
        int supportCompaniesCount = div.getSupportCompaniesCount();
        int unitCount = battalionCount + supportCompaniesCount;
        double recoveryRateSum = 0;
        double apAttackSum = 0, apAttackMax = 0;
        double hardnessSum = 0;
        double armorSum = 0, armorMax = 0;
        double orgSum = 0;
        divStatsDTO.setMaximumSpeed(999);
        // main stats
        for (Unit battalion : battalions) {
            // Unit battalion = battalions.get(i);
            // base
            divStatsDTO.setHp(divStatsDTO.getHp() + battalion.getHp());
            orgSum += battalion.getOrg();
            recoveryRateSum += battalion.getRecoveryRate();
            divStatsDTO.setWeight(divStatsDTO.getWeight() + battalion.getWeight());
            divStatsDTO.setSupplyConsumption(divStatsDTO.getSupplyConsumption() + battalion.getSupplyConsumption());
            divStatsDTO.setCombatWidth(divStatsDTO.getCombatWidth() + battalion.getCombatWidth());
            // find matching equipment
            List<Equipment> battalionEquipment = new ArrayList<>();
            Map<Equipment, Integer> battalionEquipmentMap = new HashMap<>();
            for (Map.Entry<String, Integer> entry : battalion.getEquipment().entrySet()) {
                Equipment archetype = equipmentList.stream().filter(eq -> eq.getId().equals(entry.getKey())).findFirst().orElse(null);
                List<Equipment> temp = equipmentList.stream().filter(eq-> !eq.isArchetype() && eq.getArchetype().equals(archetype.getId()) && eq.getYear() <= div.getYear()).collect(Collectors.toList());
                temp.stream().max((eq1, eq2) -> Integer.compare(eq1.getYear(), eq2.getYear())).ifPresent(eq -> {
                    archetype.upgrage(eq);
                    battalionEquipment.add(archetype);
                    battalionEquipmentMap.put(archetype, entry.getValue());
                });
                // List<Equipment> temp = new ArrayList<>();
                // temp.addAll(equipmentList.stream()
                //         .filter(eq -> eq.getId().matches("^" + entry.getKey() + "(_\\d+)?$")
                //                 && eq.getYear() <= div.getYear())
                //         .collect(Collectors.toList()));
                // temp.stream().max((eq1, eq2) -> Integer.compare(eq1.getYear(), eq2.getYear())).ifPresent(eq -> {
                //     battalionEquipment.add(eq);
                //     battalionEquipmentMap.put(eq, entry.getValue());
                // });
            }
            // stats from equipment
            for (Equipment eq : battalionEquipment) {
                if (divStatsDTO.getMaximumSpeed() > eq.getMaximumSpeed() && eq.getMaximumSpeed()>0) {
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
                if (divStatsDTO.getTerrainModifiers().get(entry.getKey()) == null) {
                    divStatsDTO.getTerrainModifiers().put(entry.getKey(), entry.getValue());
                } else {
                    divStatsDTO.getTerrainModifiers().put(entry.getKey(),
                            divStatsDTO.getTerrainModifiers().get(entry.getKey()).add(entry.getValue()));
                }
            }
        }
        // support stats
        for (Unit supportCompany : supportCompanies) {
            // base
            divStatsDTO.setHp(divStatsDTO.getHp() + supportCompany.getHp());
            orgSum += supportCompany.getOrg();
            recoveryRateSum += supportCompany.getRecoveryRate();
            divStatsDTO.setWeight(divStatsDTO.getWeight() + supportCompany.getWeight());
            divStatsDTO
                    .setSupplyConsumption(divStatsDTO.getSupplyConsumption() + supportCompany.getSupplyConsumption());
            divStatsDTO.setCombatWidth(divStatsDTO.getCombatWidth() + supportCompany.getCombatWidth());
            // find matching equipment
            List<Equipment> battalionEquipment = new ArrayList<>();
            Map<Equipment, Integer> battalionEquipmentMap = new HashMap<>();
            for (Map.Entry<String, Integer> entry : supportCompany.getEquipment().entrySet()) {
                Equipment archetype = equipmentList.stream().filter(eq -> eq.getId().equals(entry.getKey())).findFirst().orElse(null);
                List<Equipment> temp = equipmentList.stream().filter(eq-> !eq.isArchetype() && eq.getArchetype().equals(archetype.getId()) && eq.getYear() <= div.getYear()).collect(Collectors.toList());
                temp.stream().max((eq1, eq2) -> Integer.compare(eq1.getYear(), eq2.getYear())).ifPresent(eq -> {
                    archetype.upgrage(eq);
                    battalionEquipment.add(archetype);
                    battalionEquipmentMap.put(archetype, entry.getValue());
                });
                // List<Equipment> temp = new ArrayList<>();
                // temp.addAll(equipmentList.stream()
                //         .filter(eq -> eq.getId().matches("^" + entry.getKey() + "(_\\d+)?$")
                //                 && eq.getYear() <= div.getYear())
                //         .collect(Collectors.toList()));
                // temp.stream().max((eq1, eq2) -> Integer.compare(eq1.getYear(), eq2.getYear())).ifPresent(eq -> {
                //     battalionEquipment.add(eq);
                //     battalionEquipmentMap.put(eq, entry.getValue());
                // });
            }
            // stats from equipment
            for (Equipment eq : battalionEquipment) {
                if (divStatsDTO.getMaximumSpeed() > eq.getMaximumSpeed() && eq.getMaximumSpeed() > 0) {
                    divStatsDTO.setMaximumSpeed(eq.getMaximumSpeed());
                }
                divStatsDTO.setSoftAttack(divStatsDTO.getSoftAttack() + eq.getSoftAttack()*(1 + supportCompany.getSupportModifiers().getOrDefault("soft_attack", 0.0)));
                divStatsDTO.setHardAttack(divStatsDTO.getHardAttack() + eq.getHardAttack()*(1 + supportCompany.getSupportModifiers().getOrDefault("hard_attack", 0.0)));
                divStatsDTO.setAirAttack(divStatsDTO.getAirAttack() + eq.getAirAttack()*(1 + supportCompany.getSupportModifiers().getOrDefault("air_attack", 0.0)));
                double currApAttack = eq.getApAttack()*(1 + supportCompany.getSupportModifiers().getOrDefault("ap_attack", 0.0));
                apAttackSum += currApAttack;
                apAttackMax = currApAttack > apAttackMax ? currApAttack : apAttackMax; 
                divStatsDTO.setDefense(divStatsDTO.getDefense() + eq.getDefense()*(1 + supportCompany.getSupportModifiers().getOrDefault("defense", 0.0)));
                divStatsDTO.setBreakthrough(divStatsDTO.getBreakthrough() + eq.getBreakthrough()*(1 + supportCompany.getSupportModifiers().getOrDefault("breakthrough", 0.0)));
                hardnessSum += eq.getHardness();
                double currArmor = eq.getArmorValue()*(1 + supportCompany.getSupportModifiers().getOrDefault("armor_value", 0.0));
                armorSum += currArmor;
                armorMax = currArmor > armorMax ? currArmor : armorMax;
            }
            // cost
            for (Map.Entry<Equipment, Integer> entry : battalionEquipmentMap.entrySet()) {
                divStatsDTO.setBuildCostIc(
                        divStatsDTO.getBuildCostIc() + entry.getKey().getBuildCostIc() * entry.getValue());
            }
            divStatsDTO.setManpower(divStatsDTO.getManpower() + supportCompany.getManpower());
            if (supportCompany.getTrainingTime() > divStatsDTO.getTrainingTime())
                divStatsDTO.setTrainingTime(supportCompany.getTrainingTime());
            // terrain
            for (Map.Entry<TerrainType, TerrainModifier> entry : supportCompany.getTerrainModifiers().entrySet()) {
                if (divStatsDTO.getTerrainModifiers().get(entry.getKey()) == null) {
                    divStatsDTO.getTerrainModifiers().put(entry.getKey(), entry.getValue());
                } else {
                    divStatsDTO.getTerrainModifiers().put(entry.getKey(),
                            divStatsDTO.getTerrainModifiers().get(entry.getKey()).add(entry.getValue()));
                }
            }
        }
        divStatsDTO.setOrg(orgSum / unitCount);
        divStatsDTO.setRecoveryRate(recoveryRateSum / unitCount);
        // A division's piercing is equal to 40% of the highest piercing in the division
        // plus 60% of the average piercing of all battalions and companies in the
        // division.
        divStatsDTO.setApAttack(apAttackSum / unitCount * 0.6 + apAttackMax * 0.4);
        divStatsDTO.setHardness(hardnessSum / unitCount);
        divStatsDTO.setArmorValue(armorSum / unitCount * 0.6 + armorMax * 0.4);
        return divStatsDTO;
    }
}
