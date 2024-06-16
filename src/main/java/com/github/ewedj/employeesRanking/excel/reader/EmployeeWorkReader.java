package com.github.ewedj.employeesRanking.excel.reader;

import com.github.ewedj.employeesRanking.model.WorkEntry;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Class used for reading Employee work timesheet
 */
public class EmployeeWorkReader {

    private static final Logger log = LoggerFactory.getLogger(EmployeeWorkReader.class);
    private static final int DATE_CELL = 0;
    private static final int TASK_CELL = 1;
    private static final int HOURS_CELL = 2;

    private EmployeeWorkReader() {
    }

    public static List<WorkEntry> readEmployeeWork(Path timesheetPath) throws EmployeeWorkReaderException {
        List<WorkEntry> workEntries = new ArrayList<>();

        String employeeName = timesheetPath.getFileName().toString().replace(".xls", "");

        log.debug("Processing file for employeeName: {}", employeeName);

        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(timesheetPath.toFile());

//                Workbook employeeTimesheet = new XSSFWorkbook(fileInputStream);
            Workbook employeeTimesheet = null;


            employeeTimesheet = new HSSFWorkbook(fileInputStream);


            for (Sheet sheet : employeeTimesheet) {
                String projectName = sheet.getSheetName();

                log.debug("Processing project: {} for employeeName: {}", projectName, employeeName);

                for (Row row : sheet) {
                    var rowNum = row.getRowNum();
                    if (rowNum == 0 || isRowEmpty(row)) {
                        continue; //skipping headers
                    }

                    var date = row.getCell(DATE_CELL);
                    var task = row.getCell(TASK_CELL);
                    var taskHours = row.getCell(HOURS_CELL);

                    validateCell(date, "Data");
                    validateCell(task, "Zadanie");
                    validateCell(taskHours, "Czas [h]");

                    log.debug("Found task: {}, at: {}, took: {} [h], project: {}", task, date, taskHours, projectName);

                    workEntries.add(
                            new WorkEntry(
                                    projectName,
                                    employeeName,
                                    date.getLocalDateTimeCellValue().toLocalDate(),
                                    taskHours.getNumericCellValue(),
                                    task.getStringCellValue()
                            )
                    );
                }
            }
            employeeTimesheet.close();
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            throw new EmployeeWorkReaderException("Unable to open/close Employee timesheet due to:" + e.getMessage(), e);
        } catch (IOException e) {
            throw new EmployeeWorkReaderException("Unable to read/close workbook due to:" + e.getMessage(), e);
        }

        return workEntries;
    }

    private static void validateCell(Cell cell, String cellIdentifier) throws EmployeeWorkReaderException {
        if (cell == null) {
            throw new EmployeeWorkReaderException("Unexpected error, cell " + cellIdentifier
                    + " is null! Please correct the employee timesheet and run again application.");
        }
    }

    private static boolean isRowEmpty(Row row) {
        if (row == null || row.getLastCellNum() <= 0) {
            return true;
        }

        for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
            Cell cell = row.getCell(cellNum);
            if (cell != null && StringUtils.isNotBlank(cell.toString())) {
                return false;
            }
        }
        return true;

    }
}
