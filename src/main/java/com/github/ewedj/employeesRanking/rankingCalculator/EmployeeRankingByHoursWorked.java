package com.github.ewedj.employeesRanking.rankingCalculator;

import com.github.ewedj.employeesRanking.model.WorkCollector;
import com.github.ewedj.employeesRanking.model.WorkEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class EmployeeRankingByHoursWorked implements Ranking {

    private static final Logger log = LoggerFactory.getLogger(EmployeeRankingByHoursWorked.class);

    private final Map<String, Double> ranking = new LinkedHashMap<>();

    public EmployeeRankingByHoursWorked() {}

    public void calculate(WorkCollector workCollector) {
        var entrySet = workCollector.getWorkEntriesGroupByEmployee().entrySet();
        var temporaryEmployeeHoursMap = new HashMap<String, Double>();

        for (Map.Entry<String, List<WorkEntry>> employeeWorkEntries : entrySet) {
            double hours = 0;

            for (WorkEntry workEntry : employeeWorkEntries.getValue()) {
                hours += workEntry.hours();
            }

            temporaryEmployeeHoursMap.put(employeeWorkEntries.getKey(), hours);

        }

        log.debug("Before sort: {}",  temporaryEmployeeHoursMap);

        temporaryEmployeeHoursMap
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(entry -> ranking.put(entry.getKey(), entry.getValue()));

        log.debug("After sort: {}", ranking);
    }

    public void printRanking() {
        System.out.println("Ranking of Employees by Total Worked Hours:");
        int i = 1;
        for (Map.Entry<String, Double> entry : ranking.entrySet()) {
            System.out.println(i + ". " + entry.getKey() + ": " + entry.getValue() + " hours");
            i++;
        }
    }
}