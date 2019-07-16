package de.adesso.excel2jira.jira;

import de.adesso.excel2jira.jira.domain.JiraIssue;
import de.adesso.excel2jira.jira.domain.Priority;
import de.adesso.excel2jira.jira.domain.Project;
import de.adesso.excel2jira.jira.domain.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "JiraClient", url = "https://jira.adesso.de/rest/api/2/")
public interface JiraClient {

    @GetMapping("/project")
    List<Project> getProjects(@RequestHeader("Authorization") String auth);

    @GetMapping("/project/{id}")
    Project getProject(@RequestHeader("Authorization") String auth, @PathVariable(name = "id") Long id);

    @GetMapping("/user?username={username}")
    User getUser(@RequestHeader("Authorization") String auth, @PathVariable(name = "username") String username);

    @PostMapping("/issue/bulk")
    String createIssues(@RequestHeader("Authorization") String auth, @RequestBody JiraIssueListWrapper issues);


    @GetMapping("/priority")
    List<Priority> getPriorities(@RequestHeader("Authorization") String auth);
}
