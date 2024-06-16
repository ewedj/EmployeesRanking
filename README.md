# Employees Ranking

## Overview

This program is designed to process and analyze work hour data from Excel files stored in a specified directory and its subdirectories. It provides valuable insights by generating and displaying various rankings based on the data. The main features of the program include:

- Employee Ranking by Hours Worked
- Monthly Ranking by Hours Worked
- Top 10 Most Productive Days

## Requirements

- Java 21
- Excel files with extension `.xls` (format from Excel 2003 and older)

## Usage

To run program execute following command with proper arguments:
`java -jar EmployeesRanking-1.0-SNAPSHOT-all.jar <OPTIONS>`

OPTIONS:

`-h` - Display help content

`-d` - Enable debug mode

`-p` - Specify directory path that contains timesheet for employees (subdirectories included)

`-l` - Specify locale (e.g., PL, EN) for ranking list output if applicable


## How to build jar

Execute following command:`./gradlew shadowJar`. The jar file will be created in build/libs directory. 