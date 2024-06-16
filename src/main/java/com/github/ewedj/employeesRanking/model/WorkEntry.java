package com.github.ewedj.employeesRanking.model;

import java.time.LocalDate;

/**
 * Record used for storing Employee work on particular project and task
 */
public record WorkEntry(String project, String employee, LocalDate date, double hours, String task) {}
