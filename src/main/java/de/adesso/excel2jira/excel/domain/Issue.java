package de.adesso.excel2jira.excel.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an issue in excel format.
 */
@Data
@NoArgsConstructor
public class Issue implements Serializable {
    private String projectName;
    private String summary;
    private String issueType;
    private String priority;
    private String assignee;
    private List<String> fixVersions = new ArrayList<>();
    private String description;
    private List<String> labels = new ArrayList<>();

    public String toString(){
        return String.format("[ %s ] [ %s ] [ %s ] [ %s ] [ %s ]", getProjectName(), getSummary(), getIssueType(), getPriority(), getAssignee());
    }
}
