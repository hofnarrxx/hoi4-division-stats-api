package com.hofnarrxx.hoi4_unit_stats_api.parser.tech_parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.hofnarrxx.hoi4_unit_stats_api.parser.Parser;

@Component
public class TechParser extends Parser {
    private static final Set<String> SKIP_BLOCKS = Set.of(
            "enable_equipments",
            "enable_subunits",
            "enable_equipment_modules",
            "allow_branch",
            "path",
            "folder",
            "on_research_complete",
            "on_research_complete_limit",
            "ai_will_do",
            "special_project_specialization",
            "sub_technologies",
            "categories",
            "dependencies",
            "if",
            "xor");

    public static List<Technology> parseTechnologies(String input) {
        List<Technology> techList = new ArrayList<>();
        input = removeComments(input);

        String technologiesBlock = extractBlock("technologies", input);
        Map<String, String> techBlocks = splitTopLevelBlocks(technologiesBlock);

        for (Map.Entry<String, String> entry : techBlocks.entrySet()) {
            String id = entry.getKey();
            String block = entry.getValue();
            Technology tech = parseSingleTechnology(id, block);
            if (tech != null) {
                techList.add(tech);
            }
        }

        return techList;
    }

    private static Technology parseSingleTechnology(String id, String block) {
        Technology tech = new Technology(id);

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
            if (value.equals("{")) {
                if (SKIP_BLOCKS.contains(key)) {
                    // int startIndex = block.indexOf("{", block.indexOf(line));
                    // int endIndex = findMatchingBrace(block, startIndex);
                    // i += countLines(block.substring(startIndex, endIndex));
                    // continue;
                    int depth = 1;
                    while (i + 1 < lines.length && depth > 0) {
                        i++;
                        String inner = lines[i].trim();
                        if (inner.contains("{"))
                            depth++;
                        if (inner.contains("}"))
                            depth--;
                    }
                    continue;
                }
                if (key.equals("allow")) {
                    return null;
                }
                i++;
                HashMap<String, Double> effects = new HashMap<>();
                while (i < lines.length) {
                    String nextLine = lines[i++].trim();
                    if (nextLine.isEmpty() || nextLine.startsWith("#"))
                        continue;
                    if (nextLine.equals("}"))
                        break;
                    System.out.println(nextLine);
                    String[] effectParts = nextLine.split("=", 2);
                    try {
                        effects.put(effectParts[0].trim(), Double.parseDouble(effectParts[1].trim()));
                    } catch (NumberFormatException ignored) {
                        System.out.println("non-numeric value");
                    }
                }
                tech.addEffect(key, effects);
            } else {
                // Assign values
                switch (key) {
                    case "start_year" -> tech.setYear(Integer.parseInt(value));
                    default -> {
                    } // Ignore unneeded keys
                }
            }
        }
        System.out.println("end");
        return tech;
    }
}
