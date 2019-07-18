package de.adesso.excel2jira.excel;

import de.adesso.excel2jira.UnableToParseFileException;
import de.adesso.excel2jira.excel.domain.Issue;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExcelMapper {

    /**
     * Private constructor to hide the default one.
     */
    private ExcelMapper (){
    }

    /**
     *
     * @param filename path of the file to parse
     * @return list of issue objects that will be uploaded in jira
     */
    public static List<Issue> map(String filename) {
        List<Issue> issues = new ArrayList<>();

        try {
            Workbook workbook = WorkbookFactory.create(new File(filename));

            //The first sheet in the .xlsx file is the one which contains the jira issues
            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter dataFormatter = new DataFormatter();
            int firstRowNum = sheet.getFirstRowNum();
            int lastRowNum = sheet.getLastRowNum();

            // iterate from the firstRowNum + 1, because the first row contains the headings of issue fields
            for (int i = firstRowNum + 1; i <= lastRowNum; i++) {
                Row row = sheet.getRow(i);
                int firstCellNum = row.getFirstCellNum();
                Issue issue = new Issue();

                issue.setProjectName(dataFormatter.formatCellValue(row.getCell(firstCellNum++)));
                issue.setSummary(dataFormatter.formatCellValue(row.getCell(firstCellNum++)));
                issue.setIssueType(dataFormatter.formatCellValue(row.getCell(firstCellNum++)));
                issue.setPriority(dataFormatter.formatCellValue(row.getCell(firstCellNum++)));
                issue.setAssignee(dataFormatter.formatCellValue(row.getCell(firstCellNum++)));
                issue.setFixVersions(getListFromString(dataFormatter.formatCellValue(row.getCell(firstCellNum++))));
                issue.setDescription(dataFormatter.formatCellValue(row.getCell(firstCellNum++)));
                issue.setLabels(getListFromString(dataFormatter.formatCellValue(row.getCell(firstCellNum))));

                issues.add(issue);
            }

            workbook.close();
            return issues;
        } catch (IOException | IllegalStateException e) {
            throw new UnableToParseFileException(e.getMessage());
        }
    }

    /**
     *
     * @param cellValue the string value in the cell, not yet separated
     * @return List of string values, separated by ','
     */
    private static List<String> getListFromString(String cellValue) {
        List<String> strings = new ArrayList<>(Arrays.asList(cellValue.split(",")));
        strings.removeIf(String::isEmpty);
        strings.replaceAll(String::trim);
        return strings;
    }
}
