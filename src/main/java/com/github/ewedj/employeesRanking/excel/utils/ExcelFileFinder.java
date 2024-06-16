package com.github.ewedj.employeesRanking.excel.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class ExcelFileFinder {

    private static final Logger log = LoggerFactory.getLogger(ExcelFileFinder.class);
    private static final String EXCEL_EXTENSION = ".xls";

    private ExcelFileFinder() {}

    public static List<Path> listAllExcelFiles(Path path) throws IOException {
        List<Path> result;

        try (Stream<Path> stream = Files.walk(path)) {
            result = stream
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(EXCEL_EXTENSION))
                    .toList();
        }

        log.debug("Found {} files in path: {}. List: {}", result.size(), path, result);

        return result;
    }
}