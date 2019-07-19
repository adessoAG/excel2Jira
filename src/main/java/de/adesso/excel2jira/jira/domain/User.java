package de.adesso.excel2jira.jira.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User class that maps to incoming JSON.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String key;
}
