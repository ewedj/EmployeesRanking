package com.github.ewedj.employeesRanking.rankingCalculator;

import com.github.ewedj.employeesRanking.model.WorkCollector;
import com.github.ewedj.employeesRanking.model.WorkEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.TextStyle;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MonthRankingByHoursWorked implements Ranking {

    private static final Logger log = LoggerFactory.getLogger(MonthRankingByHoursWorked.class);

    private final Map<String, Double> ranking = new LinkedHashMap<>();
    private final Locale defaultLocale;

    public MonthRankingByHoursWorked(Locale locale) {
        defaultLocale = locale;
    }

    public void calculate(WorkCollector workCollector) {
        var entrySet = workCollector.getWorkEntriesGroupByEmployee().entrySet();
        var temporaryMonthHoursMap = new HashMap<String, Double>();

        for (Map.Entry<String, List<WorkEntry>> employeeWorkEntries : entrySet) {
            for (WorkEntry workEntry : employeeWorkEntries.getValue()) {
                String month = workEntry.date().getMonth().getDisplayName(
                        TextStyle.FULL_STANDALONE,
                        defaultLocale
                );

                temporaryMonthHoursMap.merge(month, workEntry.hours(), Double::sum);
            }
        }

        log.debug("Before sort: {}",  temporaryMonthHoursMap);
        temporaryMonthHoursMap
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(entry -> ranking.put(entry.getKey(), entry.getValue()));

        log.debug("After sort: {}", ranking);
    }

    public void printRanking() {
        System.out.println("Ranking of Months by Total Hours Worked:");
        int i = 1;
        for (Map.Entry<String, Double> entry : ranking.entrySet()) {
            System.out.println(i + ". " + entry.getKey() + ": " + entry.getValue() + " hours");
            i++;
        }
    }
}
