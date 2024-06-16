package com.github.ewedj.employeesRanking.rankingCalculator;

import com.github.ewedj.employeesRanking.model.WorkCollector;
import com.github.ewedj.employeesRanking.model.WorkEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Top10DaysRankingByHoursWorked implements Ranking {

    private static final Logger log = LoggerFactory.getLogger(Top10DaysRankingByHoursWorked.class);
    private static final int TOP_10_LIMIT = 10;

    private final Map<LocalDate, Double> ranking = new LinkedHashMap<>();
    private final DateTimeFormatter dateTimeFormatter;

    public Top10DaysRankingByHoursWorked(Locale locale) {
        dateTimeFormatter = DateTimeFormatter.ofPattern(
                "d MMMM yyyy", locale
        );
    }

    public void calculate(WorkCollector workCollector) {
        var entrySet = workCollector.getWorkEntriesGroupByEmployee().entrySet();
        var temporaryDateHoursMap = new HashMap<LocalDate, Double>();

        for (Map.Entry<String, List<WorkEntry>> employeeWorkEntries : entrySet) {
            for (WorkEntry workEntry : employeeWorkEntries.getValue()) {
                LocalDate date = workEntry.date();

                temporaryDateHoursMap.merge(date, workEntry.hours(), Double::sum);
            }
        }

        log.debug("Before sort: {}",  temporaryDateHoursMap);
        temporaryDateHoursMap
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(TOP_10_LIMIT)
                .forEachOrdered(entry -> ranking.put(entry.getKey(), entry.getValue()));

        log.debug("After sort: {}", ranking);
    }

    public void printRanking() {
        System.out.println("Ranking of Days in the Month and Year by Total Hours Worked:");
        int i = 1;
        for (Map.Entry<LocalDate, Double> entry : ranking.entrySet()) {
            System.out.println(i + ". " + entry.getKey().format(dateTimeFormatter) + ": " + entry.getValue() + " hours");
            i++;
        }
    }
}