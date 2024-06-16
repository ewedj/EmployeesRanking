package com.github.ewedj.employeesRanking.rankingCalculator;

import com.github.ewedj.employeesRanking.model.WorkCollector;

public interface Ranking {
    void calculate(WorkCollector workCollector);
    void printRanking();
}
