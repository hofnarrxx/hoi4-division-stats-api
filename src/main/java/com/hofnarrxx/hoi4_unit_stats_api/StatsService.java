package com.hofnarrxx.hoi4_unit_stats_api;

import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.hofnarrxx.hoi4_unit_stats_api.parser.FileUtil;
import com.hofnarrxx.hoi4_unit_stats_api.parser.Module;
import com.hofnarrxx.hoi4_unit_stats_api.parser.ModuleParser;
import com.hofnarrxx.hoi4_unit_stats_api.parser.TerrainModifier;
import com.hofnarrxx.hoi4_unit_stats_api.parser.TerrainType;
import com.hofnarrxx.hoi4_unit_stats_api.parser.equipment_parser.Equipment;
import com.hofnarrxx.hoi4_unit_stats_api.parser.equipment_parser.EquipmentParser;
import com.hofnarrxx.hoi4_unit_stats_api.parser.tech_parser.TechParser;
import com.hofnarrxx.hoi4_unit_stats_api.parser.tech_parser.Technology;
import com.hofnarrxx.hoi4_unit_stats_api.parser.unit_parser.BattalionMult;
import com.hofnarrxx.hoi4_unit_stats_api.parser.unit_parser.MultType;
import com.hofnarrxx.hoi4_unit_stats_api.parser.unit_parser.Unit;
import com.hofnarrxx.hoi4_unit_stats_api.parser.unit_parser.UnitParser;

@Service
public class StatsService {
    List<Unit> unitList = new ArrayList<>();
    List<Equipment> equipmentList = new ArrayList<>();
    List<Module> moduleList = new ArrayList<>();
    List<Technology> techList = new ArrayList<>();

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

        String techFolderPath = "src/main/resources/data/technologies";
        try{
            List<String> techFiles = FileUtil.readFolder(techFolderPath);
            techFiles.forEach(file -> techList.addAll(TechParser.parseTechnologies(file)));
        } catch(Exception e){
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

    public List<Technology> getTechList(){
        return techList;
    }

    public DivStatsDTO calculateDivisionStats(DivisionDTO div) {
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
                for (String blockedType : support.getSupportTypeBlocks()) {
                    if (anotherSupport.getName().contains(blockedType)) {
                        // error
                    }
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
        Map<TerrainType, TerrainModifier> supportTerrainModifiers = new HashMap<>();
        Map<TerrainType, TerrainModifier> battalionsTerrainModifiersSum = new HashMap<>();
        Map<TerrainType, TerrainModifier> divisionTerrainModifiers = new HashMap<>();
        double recoveryRateSum = 0;
        double apAttackSum = 0, apAttackMax = 0;
        double hardnessSum = 0;
        double armorSum = 0, armorMax = 0;
        double orgSum = 0;
        divStatsDTO.setMaximumSpeed(999);
        // battalion_mult handling, this parameter can be found in support companies
        ArrayList<BattalionMult> battalionMults = new ArrayList<>();
        Map<String, Double> factors = new HashMap<>();
        for (Unit supportCompany : supportCompanies) {
            for (BattalionMult currentMult : supportCompany.getBattalionMults()) {
                if (currentMult.getCategory() != null) {
                    BattalionMult match = battalionMults.stream()
                            .filter(m -> currentMult.getCategory().equals(m.getCategory())).findFirst().orElse(null);
                    if (match == null) {
                        battalionMults.add(currentMult);
                    } else {
                        int pos = battalionMults.indexOf(match);
                        match.combine(currentMult);
                        battalionMults.set(pos, match);
                    }
                }
            }
            // factors handling
            for (Map.Entry<String, Double> entry : supportCompany.getFactors().entrySet()) {
                if (factors.containsKey(entry.getKey())) {
                    double sum = factors.get(entry.getKey());
                    factors.put(entry.getKey(), sum + entry.getValue());
                } else {
                    factors.put(entry.getKey(), entry.getValue());
                }
            }
        }

        // support stats
        for (Unit supportCompany : supportCompanies) {
            BattalionMult battalionMult = battalionMults.stream()
                    .filter(m -> {
                        for (String category : supportCompany.getCategories()) {
                            if (m.getCategory().equals(category))
                                return true;
                        }
                        return false;
                    }).findFirst()
                    .orElse(null);
            if (battalionMult == null)
                battalionMult = new BattalionMult();
            Map<MultType, Double> multipliers = battalionMult.getMultipliers();
            Map<String, Double> nerfs = supportCompany.getSupportNerfs();
            // base
            divStatsDTO.setHp(
                    divStatsDTO.getHp() + BattalionMult.apply(multipliers, supportCompany.getHp(), "max_strength"));
            orgSum += BattalionMult.apply(multipliers, supportCompany.getOrg(), "max_organisation");
            recoveryRateSum += BattalionMult.apply(multipliers, supportCompany.getRecoveryRate(), "default_morale");
            divStatsDTO.setWeight(divStatsDTO.getWeight() + supportCompany.getWeight());
            divStatsDTO.setCombatWidth(divStatsDTO.getCombatWidth() + supportCompany.getCombatWidth());
            divStatsDTO.setSuppression(divStatsDTO.getSuppression()
                    + supportCompany.getSuppression() * (1 + factors.getOrDefault("suppresion", 0.0)));
            divStatsDTO.setSupplyConsumption(divStatsDTO.getSupplyConsumption()
                    + supportCompany.getSupplyConsumption() * (1 + factors.getOrDefault("supply_consumption", 0.0)));
            divStatsDTO.setCasualtyTrickleback(
                    divStatsDTO.getCasualtyTrickleback() + supportCompany.getCasualtyTrickleback());
            divStatsDTO.setRecon(divStatsDTO.getRecon() + supportCompany.getRecon());
            // find matching equipment
            List<Equipment> battalionEquipment = new ArrayList<>();
            Map<Equipment, Integer> battalionEquipmentMap = new HashMap<>();
            for (Map.Entry<String, Integer> entry : supportCompany.getEquipment().entrySet()) {
                Equipment archetype = equipmentList.stream().filter(eq -> eq.getId().equals(entry.getKey())).findFirst()
                        .orElse(null);
                List<Equipment> temp = equipmentList.stream().filter(eq -> !eq.isArchetype()
                        && eq.getArchetype().equals(archetype.getId()) && eq.getYear() <= div.getYear())
                        .collect(Collectors.toList());
                temp.stream().max((eq1, eq2) -> Integer.compare(eq1.getYear(), eq2.getYear())).ifPresent(eq -> {
                    Equipment upgraded = new Equipment(archetype);
                    upgraded.upgrade(eq);
                    battalionEquipment.add(upgraded);
                    battalionEquipmentMap.put(upgraded, entry.getValue());
                });
            }
            // stats from equipment
            for (Equipment eq : battalionEquipment) {
                if (divStatsDTO.getMaximumSpeed() > eq.getMaximumSpeed() && eq.getMaximumSpeed() > 0) {
                    divStatsDTO.setMaximumSpeed(eq.getMaximumSpeed());
                }
                divStatsDTO.setSoftAttack(divStatsDTO.getSoftAttack() + BattalionMult.apply(multipliers,
                        eq.getSoftAttack() * (1 + nerfs.getOrDefault("soft_attack", 0.0)), "soft_attack"));
                divStatsDTO.setHardAttack(divStatsDTO.getHardAttack() + BattalionMult.apply(multipliers,
                        eq.getHardAttack() * (1 + nerfs.getOrDefault("hard_attack", 0.0)), "hard_attack"));
                divStatsDTO.setAirAttack(divStatsDTO.getAirAttack() + BattalionMult.apply(multipliers,
                        eq.getAirAttack() * (1 + nerfs.getOrDefault("air_attack", 0.0)), "air_attack"));
                double currApAttack = BattalionMult.apply(multipliers,
                        eq.getApAttack() * (1 + nerfs.getOrDefault("ap_attack", 0.0)), "ap_attack");
                apAttackSum += currApAttack;
                apAttackMax = currApAttack > apAttackMax ? currApAttack : apAttackMax;
                divStatsDTO.setDefense(divStatsDTO.getDefense() + BattalionMult.apply(multipliers,
                        eq.getDefense() * (1 + nerfs.getOrDefault("defense", 0.0)), "defense"));
                divStatsDTO.setBreakthrough(divStatsDTO.getBreakthrough() + BattalionMult.apply(multipliers,
                        eq.getBreakthrough() * (1 + nerfs.getOrDefault("breakthrough", 0.0)), "breakthrough"));
                hardnessSum += eq.getHardness();
                double currArmor = BattalionMult.apply(multipliers,
                        eq.getArmorValue() * (1 + nerfs.getOrDefault("armor_value", 0.0)), "armor_value");
                armorSum += currArmor;
                armorMax = currArmor > armorMax ? currArmor : armorMax;
                divStatsDTO.setFuelConsumption(divStatsDTO.getFuelConsumption()
                        + eq.getFuelConsumption() * (1 + factors.getOrDefault("fuel_consumption", 0.0)));
            }
            // cost
            for (Map.Entry<Equipment, Integer> entry : battalionEquipmentMap.entrySet()) {
                divStatsDTO.setBuildCostIc(
                        divStatsDTO.getBuildCostIc() + entry.getKey().getBuildCostIc() * entry.getValue());
                divStatsDTO.addEquipment(entry.getKey().getId(), entry.getValue());
            }
            divStatsDTO.setManpower(divStatsDTO.getManpower() + supportCompany.getManpower());
            if (supportCompany.getTrainingTime() > divStatsDTO.getTrainingTime())
                divStatsDTO.setTrainingTime(supportCompany.getTrainingTime());
            // terrain
            for (Map.Entry<TerrainType, TerrainModifier> entry : supportCompany.getTerrainModifiers().entrySet()) {
                if (supportTerrainModifiers.get(entry.getKey()) == null) {
                    supportTerrainModifiers.put(entry.getKey(), entry.getValue());
                } else {
                    supportTerrainModifiers.put(entry.getKey(),
                            supportTerrainModifiers.get(entry.getKey()).add(entry.getValue()));
                }
            }
        }
        // main stats
        for (Unit battalion : battalions) {
            BattalionMult battalionMult = battalionMults.stream()
                    .filter(m -> {
                        for (String category : battalion.getCategories()) {
                            if (m.getCategory().equals(category))
                                return true;
                        }
                        return false;
                    }).findFirst()
                    .orElse(null);
            if (battalionMult == null)
                battalionMult = new BattalionMult();
            Map<MultType, Double> multipliers = battalionMult.getMultipliers();
            // base
            divStatsDTO.setHp(
                    divStatsDTO.getHp() + BattalionMult.apply(multipliers, battalion.getHp(), "max_strength"));
            orgSum += BattalionMult.apply(multipliers, battalion.getOrg(), "max_organisation");
            recoveryRateSum += BattalionMult.apply(multipliers, battalion.getRecoveryRate(), "default_morale");
            divStatsDTO.setWeight(divStatsDTO.getWeight() + battalion.getWeight());
            divStatsDTO.setCombatWidth(divStatsDTO.getCombatWidth() + battalion.getCombatWidth());
            divStatsDTO.setSuppression(divStatsDTO.getSuppression()
                    + battalion.getSuppression() * (1 + factors.getOrDefault("suppresion", 0.0)));
            divStatsDTO.setSupplyConsumption(divStatsDTO.getSupplyConsumption()
                    + battalion.getSupplyConsumption() * (1 + factors.getOrDefault("supply_consumption", 0.0)));
            divStatsDTO.setRecon(divStatsDTO.getRecon() + battalion.getRecon());
            // find matching equipment
            List<Equipment> battalionEquipment = new ArrayList<>();
            Map<Equipment, Integer> battalionEquipmentMap = new HashMap<>();
            for (Map.Entry<String, Integer> entry : battalion.getEquipment().entrySet()) {
                Equipment archetype = equipmentList.stream().filter(eq -> eq.getId().equals(entry.getKey())).findFirst()
                        .orElse(null);
                List<Equipment> temp = equipmentList.stream().filter(eq -> !eq.isArchetype()
                        && eq.getArchetype().equals(archetype.getId()) && eq.getYear() <= div.getYear())
                        .collect(Collectors.toList());
                temp.stream().max((eq1, eq2) -> Integer.compare(eq1.getYear(), eq2.getYear())).ifPresent(eq -> {
                    Equipment upgraded = new Equipment(archetype);
                    upgraded.upgrade(eq);
                    System.out.println(upgraded.getId()+", "+upgraded.getSoftAttack());
                    battalionEquipment.add(upgraded);
                    battalionEquipmentMap.put(upgraded, entry.getValue());
                });
            }
            // stats from equipment
            for (Equipment eq : battalionEquipment) {
                if (divStatsDTO.getMaximumSpeed() > eq.getMaximumSpeed() && eq.getMaximumSpeed() > 0) {
                    divStatsDTO.setMaximumSpeed(eq.getMaximumSpeed());
                }
                divStatsDTO.setSoftAttack(divStatsDTO.getSoftAttack() + BattalionMult.apply(multipliers,
                        eq.getSoftAttack(), "soft_attack"));
                divStatsDTO.setHardAttack(divStatsDTO.getHardAttack() + BattalionMult.apply(multipliers,
                        eq.getHardAttack(), "hard_attack"));
                divStatsDTO.setAirAttack(divStatsDTO.getAirAttack() + BattalionMult.apply(multipliers,
                        eq.getAirAttack(), "air_attack"));
                double currApAttack = BattalionMult.apply(multipliers,
                        eq.getApAttack(), "ap_attack");
                apAttackSum += currApAttack;
                apAttackMax = currApAttack > apAttackMax ? currApAttack : apAttackMax;
                divStatsDTO.setDefense(divStatsDTO.getDefense() + BattalionMult.apply(multipliers,
                        eq.getDefense(), "defense"));
                divStatsDTO.setBreakthrough(divStatsDTO.getBreakthrough() + BattalionMult.apply(multipliers,
                        eq.getBreakthrough(), "breakthrough"));
                hardnessSum += eq.getHardness();
                double currArmor = BattalionMult.apply(multipliers,
                        eq.getArmorValue(), "armor_value");
                armorSum += currArmor;
                armorMax = currArmor > armorMax ? currArmor : armorMax;
                divStatsDTO.setFuelConsumption(divStatsDTO.getFuelConsumption()
                        + eq.getFuelConsumption() * (1 + factors.getOrDefault("fuel_consumption", 0.0)));
            }
            // cost
            for (Map.Entry<Equipment, Integer> entry : battalionEquipmentMap.entrySet()) {
                divStatsDTO.setBuildCostIc(
                        divStatsDTO.getBuildCostIc() + entry.getKey().getBuildCostIc() * entry.getValue());
                divStatsDTO.addEquipment(entry.getKey().getId(), entry.getValue());
            }
            divStatsDTO.setManpower(divStatsDTO.getManpower() + battalion.getManpower());
            if (battalion.getTrainingTime() > divStatsDTO.getTrainingTime())
                divStatsDTO.setTrainingTime(battalion.getTrainingTime());
            // terrain
            for (Map.Entry<TerrainType, TerrainModifier> entry : battalion.getTerrainModifiers().entrySet()) {
                if (battalionsTerrainModifiersSum.get(entry.getKey()) == null) {
                    battalionsTerrainModifiersSum.put(entry.getKey(), entry.getValue());
                } else {
                    battalionsTerrainModifiersSum.put(entry.getKey(),
                            battalionsTerrainModifiersSum.get(entry.getKey()).add(entry.getValue()));
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
        divStatsDTO.setExpLoss(factors.getOrDefault("experience_loss", 0.0));
        divStatsDTO.setEquipmentCapture(factors.getOrDefault("equipment_capture", 0.0));
        divStatsDTO.setReliabilityBonus(factors.getOrDefault("reliability", 0.0));
        // The net adjuster for a division is the average of its combat battalions
        // plus the sum of the adjusters of its support battalions.
        for (Map.Entry<TerrainType, TerrainModifier> entry : battalionsTerrainModifiersSum.entrySet()) {
            TerrainModifier battalionAverage = entry.getValue().divide(battalionCount);
            TerrainModifier supportBonus = supportTerrainModifiers.get(entry.getKey());
            if (supportBonus != null) {
                divisionTerrainModifiers.put(entry.getKey(), battalionAverage.add(supportBonus));
                continue;
            }
            divisionTerrainModifiers.put(entry.getKey(), battalionAverage);
        }
        divStatsDTO.setTerrainModifiers(divisionTerrainModifiers);
        return divStatsDTO;
    }
}
