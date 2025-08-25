package com.hofnarrxx.hoi4_unit_stats_api.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

@Component
public class FileUtil {
    private static String readFile(Path path) throws IOException {
        return Files.readString(path);
    }

    public static List<String> readFolder(String path) throws IOException {
        Path folderPath = Paths.get(path);
        List<String> filesContent = new ArrayList<>();
        try (Stream<Path> paths = Files.list(folderPath)) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(filePath -> {
                        try {
                            filesContent.add(readFile(filePath));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filesContent;
    }
}
