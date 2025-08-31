package com.hofnarrxx.hoi4_unit_stats_api.parser;

import java.util.*;

import org.springframework.stereotype.Component;

@Component
public class UnitParser extends Parser {
    public static List<Unit> parseUnits(String input) {
        List<Unit> unitList = new ArrayList<>();
        input = removeComments(input);

        String unitsBlock = extractBlock("sub_units", input);
        Map<String, String> unitBlocks = splitTopLevelBlocks(unitsBlock);

        for (Map.Entry<String, String> entry : unitBlocks.entrySet()) {
            String name = entry.getKey();
            String block = entry.getValue();
            if (name.equals("fake_intel_unit"))
                continue;
            Unit unit = parseSingleUnit(name, block);
            unitList.add(unit);
        }

        return unitList;
    }

    private static Unit parseSingleUnit(String name, String block) {
        Unit unit = new Unit(name);

        String[] lines = block.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty())
                continue;

            String[] parts = line.split("=", 2);
            if (parts.length < 2)
                continue;

            String key = parts[0].trim();
            String value = parts[1].trim();
            // if (line.startsWith("resources")) {
            // continue;
            // }
            if (key.equals("type") && value.equals("{")) {
                // Parse block
                ArrayList<String> types = new ArrayList<>();
                i++; // move to next line
                while (i < lines.length) {
                    String nextLine = lines[i++].trim();
                    if (nextLine.startsWith("#"))
                        continue;
                    if (nextLine.equals("}"))
                        break;
                    types.add(nextLine.split("#")[0].trim()); // strip inline comments
                }
                unit.setType(types);
            } else if (key.equals("need") && value.equals("{")) {
                i++;
                while (i < lines.length) {
                    String nextLine = lines[i].trim();
                    if (nextLine.startsWith("#"))
                        continue;
                    if (nextLine.equals("}"))
                        break;
                    i++;
                    String[] eqParts = nextLine.split("=", 2);
                    unit.addEquipment(eqParts[0].trim(), Integer.parseInt(eqParts[1].trim()));
                }
            } else if ((key.equals("forest") || key.equals("hills") || key.equals("mountain") || key.equals("jungle") ||
                    key.equals("marsh") || key.equals("fort") || key.equals("river") || key.equals("amphibious") ||
                    key.equals("plains") || key.equals("desert")) && value.equals("{")) {
                double attack = 0, defense = 0, movement = 0;
                i++;
                while (i < lines.length) {
                    String nextLine = lines[i++].trim();
                    if (nextLine.startsWith("#"))
                        continue;
                    if (nextLine.equals("}"))
                        break;
                    String[] terrainParts = nextLine.split("=", 2);
                    if (terrainParts[0].equals("attack")) {
                        attack = Double.parseDouble(terrainParts[1].trim());
                    } else if (terrainParts[0].equals("defense")) {
                        defense = Double.parseDouble(terrainParts[1].trim());
                    } else {
                        movement = Double.parseDouble(terrainParts[1].trim());
                    }
                    unit.addTerrainModifier(TerrainType.valueOf(key.toUpperCase()),
                            new TerrainModifier(attack, defense, movement));
                }
            } else {
                // Assign values
                switch (key) {
                    case "group" -> unit.setGroup(value);
                    case "abbreviation" -> unit.setId(value.replace("\"", ""));
                    case "same_support_type" -> unit.setSupportTypeBlock(value);
                    case "combat_width" -> unit.setCombatWidth(Double.parseDouble(value));
                    case "max_strength" -> unit.setHp(Double.parseDouble(value));
                    case "max_organisation" -> unit.setOrg(Double.parseDouble(value));
                    case "default_morale" -> unit.setRecoveryRate(Double.parseDouble(value));
                    case "manpower" -> unit.setManpower(Integer.parseInt(value));
                    case "training_time" -> unit.setTrainingTime(Integer.parseInt(value));
                    case "suppression" -> unit.setSuppression(Double.parseDouble(value));
                    case "weight" -> unit.setWeight(Double.parseDouble(value));
                    case "supply_consumption" -> unit.setSupplyConsumption(Double.parseDouble(value));
                    case "experience_loss_factor" -> unit.setExpLoss(Double.parseDouble(value));
                    case "reliability_factor" -> unit.setReliability(Double.parseDouble(value));
                    case "recon" -> unit.setRecon(Double.parseDouble(value));
                    case "initiative" -> unit.setInitiative(Double.parseDouble(value));
                    case "entrenchment" -> unit.setEntrenchment(Double.parseDouble(value));
                    case "equipment_capture_factor" -> unit.setEquipmentCapture(Double.parseDouble(value));
                    // support modifiers
                    case "defense", "soft_attack", "hard_attack", "ap_attack", "air_attack", "breakthrough",
                            "armor_value" ->
                        unit.addSupportModifier(key, Double.parseDouble(value));
                    default -> {
                    } // Ignore unneeded keys
                }
            }
        }
        return unit;
    }
}