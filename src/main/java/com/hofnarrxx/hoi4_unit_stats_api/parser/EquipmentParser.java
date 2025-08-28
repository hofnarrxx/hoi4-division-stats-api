package com.hofnarrxx.hoi4_unit_stats_api.parser;

import java.util.*;

import org.springframework.stereotype.Component;

@Component
public class EquipmentParser extends Parser{
    public static List<Equipment> parseEquipments(String input) {
        List<Equipment> equipmentList = new ArrayList<>();
        input = removeComments(input);

        String equipmentsBlock = extractBlock("equipments", input);
        Map<String, String> equipmentBlocks = splitTopLevelBlocks(equipmentsBlock);

        for (Map.Entry<String, String> entry : equipmentBlocks.entrySet()) {
            String id = entry.getKey();
            String block = entry.getValue();
            Equipment eq = parseSingleEquipment(id, block);
            equipmentList.add(eq);
        }

        return equipmentList;
    }

    private static Equipment parseSingleEquipment(String id, String block) {
        Equipment eq = new Equipment(id);

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
            if (line.startsWith("resources")) {
                continue;
            }
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
                eq.setType(types);
            } else {
                // Assign values
                switch (key) {
                    case "year" -> eq.setYear(Integer.parseInt(value));
                    case "archetype" -> eq.setArchetype(value);
                    case "defense" -> eq.setDefense(Double.parseDouble(value));
                    case "soft_attack" -> eq.setSoftAttack(Double.parseDouble(value));
                    case "hard_attack" -> eq.setHardAttack(Double.parseDouble(value));
                    case "ap_attack" -> eq.setApAttack(Double.parseDouble(value));
                    case "air_attack" -> eq.setAirAttack(Double.parseDouble(value));
                    case "breakthrough" -> eq.setBreakthrough(Double.parseDouble(value));
                    case "reliability" -> eq.setReliability(Double.parseDouble(value));
                    case "build_cost_ic" -> eq.setBuildCostIc(Double.parseDouble(value));
                    case "maximum_speed" -> eq.setMaximumSpeed(Double.parseDouble(value));
                    case "armor_value" -> eq.setArmorValue(Double.parseDouble(value));
                    case "hardness" -> eq.setHardness(Double.parseDouble(value));
                    case "fuel_consumption" -> eq.setFuelConsumption(Double.parseDouble(value));
                    default -> {
                    } // Ignore unneeded keys
                }
            }
        }

        return eq;
    }
}