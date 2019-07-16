package de.adesso.excel2jira.jira.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.adesso.excel2jira.jira.JiraIssueJsonSerializer;
import lombok.Data;

@Data
@JsonSerialize(using = JiraIssueJsonSerializer.class)
public class JiraIssue {
    private IssueFields fields = new IssueFields();
}
