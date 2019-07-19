package de.adesso.excel2jira.jira.domain.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Fix version class that maps to incoming JSON.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FixVersion {
    private Long id;
    private String name;
}
