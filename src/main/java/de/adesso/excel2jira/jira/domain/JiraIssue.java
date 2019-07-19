package de.adesso.excel2jira.jira.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.adesso.excel2jira.jira.JiraIssueJsonSerializer;
import lombok.Data;

import java.util.List;

/**
 * This class maps directly to the JSON that JIRA expects for creating an Issue.
 * It is serialized by {@link JiraIssueJsonSerializer}
 */
@Data
@JsonSerialize(using = JiraIssueJsonSerializer.class)
public class JiraIssue {
    private Long projectId;

    private String summary;

    private Long issueType;

    private String assignee;

    private Long priority;

    private List<String> labels;

    private List<Long> versions;

    private String description;
}
