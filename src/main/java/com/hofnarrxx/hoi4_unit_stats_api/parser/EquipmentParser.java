package com.hofnarrxx.hoi4_unit_stats_api.parser;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EquipmentParser {
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

    private static Map<String, Integer> parseKeyValueIntBlock(String block) {
        Map<String, Integer> map = new HashMap<>();
        String[] lines = block.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (!line.contains("="))
                continue;
            String[] parts = line.split("=", 2);
            map.put(parts[0].trim(), Integer.parseInt(parts[1].trim()));
        }
        return map;
    }

    private static Map<String, String> splitTopLevelBlocks(String input) {
        // Map<String, String> map = new LinkedHashMap<>();
        // Pattern pattern = Pattern.compile("(\\w+)\\s*=\\s*\\{");
        // Matcher matcher = pattern.matcher(block);

        // while (matcher.find()) {
        // String key = matcher.group(1);
        // int start = matcher.end() - 1;
        // int end = findMatchingBrace(block, start);
        // String subBlock = block.substring(start + 1, end).trim();
        // map.put(key, subBlock);
        // }
        // return map;
        Map<String, String> blocks = new LinkedHashMap<>();
        int i = 0;
        while (i < input.length()) {
            // Skip whitespace
            while (i < input.length() && Character.isWhitespace(input.charAt(i)))
                i++;

            // Find the key
            int keyStart = i;
            while (i < input.length() && (Character.isLetterOrDigit(input.charAt(i)) || input.charAt(i) == '_'))
                i++;
            if (i == keyStart)
                break; // no more keys

            String key = input.substring(keyStart, i).trim();

            // Skip spaces and '='
            while (i < input.length() && (input.charAt(i) == ' ' || input.charAt(i) == '='))
                i++;

            // Expecting a block now
            if (i >= input.length() || input.charAt(i) != '{')
                continue;

            int braceStart = i;
            int braceEnd = findMatchingBrace(input, braceStart);
            if (braceEnd == -1)
                break;

            String blockContent = input.substring(braceStart + 1, braceEnd).trim();
            blocks.put(key, blockContent);
            i = braceEnd + 1;
        }
        return blocks;
    }

    private static int findMatchingBrace(String text, int openIndex) {
        int level = 1;
        for (int i = openIndex + 1; i < text.length(); i++) {
            if (text.charAt(i) == '{')
                level++;
            else if (text.charAt(i) == '}')
                level--;
            if (level == 0)
                return i;
        }
        return -1;
    }

    private static String extractBlock(String key, String input) {
        Pattern pattern = Pattern.compile(key + "\\s*=\\s*\\{");
        Matcher matcher = pattern.matcher(input);
        if (!matcher.find())
            return "";
        int start = matcher.end() - 1;
        int end = findMatchingBrace(input, start);
        return input.substring(start + 1, end).trim();
    }

    private static String removeComments(String input) {
        return input.replaceAll("(?m)#.*$", ""); // removes everything after # on each line
    }
}