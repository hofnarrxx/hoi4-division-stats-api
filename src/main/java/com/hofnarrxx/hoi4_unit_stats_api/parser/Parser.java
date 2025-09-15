package com.hofnarrxx.hoi4_unit_stats_api.parser;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    protected static Map<String, Integer> parseKeyValueIntBlock(String block) {
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

    protected static Map<String, String> splitTopLevelBlocks(String input) {
        Map<String, String> blocks = new LinkedHashMap<>();
        int i = 0;
        while (i < input.length()) {
            // Skip whitespace
            while (i < input.length() && Character.isWhitespace(input.charAt(i)))
                i++;
            // Skip lines starting with '@'
            if (i < input.length() && input.charAt(i) == '@') {
                while (i < input.length() && input.charAt(i) != '\n' && input.charAt(i) != '\r')
                    i++;
                continue;
            }
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

    protected static int findMatchingBrace(String text, int openIndex) {
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

    protected static String extractBlock(String key, String input) {
        Pattern pattern = Pattern.compile(key + "\\s*=\\s*\\{");
        Matcher matcher = pattern.matcher(input);
        if (!matcher.find())
            return "";
        int start = matcher.end() - 1;
        int end = findMatchingBrace(input, start);
        return input.substring(start + 1, end).trim();
    }

    protected static String removeComments(String input) {
        return input.replaceAll("(?m)#.*$", ""); // removes everything after # on each line
    }

    protected static int countLines(String s) {
        return (int) s.lines().count();
    }
}
