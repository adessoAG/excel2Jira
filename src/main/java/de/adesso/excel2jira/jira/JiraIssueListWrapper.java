package de.adesso.excel2jira.jira;

import de.adesso.excel2jira.jira.domain.JiraIssue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JiraIssueListWrapper {

    private List<JiraIssue> issueUpdates = new ArrayList<>();
}
