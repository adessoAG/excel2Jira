package de.adesso.excel2jira;

import de.adesso.excel2jira.excel.domain.Issue;
import lombok.Getter;

public class UnableToMapIssueException extends Exception {

    @Getter
    private Issue issue;

    public UnableToMapIssueException(String s, Issue issue) {
        super(s);
        this.issue = issue;
    }
}
