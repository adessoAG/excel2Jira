package de.adesso.excel2jira.jira.domain.project;

import lombok.Data;

/**
 * Fix version class that maps to incoming JSON.
 */
@Data
public class FixVersion {
    private Long id;
    private String name;
}
