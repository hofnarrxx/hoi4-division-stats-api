package com.hofnarrxx.hoi4_unit_stats_api.parser;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnitParser {
    public static List<Unit> parseUnits(String input) {
        List<Unit> unitList = new ArrayList<>();
        input = removeComments(input);

        String unitsBlock = extractBlock("sub_units", input);
        Map<String, String> unitBlocks = splitTopLevelBlocks(unitsBlock);

        for (Map.Entry<String, String> entry : unitBlocks.entrySet()) {
            String name = entry.getKey();
            String block = entry.getValue();
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
                    String nextLine = lines[i++].trim();
                    if (nextLine.startsWith("#"))
                        continue;
                    if (nextLine.equals("}"))
                        break;
                    String[] eqParts = nextLine.split("=", 2);
                    unit.addEquipment(eqParts[0].trim(), Integer.parseInt(eqParts[1].trim()));
                }
            } else if ((key.equals("forest") || key.equals("hills") || key.equals("mountain") || key.equals("jungle") ||
                    key.equals("marsh") || key.equals("fort") || key.equals("river") || key.equals("amphibious") ||
                    key.equals("urban") || key.equals("desert")) && value.equals("{")) {
                double attack = 0, defense = 0, movement = 0;
                i++;
                while (i < lines.length) {
                    String nextLine = lines[i++].trim();
                    if (nextLine.startsWith("#"))
                        continue;
                    if (nextLine.equals("}"))
                        break;
                    System.out.println(nextLine);
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
                    case "abbreviation" -> unit.setId(value);
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
                    default -> {
                    } // Ignore unneeded keys
                }
            }
        }
        return unit;
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