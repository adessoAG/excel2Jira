package de.adesso.excel2jira.jira;

import de.adesso.excel2jira.UnableToMapIssueException;
import de.adesso.excel2jira.excel.domain.Issue;
import de.adesso.excel2jira.jira.domain.*;
import de.adesso.excel2jira.jira.domain.project.FixVersion;
import de.adesso.excel2jira.jira.domain.project.IssueType;
import de.adesso.excel2jira.jira.domain.project.Project;
import feign.FeignException;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class JiraIssueMapper {

    /**
     * Private constructor to hide the default constructor
     */
    private JiraIssueMapper(){}

    /**
     * Maps a list of Issue objects to a list of JiraIssue objects.
     * @param jiraClient An instance of a jira client, needed to get project and users from the server.
     * @param issues A list of Issue objects to map.
     * @param projects The projects available to the user with @auth
     * @param priorities The priorities available on the server
     * @param auth The auth token required to communicate with the server.
     * @return A list of JiraIssue objects, that can be directly uploaded to JIRA.
     * @throws UnableToMapIssueException Thrown if an error occurs during the mapping. (Name does not map to id)
     */
    public static List<JiraIssue> map(URI url, JiraClient jiraClient, List<Issue> issues, List<Project> projects, List<Priority> priorities, String auth) throws UnableToMapIssueException {
        List<JiraIssue> resultList = new ArrayList<>();
        for(Issue issue : issues){
            JiraIssue jiraIssue = new JiraIssue();
            String projectName = issue.getProjectName();
            Project issueProject = null;
            for(Project project : projects){
                if(project.getName().equals(projectName)){
                    issueProject = jiraClient.getProject(url, auth, project.getId());
                    break;
                }
            }
            if(issueProject == null){
                throw new UnableToMapIssueException("No project with name " + issue.getProjectName() + " found!");
            }
            jiraIssue.setProjectId(issueProject.getId());
            jiraIssue.setDescription(issue.getDescription());
            jiraIssue.setIssueType(getIssueType(issue.getIssueType(), issueProject));
            jiraIssue.setVersions(getFixVersions(issue.getFixVersions(), issueProject));
            jiraIssue.setPriority(getPriorities(issue.getPriority(), priorities));
            jiraIssue.setLabels(issue.getLabels());
            jiraIssue.setSummary(issue.getSummary());

            //Set assignee
            try {
                jiraIssue.setAssignee(jiraClient.getUser(url, auth, issue.getAssignee()).getKey());
            } catch (FeignException.NotFound e){
                jiraIssue.setAssignee(null);
            }
            resultList.add(jiraIssue);
        }
        return resultList;
    }

    /**
     * Maps a priority name to it's id on the JIRA server.
     * @param priorityName The name of the priority.
     * @param priorities A list of available priorities.
     * @return The id corresponding to the name.
     * @throws UnableToMapIssueException Thrown if the name cannot be mapped to an id.
     */
    private static Long getPriorities(String priorityName, List<Priority> priorities) throws UnableToMapIssueException {
        for(Priority priority : priorities){
            if(priorityName.equals(priority.getName())){
                return priority.getId();
            }
        }
        throw new UnableToMapIssueException("No priority with name " +priorityName + " found");
    }

    /**
     * Maps a list of fixVersion names to their corresponding ids on the jira server.
     * @param fixVersions The names of the fix versions.
     * @param issueProject The project to look in.
     * @return A list of fixVersion ids.
     * @throws UnableToMapIssueException Thrown if a fixVersion is not found.
     */
    private static List<Long> getFixVersions(List<String> fixVersions, Project issueProject) throws UnableToMapIssueException {
        List<Long> fixVersionIds = new ArrayList<>();
        for(String fixVersionString : fixVersions){
            boolean fixVersionFound = false;
            for(FixVersion fixVersion : issueProject.getVersions()){
                if(fixVersion.getName().equals(fixVersionString)){
                    fixVersionFound = true;
                    fixVersionIds.add(fixVersion.getId());
                    break;
                }
            }
            if(!fixVersionFound){
                throw new UnableToMapIssueException("No fixVersion with name " + fixVersionString + " found in project " + issueProject.getName());
            }
        }
        return fixVersionIds;
    }

    /**
     * Return The id of the issue type corresponding to the issue type name.
     * @param issueTypeName The name of the issue type, whose ID we need.
     * @param project The project to look in.
     * @return The id of the issue type.
     * @throws UnableToMapIssueException Thrown if the issue type is not found.
     */
    private static Long getIssueType(String issueTypeName, Project project) throws UnableToMapIssueException {
        for(IssueType issueType : project.getIssueTypes()){
            if (issueType.getName().equals(issueTypeName)) {
                return issueType.getId();
            }
        }
        throw new UnableToMapIssueException("No issueType with name " + issueTypeName + " found in project " + project.getName());
    }
}
