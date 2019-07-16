package de.adesso.excel2jira.jira.domain;

import lombok.Data;

import java.util.List;

@Data
public class IssueFields {

    //TODO: wrap as JSON "project": {"id": 10}
    private Long projectId;

    private String summary;

    //TODO: wrap as JSON "issueType": {"id": 10}
    private Long issueType;

    //TODO: wrap as JSON "assignee": {"name": "hombergs"}
    private String assignee;

    //TODO: wrap as JSON "priority": {"id": 10}
    private Long priority;

    private List<String> labels;

    //TODO: wrap as JSON "versions": [{"id": 10}]

    private List<Long> versions;

    private String description;
}
