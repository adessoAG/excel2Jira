package de.adesso.excel2jira.jira.domain.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * IssueType class that maps to incoming JSON.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssueType {
    private Long id;
    private String name;
}
