package de.adesso.excel2jira.jira;

import de.adesso.excel2jira.jira.domain.JiraIssueListWrapper;
import de.adesso.excel2jira.jira.domain.Priority;
import de.adesso.excel2jira.jira.domain.User;
import de.adesso.excel2jira.jira.domain.project.Project;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@FeignClient(name = "JiraClient", url = "placeholder")
public interface JiraClient {

    @GetMapping("/project")
    List<Project> getProjects(URI baseUrl, @RequestHeader("Authorization") String auth);

    @GetMapping("/project/{id}")
    Project getProject(URI baseUrl, @RequestHeader("Authorization") String auth, @PathVariable(name = "id") Long id);

    @GetMapping("/user?username={username}")
    User getUser(URI baseUrl, @RequestHeader("Authorization") String auth, @PathVariable(name = "username") String username);

    @PostMapping("/issue/bulk")
    void createIssues(URI baseUrl, @RequestHeader("Authorization") String auth, @RequestBody JiraIssueListWrapper issues);

    @GetMapping("/priority")
    List<Priority> getPriorities(URI baseUrl, @RequestHeader("Authorization") String auth);
}
