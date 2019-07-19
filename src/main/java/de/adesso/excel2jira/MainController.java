package de.adesso.excel2jira;

import de.adesso.excel2jira.excel.ExcelMapper;
import de.adesso.excel2jira.excel.domain.Issue;
import de.adesso.excel2jira.jira.JiraClient;
import de.adesso.excel2jira.jira.JiraIssueMapper;
import de.adesso.excel2jira.jira.domain.JiraIssue;
import de.adesso.excel2jira.jira.domain.JiraIssueListWrapper;
import de.adesso.excel2jira.jira.domain.Priority;
import de.adesso.excel2jira.jira.domain.project.Project;
import feign.FeignException;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class MainController implements ApplicationRunner {

    private Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private JiraClient jiraClient;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (!args.containsOption("file")) {
            logger.error("No filepath specified in command line arguments.");
            return;
        }
        if(!args.containsOption("username")){
            logger.error("No username specified in command line arguments.");
            return;
        }
        if(!args.containsOption("password")){
            logger.error("No password specified in command line arguments.");
            return;
        }
        if(!args.containsOption("url")){
            logger.error("No url specified in command line arguments.");
            return;
        }

        String filepath = args.getOptionValues("file").get(0);
        String url = args.getOptionValues("url").get(0);
        String username = args.getOptionValues("username").get(0);
        String password = args.getOptionValues("password").get(0);
        URI determinedBasePathUri = URI.create("https://" + url + "/rest/api/2/");

        //Map xlsx file to a List of Issues
        List<Issue> issues;
        try {
            issues = ExcelMapper.map(filepath);
        } catch (UnableToParseFileException e){
            logger.error(String.format("File %s not found!", filepath));
            return;
        }

        if(args.containsOption("fixVersions")) {
            List<String> versions = new ArrayList<>(Arrays.asList(args.getOptionValues("fixVersions").get(0).split(",")));
            versions.replaceAll(String::trim);
            for (Issue issue : issues) {
                issue.getFixVersions().addAll(versions);
            }
        }

        //Get the available projects for the user
        String usernameAndPassword = username + ":" + password;
        String auth = "Basic " + new String(Base64.encodeBase64(usernameAndPassword.getBytes()));

        List<Project> projects;
        try {
            projects = jiraClient.getProjects(determinedBasePathUri, auth);
        } catch (FeignException.Unauthorized e){
            logger.error("Username or password wrong!");
            return;
        }
        List<Priority> priorities = jiraClient.getPriorities(determinedBasePathUri, auth);

        List<JiraIssue> jiraIssues;
        try {
            jiraIssues = JiraIssueMapper.map(determinedBasePathUri, jiraClient, issues, projects,priorities, auth);
        }catch (UnableToMapIssueException e){
            logger.error(e.getMessage());
            return;
        }

        //Send the issues off to JIRA
        jiraClient.createIssues(determinedBasePathUri, auth, new JiraIssueListWrapper(jiraIssues));
    }
}
