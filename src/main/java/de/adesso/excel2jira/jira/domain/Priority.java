package de.adesso.excel2jira.jira.domain;

import lombok.Data;

/**
 * Priority class that maps to incoming JSON.
 */
@Data
public class Priority {
    private Long id;
    private String name;
}
