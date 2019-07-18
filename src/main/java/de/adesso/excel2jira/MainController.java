package de.adesso.excel2jira;

import de.adesso.excel2jira.excel.ExcelMapper;
import de.adesso.excel2jira.excel.domain.Issue;
import de.adesso.excel2jira.jira.JiraClient;
import de.adesso.excel2jira.jira.JiraIssueListWrapper;
import de.adesso.excel2jira.jira.domain.*;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MainController implements ApplicationRunner {

    private Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private JiraClient jiraClient;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        /*if (!args.containsOption("--file")) {
            logger.error("No filepath specified in command line arguments.");
            return;
        }
        if (!args.containsOption("--url")) {
            logger.error("No url specified in command line arguments.");
            return;
        }

        String filepath = args.getOptionValues("--file").get(0);
        String url = args.getOptionValues("--url").get(0);*/

        //Map xlsx file to a List of Issues
        String path = "C:\\Users\\kkrause\\Desktop\\issues.xlsx";
        List<Issue> issues = new ExcelMapper().map(path);

        //Get the available projects for the user
        String usernameAndPassword = "atanasov:/testForJira123";

        String auth = "Basic " + new String(Base64.encodeBase64(usernameAndPassword.getBytes()));
        List<Project> projects = jiraClient.getProjects(auth);

        //Map the issues to a list of Jira issues
        List<JiraIssue> jiraIssues = mapToJiraIssues(issues, projects, auth);

        //Send the issues off to JIRA
        jiraClient.createIssues(auth, new JiraIssueListWrapper(jiraIssues));
    }

    private List<JiraIssue> mapToJiraIssues(List<Issue> issues, List<Project> projects, String auth) throws UnableToMapIssueException {
        List<JiraIssue> resultList = new ArrayList<>();
        for(Issue issue : issues){
            JiraIssue jiraIssue = new JiraIssue();
            String projectName = issue.getProjectName();
            Project issueProject = null;
            for(Project project : projects){
                if(project.getName().equals(projectName)){
                    issueProject = jiraClient.getProject(auth, project.getId());
                    break;
                }
            }
            if(issueProject == null){
                throw new UnableToMapIssueException("No project with name " + issue.getProjectName() + " found!");
            }
            jiraIssue.getFields().setProjectId(issueProject.getId());

            //Set the description
            jiraIssue.getFields().setDescription(issue.getDescription());

            //Set the issueTypeId
            boolean issueTypeIdFound = false;
            for(IssueType issueType : issueProject.getIssueTypes()){
                if (issueType.getName().equals(issue.getIssueType())) {
                    jiraIssue.getFields().setIssueType(issueType.getId());
                    issueTypeIdFound = true;
                    break;
                }
            }
            if(!issueTypeIdFound){
                throw new UnableToMapIssueException("No issueType with name " + issue.getIssueType() + " found in project " + projectName);
            }

            //Set the fix versions
            List<Long> fixVersionIds = new ArrayList<>();
            for(String fixVersionString : issue.getFixVersions()){
                boolean fixVersionFound = false;
                for(FixVersion fixVersion : issueProject.getVersions()){
                    if(fixVersion.getName().equals(fixVersionString)){
                        fixVersionFound = true;
                        fixVersionIds.add(fixVersion.getId());
                        break;
                    }
                }
                if(!fixVersionFound){
                    throw new UnableToMapIssueException("No fixVersion with name " + fixVersionString + " found in project " + projectName);
                }
            }
            jiraIssue.getFields().setVersions(fixVersionIds);


            //Set priorities
            boolean priorityFound = false;
            List<Priority> priorities = jiraClient.getPriorities(auth);
            for(Priority priority : priorities){
                if(issue.getPriority().equals(priority.getName())){
                    priorityFound = true;
                    jiraIssue.getFields().setPriority(priority.getId());
                    break;
                }
            }
            if(!priorityFound){
                throw new UnableToMapIssueException("No priority with name " + issue.getPriority() + " found");
            }

            //Set User
            if(jiraClient.getUser(auth, issue.getAssignee()).getKey() != null){
                jiraIssue.getFields().setAssignee(issue.getAssignee());
            }else{
                throw new UnableToMapIssueException("No user with name " + issue.getAssignee() + " found");
            }

            //Set Labels
            jiraIssue.getFields().setLabels(issue.getLabels());

            //Set Summary
            jiraIssue.getFields().setSummary(issue.getSummary());
            resultList.add(jiraIssue);
        }

        return resultList;
    }
}
