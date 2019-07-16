package de.adesso.excel2jira.excel;

import de.adesso.excel2jira.excel.domain.Issue;

import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExcelMapper {

    public List<Issue> map(String filename) throws IOException {
        List<Issue> issues = new ArrayList<>();
        Workbook workbook = WorkbookFactory.create(new File(filename));
        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();

        int numberOfRows = sheet.getPhysicalNumberOfRows();

        for (int i = 1; i < numberOfRows - 1; i++) {
            Row row = sheet.getRow(i);
            Issue issue = new Issue();

            issue.setProjectName(dataFormatter.formatCellValue(row.getCell(0)));
            issue.setSummary(dataFormatter.formatCellValue(row.getCell(1)));
            issue.setIssueType(dataFormatter.formatCellValue(row.getCell(2)));
            issue.setPriority(dataFormatter.formatCellValue(row.getCell(3)));
            issue.setAssignee(dataFormatter.formatCellValue(row.getCell(4)));
            issue.setFixVersions(getListFromString(dataFormatter.formatCellValue(row.getCell(5))));
            issue.setDescription(dataFormatter.formatCellValue(row.getCell(6)));
            issue.setLabels(getListFromString(dataFormatter.formatCellValue(row.getCell(7))));

            issues.add(issue);
        }

        workbook.close();
        return issues;
    }

    private List<String> getListFromString(String cellValue) {
        List<String> strings = new ArrayList<>(Arrays.asList(cellValue.split(",")));
        strings.removeIf(String::isEmpty);
        strings.replaceAll(String::trim);
        return strings;
    }
}
