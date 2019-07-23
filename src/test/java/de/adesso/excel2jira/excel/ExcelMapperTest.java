package de.adesso.excel2jira.excel;

import de.adesso.excel2jira.excel.domain.Issue;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.List;
import java.util.Objects;

public class ExcelMapperTest {

    private final String TEST_FILE_PATH = Objects.requireNonNull(this.getClass().getClassLoader().getResource("issues-examples.xlsx")).getPath();
    private final String TEST_FILE_NO_DESCRIPTION = Objects.requireNonNull(this.getClass().getClassLoader().getResource("issues-no-description.xlsx")).getPath();
    private final String TEST_FILE_NO_FIX_VERSIONS = Objects.requireNonNull(this.getClass().getClassLoader().getResource("issues-no-fix-versions.xlsx")).getPath();
    private final String TEST_FILE_NO_LABELS = Objects.requireNonNull(this.getClass().getClassLoader().getResource("issues-no-labels.xlsx")).getPath();
    private final String TEST_FILE_EMPTY_PROJECT = Objects.requireNonNull(this.getClass().getClassLoader().getResource("issues-no-project.xlsx")).getPath();
    private final String TEST_FILE_EMPTY_SUMMARY = Objects.requireNonNull(this.getClass().getClassLoader().getResource("issues-no-summary.xlsx")).getPath();
    private final String TEST_FILE_EMPTY_TYPE = Objects.requireNonNull(this.getClass().getClassLoader().getResource("issues-no-type.xlsx")).getPath();
    private final String TEST_FILE_EMPTY_PRIORITY = Objects.requireNonNull(this.getClass().getClassLoader().getResource("issues-no-priority.xlsx")).getPath();
    private final String TEST_FILE_EMPTY_ASSIGNEE = Objects.requireNonNull(this.getClass().getClassLoader().getResource("issues-no-assignee.xlsx")).getPath();

    @Test
    public void parseFileSuccessfully() {
        List<Issue> issues = ExcelMapper.map(TEST_FILE_PATH);

        Assertions.assertThat(issues).hasSize(3);

        Issue firstIssue = issues.get(0);
        Issue secondIssue = issues.get(1);
        Issue thirdIssue = issues.get(2);

        Assertions.assertThat(firstIssue.getPriority()).isEqualTo("3 - leicht umgehbar");
        Assertions.assertThat(firstIssue.getLabels()).hasSize(3);
        Assertions.assertThat(firstIssue.getAssignee()).isEqualTo("hombergs");
        Assertions.assertThat(firstIssue.getIssueType()).isEqualTo("Feature");
        Assertions.assertThat(firstIssue.getPriority()).isEqualTo("3 - leicht umgehbar");
        Assertions.assertThat(firstIssue.getProjectName()).isEqualTo("Open Source Team");
        Assertions.assertThat(firstIssue.getFixVersions().get(0)).isEqualTo("Standort 4711");
        Assertions.assertThat(firstIssue.getDescription()).isEqualTo("Bitte das folgende Szenario evaluieren.");

        Assertions.assertThat(secondIssue.getLabels()).hasSize(2);
        Assertions.assertThat(thirdIssue.getLabels()).hasSize(1);
        Assertions.assertThat(secondIssue.getSummary()).isEqualTo("DEV-Blog");
        Assertions.assertThat(thirdIssue.getSummary()).isEqualTo("Eureka-Support für Prometheus");
    }

    @Test
    public void parseFileSuccessfullyWithEmptyDescription() {
        List<Issue> issues = ExcelMapper.map(TEST_FILE_NO_DESCRIPTION);

        Assertions.assertThat(issues).hasSize(3);

        Issue firstIssue = issues.get(0);
        Issue secondIssue = issues.get(1);
        Issue thirdIssue = issues.get(2);

        Assertions.assertThat(firstIssue.getPriority()).isEqualTo("3 - leicht umgehbar");
        Assertions.assertThat(firstIssue.getLabels()).hasSize(3);
        Assertions.assertThat(firstIssue.getAssignee()).isEqualTo("hombergs");
        Assertions.assertThat(firstIssue.getIssueType()).isEqualTo("Feature");
        Assertions.assertThat(firstIssue.getPriority()).isEqualTo("3 - leicht umgehbar");
        Assertions.assertThat(firstIssue.getProjectName()).isEqualTo("Open Source Team");
        Assertions.assertThat(firstIssue.getFixVersions().get(0)).isEqualTo("Standort 4711");
        Assertions.assertThat(firstIssue.getDescription()).isEmpty();

        Assertions.assertThat(secondIssue.getLabels()).hasSize(2);
        Assertions.assertThat(thirdIssue.getLabels()).hasSize(1);
        Assertions.assertThat(secondIssue.getSummary()).isEqualTo("DEV-Blog");
        Assertions.assertThat(thirdIssue.getSummary()).isEqualTo("Eureka-Support für Prometheus");
    }

    @Test
    public void parseFileSuccessfullyWithEmptyFixVersions() {
        List<Issue> issues = ExcelMapper.map(TEST_FILE_NO_FIX_VERSIONS);

        Assertions.assertThat(issues).hasSize(3);

        Issue firstIssue = issues.get(0);
        Issue secondIssue = issues.get(1);
        Issue thirdIssue = issues.get(2);

        Assertions.assertThat(firstIssue.getPriority()).isEqualTo("3 - leicht umgehbar");
        Assertions.assertThat(firstIssue.getLabels()).hasSize(3);
        Assertions.assertThat(firstIssue.getAssignee()).isEqualTo("hombergs");
        Assertions.assertThat(firstIssue.getIssueType()).isEqualTo("Feature");
        Assertions.assertThat(firstIssue.getPriority()).isEqualTo("3 - leicht umgehbar");
        Assertions.assertThat(firstIssue.getProjectName()).isEqualTo("Open Source Team");
        Assertions.assertThat(firstIssue.getFixVersions()).isEmpty();
        Assertions.assertThat(firstIssue.getDescription()).isEqualTo("Bitte das folgende Szenario evaluieren.");

        Assertions.assertThat(secondIssue.getLabels()).hasSize(2);
        Assertions.assertThat(thirdIssue.getLabels()).hasSize(1);
        Assertions.assertThat(secondIssue.getSummary()).isEqualTo("DEV-Blog");
        Assertions.assertThat(thirdIssue.getSummary()).isEqualTo("Eureka-Support für Prometheus");
    }

    @Test
    public void parseFileSuccessfullyWithEmptyLabels() {
        List<Issue> issues = ExcelMapper.map(TEST_FILE_NO_LABELS);

        Assertions.assertThat(issues).hasSize(3);

        Issue firstIssue = issues.get(0);
        Issue secondIssue = issues.get(1);
        Issue thirdIssue = issues.get(2);

        Assertions.assertThat(firstIssue.getPriority()).isEqualTo("3 - leicht umgehbar");
        Assertions.assertThat(firstIssue.getLabels()).isEmpty();
        Assertions.assertThat(firstIssue.getAssignee()).isEqualTo("hombergs");
        Assertions.assertThat(firstIssue.getIssueType()).isEqualTo("Feature");
        Assertions.assertThat(firstIssue.getPriority()).isEqualTo("3 - leicht umgehbar");
        Assertions.assertThat(firstIssue.getProjectName()).isEqualTo("Open Source Team");
        Assertions.assertThat(firstIssue.getFixVersions().get(0)).isEqualTo("Standort 4711");
        Assertions.assertThat(firstIssue.getDescription()).isEqualTo("Bitte das folgende Szenario evaluieren.");

        Assertions.assertThat(secondIssue.getLabels()).isEmpty();
        Assertions.assertThat(thirdIssue.getLabels()).isEmpty();
        Assertions.assertThat(secondIssue.getSummary()).isEqualTo("DEV-Blog");
        Assertions.assertThat(thirdIssue.getSummary()).isEqualTo("Eureka-Support für Prometheus");
    }

    @Test(expected = UnableToParseFileException.class)
    public void parsingFailsWhenProjectEmpty() {
        ExcelMapper.map(TEST_FILE_EMPTY_PROJECT);
    }

    @Test(expected = UnableToParseFileException.class)
    public void parsingFailsWhenSummaryEmpty() {
        ExcelMapper.map(TEST_FILE_EMPTY_SUMMARY);
    }

    @Test(expected = UnableToParseFileException.class)
    public void parsingFailsWhenTypeEmpty() {
        ExcelMapper.map(TEST_FILE_EMPTY_TYPE);
    }

    @Test(expected = UnableToParseFileException.class)
    public void parsingFailsWhenPriorityEmpty() {
        ExcelMapper.map(TEST_FILE_EMPTY_PRIORITY);
    }

    @Test(expected = UnableToParseFileException.class)
    public void parsingFailsWhenAssigneeEmpty() {
        ExcelMapper.map(TEST_FILE_EMPTY_ASSIGNEE);
    }

    @Test
    public void shouldThrowExceptionWhenFileDoesNotExist() {
        Assertions.assertThatThrownBy(() -> ExcelMapper.map("incorrect-file-path")).isInstanceOf(UnableToParseFileException.class);
    }
}
