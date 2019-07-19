package de.adesso.excel2jira.excel;

import de.adesso.excel2jira.UnableToParseFileException;
import de.adesso.excel2jira.excel.domain.Issue;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.List;

public class ExcelMapperTest {

    private static final String TEST_FILE_PATH = "./src/test/java/de/adesso/excel2jira/excel/issues-examples.xlsx";

    @Test
    public void parseFileSuccessfully() {
        List<Issue> issues = ExcelMapper.map(TEST_FILE_PATH);

        Assertions.assertThat(issues).hasSize(3);

        Issue firstIssue = issues.get(0);
        Issue secondIssue = issues.get(1);
        Issue thirdIssue = issues.get(2);

        Assertions.assertThat(firstIssue.getPriority()).isEqualTo("3 - leicht umgehbar");
        Assertions.assertThat(firstIssue.getLabels()).hasSize(3);
        Assertions.assertThat(secondIssue.getLabels()).hasSize(2);
        Assertions.assertThat(thirdIssue.getLabels()).hasSize(1);
        Assertions.assertThat(secondIssue.getSummary()).isEqualTo("DEV-Blog");
        Assertions.assertThat(thirdIssue.getSummary()).isEqualTo("Eureka-Support für Prometheus");
    }

    @Test
    public void shouldThrowExceptionWhenFileDoesNotExist() {
        Assertions.assertThatThrownBy(() -> ExcelMapper.map("incorrect-file-path")).isInstanceOf(UnableToParseFileException.class);
    }
}
