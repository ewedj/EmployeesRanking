package com.github.ewedj.employeesRanking.excel.reader;

public class EmployeeWorkReaderException extends Exception {
    public EmployeeWorkReaderException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public EmployeeWorkReaderException(String errorMessage) {
        super(errorMessage);
    }
}
