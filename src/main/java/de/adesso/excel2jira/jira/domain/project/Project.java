package de.adesso.excel2jira.jira.domain.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    private Long id;
    private String key;
    private String name;
    private List<IssueType> issueTypes = new ArrayList<>();
    private List<FixVersion> versions = new ArrayList<>();
}
