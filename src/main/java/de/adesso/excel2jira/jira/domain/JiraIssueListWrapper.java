package de.adesso.excel2jira.jira.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


/**
 * Wraps a list of Jira issues in order to conform to the JSON form required by JIRA
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class JiraIssueListWrapper {

    private List<JiraIssue> issueUpdates = new ArrayList<>();
}
