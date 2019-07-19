package de.adesso.excel2jira;

import de.adesso.excel2jira.excel.ExcelMapper;
import de.adesso.excel2jira.excel.UnableToParseFileException;
import de.adesso.excel2jira.excel.domain.Issue;
import de.adesso.excel2jira.jira.AuthorizationException;
import de.adesso.excel2jira.jira.JiraClient;
import de.adesso.excel2jira.jira.JiraIssueMapper;
import de.adesso.excel2jira.jira.UnableToMapIssueException;
import de.adesso.excel2jira.jira.domain.JiraIssue;
import de.adesso.excel2jira.jira.domain.JiraIssueListWrapper;
import feign.FeignException;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.Console;
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
    public void run(ApplicationArguments args) {

        if (args.getNonOptionArgs().contains("help")) {
            logHelp();
            return;
        }
        if (!args.containsOption("file")) {
            logger.error("No filepath specified in command line arguments.");
            logHelp();
            return;
        }
        if(!args.containsOption("username")){
            logger.error("No username specified in command line arguments.");
            logHelp();
            return;
        }
        if(!args.containsOption("url")){
            logger.error("No url specified in command line arguments.");
            logHelp();
            return;
        }

        String filepath = args.getOptionValues("file").get(0);
        String url = args.getOptionValues("url").get(0);
        String username = args.getOptionValues("username").get(0);
        URI determinedBasePathUri = URI.create("https://" + url + "/rest/api/2/");

        System.out.print("Please enter your JIRA account password: ");
        Console console = System.console();
        String password = new String(console.readPassword());

        //Map xlsx file to a List of Issues
        List<Issue> issues;
        try {
            issues = ExcelMapper.map(filepath);
        } catch (UnableToParseFileException e){
            logger.error(e.getMessage());
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

        List<JiraIssue> jiraIssues;
        try {
            jiraIssues = JiraIssueMapper.map(determinedBasePathUri, jiraClient, issues, auth);
        }catch (UnableToMapIssueException e){
            logger.error(String.format("Error processing item: %s Error is: %s", e.getIssue().toString(), e.getMessage()));
            logger.error("Aborting! No issues have been created!");
            return;
        } catch (AuthorizationException e) {
            logger.error(e.getMessage());
            return;
        }

        //Send the issues off to JIRA
        try {
            jiraClient.createIssues(determinedBasePathUri, auth, new JiraIssueListWrapper(jiraIssues));
            logger.error("All issues successfully created!");
        } catch (FeignException.BadRequest e){
            if(e.contentUTF8().contains("labels")){
                logger.error("The server does not support creating labels! Please remove the labels and try again!");
                logger.error("Aborting! No issues have been created!");
            }
        }
    }

    private void logHelp(){
        logger.info("\nUsage:\n" +
                "--file=\"filename.xlsx\"               The Excel sheet to parse\n" +
                "--username=\"user\"                    The JIRA account username\n" +
                "--url=\"jira.company.domain\"          The JIRA server top level domain\n" +
                "--fixVersions=\"version1, version2\"   A comma-separated list of fix versions\n");
    }
}
