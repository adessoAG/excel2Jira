package de.adesso.excel2jira.jira.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.adesso.excel2jira.jira.JiraIssueJsonSerializer;
import lombok.Data;

import java.util.List;

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
