package de.adesso.excel2jira.excel;

import de.adesso.excel2jira.UnableToParseFileException;
import de.adesso.excel2jira.excel.domain.Issue;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Reads an Excel table and maps it to a List o {@link Issue} objects
 */
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
    public static List<Issue> map(String filename) throws UnableToParseFileException {
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

                String projectName = dataFormatter.formatCellValue(row.getCell(firstCellNum++)).trim();
                if (projectName.isEmpty()) {
                    throw new UnableToParseFileException(String.format("Project name in row %d must not be empty!", row.getRowNum() + 1));
                } else {
                    issue.setProjectName(projectName);
                }

                String summary = dataFormatter.formatCellValue(row.getCell(firstCellNum++)).trim();
                if (summary.isEmpty()) {
                    throw new UnableToParseFileException(String.format("Summary in row %d must not be empty!", row.getRowNum() + 1));
                } else {
                    issue.setSummary(summary);
                }

                String issueType = dataFormatter.formatCellValue(row.getCell(firstCellNum++)).trim();
                if (issueType.isEmpty()) {
                    throw new UnableToParseFileException(String.format("Issue type in row %d must not be empty!", row.getRowNum() + 1));
                } else {
                    issue.setIssueType(issueType);
                }

                String priority = dataFormatter.formatCellValue(row.getCell(firstCellNum++)).trim();
                if (priority.isEmpty()) {
                    throw new UnableToParseFileException(String.format("Priority in row %d must not be empty!", row.getRowNum() + 1));
                } else {
                    issue.setPriority(priority);
                }

                String assignee = dataFormatter.formatCellValue(row.getCell(firstCellNum++)).trim();
                if (assignee.isEmpty()) {
                    throw new UnableToParseFileException(String.format("Assignee in row %d must not be empty!", row.getRowNum() + 1));
                } else {
                    issue.setAssignee(assignee);
                }

                issue.setFixVersions(getListFromString(dataFormatter.formatCellValue(row.getCell(firstCellNum++))));
                issue.setDescription(dataFormatter.formatCellValue(row.getCell(firstCellNum++)).trim());
                issue.setLabels(getListFromString(dataFormatter.formatCellValue(row.getCell(firstCellNum))));

                issues.add(issue);
            }

            workbook.close();
            return issues;
        } catch (IOException | IllegalStateException e) {
            throw new UnableToParseFileException(String.format("File %s not found or is in use by another process!", filename));
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
