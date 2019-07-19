package de.adesso.excel2jira.jira.domain.project;

import lombok.Data;

/**
 * IssueType class that maps to incoming JSON.
 */
@Data
public class IssueType {
    private Long id;
    private String name;
}
