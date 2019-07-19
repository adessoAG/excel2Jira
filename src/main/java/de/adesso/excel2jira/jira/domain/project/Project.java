package de.adesso.excel2jira.jira.domain.project;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Project {
    private Long id;
    private String key;
    private String name;
    private List<IssueType> issueTypes = new ArrayList<>();
    private List<FixVersion> versions = new ArrayList<>();
}
