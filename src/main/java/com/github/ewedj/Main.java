package com.github.ewedj;


import com.github.ewedj.employeesRanking.excel.reader.EmployeeWorkReader;
import com.github.ewedj.employeesRanking.excel.reader.EmployeeWorkReaderException;
import com.github.ewedj.employeesRanking.excel.utils.ExcelFileFinder;
import com.github.ewedj.employeesRanking.model.WorkCollector;
import com.github.ewedj.employeesRanking.rankingCalculator.EmployeeRankingByHoursWorked;
import com.github.ewedj.employeesRanking.rankingCalculator.MonthRankingByHoursWorked;
import com.github.ewedj.employeesRanking.rankingCalculator.Ranking;
import com.github.ewedj.employeesRanking.rankingCalculator.Top10DaysRankingByHoursWorked;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private static final String LOG_GROUP_NAME = Main.class.getPackageName();

    public static void main(String[] args) {
        Locale locale = Locale.US;
        String directoryPath = null;

        Options options = new Options();
        options.addOption("h", "help", false, "Show help");
        options.addOption("d", "debug", false, "Enable debug logging");
        options.addOption("l", "locale", true, "Specify locale (e.g., PL, EN) for ranking list output if applicable");
        options.addOption("p", "path", true, "Specify directory path that contains timesheet for employees (subdirectories included)");

        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("help")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp(" ", options);
                return;
            }

            if (cmd.hasOption("debug")) {
                ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(LOG_GROUP_NAME);
                logger.setLevel(ch.qos.logback.classic.Level.DEBUG);
            }

            if (cmd.hasOption("locale")) {
                String localeStr = cmd.getOptionValue("locale");
                locale = Locale.of(localeStr);
            }

            if (cmd.hasOption("path")) {
                directoryPath = cmd.getOptionValue("path");
            } else {
                throw new RuntimeException("Path is required!");
            }

        } catch (ParseException|RuntimeException e) {
            System.err.println("Unexpected exception. Reason: " + e.getMessage());
            System.exit(1);
        }

        processRanking(directoryPath, locale);
    }

    private static void processRanking(String directoryPath, Locale locale) {
        WorkCollector workCollector = new WorkCollector();
        try {
            var workbooksPath = Paths.get(directoryPath);
            var excelFiles = ExcelFileFinder.listAllExcelFiles(workbooksPath);

            for (Path path : excelFiles) {
                var employeeWorkEntries = EmployeeWorkReader.readEmployeeWork(path);
                workCollector.add(employeeWorkEntries);
            }

        } catch (InvalidPathException | IOException | EmployeeWorkReaderException e) {
            log.error("Unable to process workbook due to:", e);
        }


        Ranking employeeRankingByHoursWorked = new EmployeeRankingByHoursWorked();
        employeeRankingByHoursWorked.calculate(workCollector);
        employeeRankingByHoursWorked.printRanking();
        System.out.println();

        Ranking monthRankingByHoursWorked = new MonthRankingByHoursWorked(locale);
        monthRankingByHoursWorked.calculate(workCollector);
        monthRankingByHoursWorked.printRanking();
        System.out.println();

        Ranking top10daysRankingByHoursWorked = new Top10DaysRankingByHoursWorked(locale);
        top10daysRankingByHoursWorked.calculate(workCollector);
        top10daysRankingByHoursWorked.printRanking();
    }
}