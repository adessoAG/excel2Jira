package de.adesso.excel2jira.jira.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Priority class that maps to incoming JSON.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Priority {
    private Long id;
    private String name;
}
