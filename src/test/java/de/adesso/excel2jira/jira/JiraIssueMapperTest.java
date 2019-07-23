package de.adesso.excel2jira.jira;


import de.adesso.excel2jira.excel.domain.Issue;
import de.adesso.excel2jira.jira.domain.JiraIssue;
import de.adesso.excel2jira.jira.domain.Priority;
import de.adesso.excel2jira.jira.domain.User;
import de.adesso.excel2jira.jira.domain.project.FixVersion;
import de.adesso.excel2jira.jira.domain.project.IssueType;
import de.adesso.excel2jira.jira.domain.project.Project;
import feign.FeignException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class JiraIssueMapperTest {

    private JiraClient mockJiraClient = Mockito.mock(JiraClient.class);

    private URI testUri;
    private String testAuth = "auth";
    private Issue testIssue;

    @Before
    public void setUp() throws URISyntaxException {
        testIssue = new Issue();
        testIssue.setProjectName("testProject");
        testIssue.setSummary("testProject summary");
        testIssue.setIssueType("Feature");
        testIssue.setPriority("important");
        testIssue.setAssignee("atanasov");
        testIssue.setDescription("testDescription");
        testIssue.setLabels(Arrays.asList("label1", "label2"));
        testIssue.setFixVersions(Collections.singletonList("fixVersion1"));

        testUri = new URI("https://valid.url");
        Project testProject = new Project(1L, "testProject", testIssue.getProjectName(),
                Collections.singletonList(new IssueType(2L, "Feature")),
                Collections.singletonList(new FixVersion(3L, "fixVersion1")));

        when(mockJiraClient.getProjects(testUri, testAuth)).thenReturn(Collections.singletonList(testProject));
        when(mockJiraClient.getProject(testUri, testAuth, 1L)).thenReturn(testProject);
        when(mockJiraClient.getPriorities(testUri, testAuth)).thenReturn(Collections.singletonList(new Priority(4L, testIssue.getPriority())));
        when(mockJiraClient.getUser(testUri, testAuth, "atanasov")).thenReturn(new User(testIssue.getAssignee()));
        when(mockJiraClient.getUser(testUri, testAuth, "atanasov2")).thenThrow(new FeignException.NotFound("not found", "not found!".getBytes()));
    }

    @Test
    public void testIssueIsMappedCorrectly() throws UnableToMapIssueException, AuthorizationException {
        List<JiraIssue> jiraIssues = JiraIssueMapper.map(testUri, mockJiraClient, Collections.singletonList(testIssue), testAuth);
        assertEquals(1, jiraIssues.size());
        assertEquals(1L, jiraIssues.get(0).getProjectId().longValue());
        assertEquals(testIssue.getDescription(), jiraIssues.get(0).getDescription());
        assertEquals(testIssue.getAssignee(), jiraIssues.get(0).getAssignee());
        assertEquals(testIssue.getSummary(), jiraIssues.get(0).getSummary());
        assertEquals(4L, jiraIssues.get(0).getPriority().longValue());
        assertEquals(3L, jiraIssues.get(0).getVersions().get(0).longValue());
        assertEquals(2L, jiraIssues.get(0).getIssueType().longValue());
    }

    @Test(expected = UnableToMapIssueException.class)
    public void testMapperThrowsExceptionIfUserNotFound() throws UnableToMapIssueException, AuthorizationException {
        testIssue.setAssignee("atanasov2");
        JiraIssueMapper.map(testUri, mockJiraClient, Collections.singletonList(testIssue), testAuth);
    }

    @Test(expected = UnableToMapIssueException.class)
    public void testMapperThrowsExceptionIfProjectNotFound() throws UnableToMapIssueException, AuthorizationException {
        testIssue.setProjectName("testProject1");
        JiraIssueMapper.map(testUri, mockJiraClient, Collections.singletonList(testIssue), testAuth);
    }

    @Test(expected = UnableToMapIssueException.class)
    public void testMapperThrowsExceptionIfIssueTypeNotFound() throws UnableToMapIssueException, AuthorizationException {
        testIssue.setIssueType("non-Feature");
        JiraIssueMapper.map(testUri, mockJiraClient, Collections.singletonList(testIssue), testAuth);
    }

    @Test(expected = UnableToMapIssueException.class)
    public void testMapperThrowsExceptionIfPriorityNotFound() throws UnableToMapIssueException, AuthorizationException {
        testIssue.setPriority("not important");
        JiraIssueMapper.map(testUri, mockJiraClient, Collections.singletonList(testIssue), testAuth);
    }

    @Test(expected = UnableToMapIssueException.class)
    public void testMapperThrowsExceptionIfFixVersionNotFound() throws UnableToMapIssueException, AuthorizationException {
        testIssue.setFixVersions(Collections.singletonList("fixVersion2"));
        JiraIssueMapper.map(testUri, mockJiraClient, Collections.singletonList(testIssue), testAuth);
    }
}
