package com.hofnarrxx.hoi4_unit_stats_api.parser.tech_parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.hofnarrxx.hoi4_unit_stats_api.parser.Parser;
import com.hofnarrxx.hoi4_unit_stats_api.parser.TerrainModifier;
import com.hofnarrxx.hoi4_unit_stats_api.parser.TerrainType;
import com.hofnarrxx.hoi4_unit_stats_api.parser.unit_parser.BattalionMult;
import com.hofnarrxx.hoi4_unit_stats_api.parser.unit_parser.MultType;

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
                    String[] nextLineParts = nextLine.split("=", 2);
                    if (nextLine.startsWith("battalion_mult")) {
                        BattalionMult bm = new BattalionMult();
                        MultType mt = new MultType();
                        while (i < lines.length) {
                            nextLine = lines[i++].trim();
                            if (nextLine.isEmpty() || nextLine.startsWith("#")
                                    || nextLine.startsWith("display_as_percentage"))
                                continue;
                            if (nextLine.equals("}"))
                                break;
                            if (nextLine.startsWith("add")) {
                                mt.setAdditive(true);
                                continue;
                            }
                            nextLineParts = nextLine.split("=", 2);
                            String keyBM = nextLineParts[0].trim();
                            String valueBM = nextLineParts[1].trim();
                            if (keyBM.equals("category")) {
                                bm.setCategory(valueBM);
                            } else {
                                mt.setStat(keyBM);
                                bm.addMultiplier(mt, Double.parseDouble(valueBM));
                            }
                        }
                        tech.addMult(key, bm);
                    } else if (TerrainType.isValidTerrain(nextLineParts[0].trim())) {
                        String terrainType = nextLineParts[0].trim().toUpperCase();
                        double attack = 0, defense = 0, movement = 0;
                        while (i < lines.length) {
                            nextLine = lines[i++].trim();
                            //System.out.println(id+", "+key+", "+nextLine);
                            if (nextLine.isEmpty() || nextLine.startsWith("#"))
                                continue;
                            if (nextLine.equals("}"))
                                break;
                            nextLineParts = nextLine.split("=", 2);
                            if (nextLineParts[0].trim().equals("attack")) {
                                attack = Double.parseDouble(nextLineParts[1].trim());
                                System.out.println("modified attack, "+id+", "+key+", "+nextLineParts[1].trim());
                            } else if (nextLineParts[0].trim().equals("defense") || nextLineParts[0].trim().equals("defence")) {
                                defense = Double.parseDouble(nextLineParts[1].trim());
                                System.out.println("modified defense, "+id+", "+key+", "+nextLineParts[1].trim());
                            } else {
                                movement = Double.parseDouble(nextLineParts[1].trim());
                            }
                        }
                        tech.addTerrainModifier(key,new TerrainModifier(TerrainType.valueOf(terrainType),attack, defense, movement));
                    } else {
                        String[] effectParts = nextLine.split("=", 2);
                        try {
                            effects.put(effectParts[0].trim(), Double.parseDouble(effectParts[1].trim()));
                        } catch (NumberFormatException ignored) {
                            System.out.println("non-numeric value");
                        }
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
        return tech;
    }
}
