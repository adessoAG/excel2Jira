package de.adesso.excel2jira.jira;

import de.adesso.excel2jira.excel.domain.Issue;
import de.adesso.excel2jira.jira.domain.JiraIssue;
import de.adesso.excel2jira.jira.domain.Priority;
import de.adesso.excel2jira.jira.domain.project.FixVersion;
import de.adesso.excel2jira.jira.domain.project.IssueType;
import de.adesso.excel2jira.jira.domain.project.Project;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JiraIssueMapper {

    private static Logger logger = LoggerFactory.getLogger(JiraIssueMapper.class);

    private static HashMap<String, Project> cachedProjects = new HashMap<>();
    private static List<String> cachedUsers = new ArrayList<>();

    /**
     * Private constructor to hide the default constructor
     */
    private JiraIssueMapper(){}

    /**
     * Maps a list of Issue objects to a list of JiraIssue objects.
     * @param jiraClient An instance of a jira client, needed to get project and users from the server.
     * @param issues A list of Issue objects to map.
     * @param auth The auth token required to communicate with the server.
     * @return A list of JiraIssue objects, that can be directly uploaded to JIRA.
     * @throws UnableToMapIssueException Thrown if an error occurs during the mapping. (Name does not map to id)
     * @throws AuthorizationException Thrown if the supplied credentials are invalid.
     */
    public static List<JiraIssue> map(URI url, JiraClient jiraClient, List<Issue> issues, String auth) throws UnableToMapIssueException, AuthorizationException {

        List<Project> projects;
        try {
            projects = jiraClient.getProjects(url, auth);
        } catch (FeignException.Unauthorized e){
            throw new AuthorizationException("Username or password wrong!");
        }
        List<Priority> priorities = jiraClient.getPriorities(url, auth);

        List<JiraIssue> resultList = new ArrayList<>();
        for(Issue issue : issues){
            JiraIssue jiraIssue = new JiraIssue();
            String projectName = issue.getProjectName();
            Project issueProject = null;
            for(Project project : projects){
                if(project.getName().equals(projectName)) {
                    if (cachedProjects.containsKey(projectName)) {
                        issueProject = cachedProjects.get(projectName);
                    } else {
                        issueProject = jiraClient.getProject(url, auth, project.getId());
                        cachedProjects.put(issueProject.getName(), issueProject);
                    }
                    break;
                }
            }
            if(issueProject == null){
                throw new UnableToMapIssueException("No project with name " + issue.getProjectName() + " found!", issue);
            }
            jiraIssue.setProjectId(issueProject.getId());
            jiraIssue.setDescription(issue.getDescription());
            jiraIssue.setIssueType(getIssueType(issue, issueProject));
            jiraIssue.setVersions(getFixVersions(issue, issueProject));
            jiraIssue.setPriority(getPriorities(issue, priorities));
            jiraIssue.setLabels(issue.getLabels());
            jiraIssue.setSummary(issue.getSummary());

            //Set assignee
            if(issue.getAssignee().equals("Nicht zugewiesen") || issue.getAssignee().equals("Not assigned")) {
                jiraIssue.setAssignee(null);
            }else{
                try {
                    if(cachedUsers.contains(issue.getAssignee())){
                        jiraIssue.setAssignee(issue.getAssignee());
                    }else{
                        jiraIssue.setAssignee(jiraClient.getUser(url, auth, issue.getAssignee()).getKey());
                        cachedUsers.add(jiraIssue.getAssignee());
                    }
                } catch (FeignException.NotFound e) {
                    throw new UnableToMapIssueException("No user with name " + issue.getAssignee() + " found!", issue);
                }
            }
            logger.info(String.format("Item: %s processed successfully", issue.toString()));
            resultList.add(jiraIssue);
        }
        return resultList;
    }

    /**
     * Maps a priority name to it's id on the JIRA server.
     * @param issue The issue to check.
     * @param priorities A list of available priorities.
     * @return The id corresponding to the name.
     * @throws UnableToMapIssueException Thrown if the name cannot be mapped to an id.
     */
    private static Long getPriorities(Issue issue, List<Priority> priorities) throws UnableToMapIssueException {
        for(Priority priority : priorities){
            if(issue.getPriority().equals(priority.getName())){
                return priority.getId();
            }
        }
        throw new UnableToMapIssueException("No priority with name " +issue.getPriority() + " found", issue);
    }

    /**
     * Maps a list of fixVersion names to their corresponding ids on the jira server.
     * @param issue The issue to check.
     * @param issueProject The project to look in.
     * @return A list of fixVersion ids.
     * @throws UnableToMapIssueException Thrown if a fixVersion is not found.
     */
    private static List<Long> getFixVersions(Issue issue, Project issueProject) throws UnableToMapIssueException {
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
                throw new UnableToMapIssueException("No fixVersion with name " + fixVersionString + " found in project " + issueProject.getName(), issue);
            }
        }
        return fixVersionIds;
    }

    /**
     * Return The id of the issue type corresponding to the issue type name.
     * @param issue The issue to check.
     * @param project The project to look in.
     * @return The id of the issue type.
     * @throws UnableToMapIssueException Thrown if the issue type is not found.
     */
    private static Long getIssueType(Issue issue, Project project) throws UnableToMapIssueException {
        for(IssueType issueType : project.getIssueTypes()){
            if (issueType.getName().equals(issue.getIssueType())) {
                return issueType.getId();
            }
        }
        throw new UnableToMapIssueException("No issueType with name " + issue.getIssueType() + " found in project " + project.getName(), issue);
    }
}
