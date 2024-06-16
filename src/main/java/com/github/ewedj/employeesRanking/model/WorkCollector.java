package com.github.ewedj.employeesRanking.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkCollector {


    private static final Logger log = LoggerFactory.getLogger(WorkCollector.class);

    private final Map<String, List<WorkEntry>> workEntryHashMap = new HashMap<String, List<WorkEntry>>();

    public void add(List<WorkEntry> employeeWorkEntries) {
        for (WorkEntry workEntry : employeeWorkEntries) {
            var employee = workEntry.employee();
            if (!workEntryHashMap.containsKey(employee)) {
                workEntryHashMap.put(employee, new ArrayList<WorkEntry>());
            }
            workEntryHashMap.get(employee).add(workEntry);
        }
    }

    public Map<String, List<WorkEntry>> getWorkEntriesGroupByEmployee() {
        return workEntryHashMap;
    }
}
