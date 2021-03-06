package de.adesso.excel2jira.jira;

import de.adesso.excel2jira.excel.domain.Issue;
import lombok.Getter;

public class UnableToMapIssueException extends Exception {

    @Getter
    private final Issue issue;

    public UnableToMapIssueException(String s, Issue issue) {
        super(s);
        this.issue = issue;
    }
}
