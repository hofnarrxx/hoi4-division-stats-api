package com.hofnarrxx.hoi4_unit_stats_api.parser;

import java.util.*;

import org.springframework.stereotype.Component;

@Component
public class ModuleParser extends Parser{
    public static List<Module> parseModules(String input) {
        List<Module> moduleList = new ArrayList<>();
        input = removeComments(input);

        String moduleBlock = extractBlock("equipment_modules", input);
        Map<String, String> moduleBlocks = splitTopLevelBlocks(moduleBlock);

        for (Map.Entry<String, String> entry : moduleBlocks.entrySet()) {
            String id = entry.getKey();
            String block = entry.getValue();
            if(id.equals("limit")) continue;
            Module mod = parseSingleModule(id, block);
            moduleList.add(mod);
            if(id.equals("expanded_fuel_tank")) break;
        }

        return moduleList;
    }

    private static Module parseSingleModule(String id, String block) {
        Module mod = new Module(id);

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
            if (line.startsWith("build_cost_resources")) {
                continue;
            }
            if (key.equals("add_stats") && value.equals("{")) {
                Map<String, Double> addStats = new HashMap<>();
                i++;
                while (i < lines.length) {
                    String nextLine = lines[i++].trim();
                    if (nextLine.startsWith("#") || nextLine.isBlank())
                        continue;
                    if (nextLine.equals("}"))
                        break;
                    String modParts[] = nextLine.split("=", 2);
                    addStats.put(modParts[0].trim(), Double.parseDouble(modParts[1].trim()));
                }
                mod.setAddStats(addStats);
            } else if (key.equals("multiply_stats") && value.equals("{")) {
                Map<String, Double> multiplyStats = new HashMap<>();
                i++;
                while (i < lines.length) {
                    String nextLine = lines[i++].trim();
                    if (nextLine.startsWith("#") || nextLine.isBlank())
                        continue;
                    if (nextLine.equals("}"))
                        break;
                    String modParts[] = nextLine.split("=", 2);
                    multiplyStats.put(modParts[0].trim(), Double.parseDouble(modParts[1].trim()));
                }
                mod.setMultiplyStats(multiplyStats);
            } else {
                // Assign values
                switch (key) {
                    case "abbreviation" -> mod.setId(value.replace("\"", ""));
                    case "category" -> mod.setCategory(value);
                    default -> {
                    } // Ignore unneeded keys
                }
            }
        }

        return mod;
    }
}