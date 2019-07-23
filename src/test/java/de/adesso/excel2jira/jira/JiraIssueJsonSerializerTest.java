package de.adesso.excel2jira.jira;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.adesso.excel2jira.jira.domain.JiraIssue;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * The JSON serializer for the JiraIssue class always writes the JSON attributes in the same order (as the procedure is done manually).
 * Therefore, testing against the known string result should be ok.
 */
public class JiraIssueJsonSerializerTest {

    private JiraIssue testIssue;

    @Before
    public void setUp(){
        testIssue = new JiraIssue();
        testIssue.setAssignee("atanasov");
        testIssue.setPriority(1L);
        testIssue.setVersions(Arrays.asList(2L, 3L));
        testIssue.setLabels(Arrays.asList("label1", "label2"));
        testIssue.setIssueType(3L);
        testIssue.setDescription("description");
        testIssue.setSummary("summary");
        testIssue.setProjectId(5L);
    }

    @Test
    public void issueIsCorrectlySerialized() throws JsonProcessingException {
        String expected = "{\"update\":{},\"fields\":{\"project\":{\"id\":\"5\"},\"summary\":\"summary\",\"issuetype\":" +
                "{\"id\":\"3\"},\"assignee\":{\"name\":\"atanasov\"},\"priority\":{\"id\":\"1\"},\"description\":\"description\"," +
                "\"labels\":[\"label1\",\"label2\"],\"fixVersions\":[{\"id\":\"2\"},{\"id\":\"3\"}]}}";

        ObjectMapper mapper = new ObjectMapper();
        String result = mapper.writeValueAsString(testIssue);
        assertEquals(expected, result);
    }

    @Test
    public void issueIsCorrectlySerializedWithLabelsNull() throws JsonProcessingException {
        testIssue.setLabels(null);

        String expected = "{\"update\":{},\"fields\":{\"project\":{\"id\":\"5\"},\"summary\":\"summary\",\"issuetype\":" +
                "{\"id\":\"3\"},\"assignee\":{\"name\":\"atanasov\"},\"priority\":{\"id\":\"1\"},\"description\":\"description\"," +
                "\"fixVersions\":[{\"id\":\"2\"},{\"id\":\"3\"}]}}";

        ObjectMapper mapper = new ObjectMapper();
        String result = mapper.writeValueAsString(testIssue);
        assertEquals(expected, result);
    }

    @Test
    public void issueIsCorrectlySerializedWithLabelsEmpty() throws JsonProcessingException {
        testIssue.setLabels(new ArrayList<>());

        String expected = "{\"update\":{},\"fields\":{\"project\":{\"id\":\"5\"},\"summary\":\"summary\",\"issuetype\":" +
                "{\"id\":\"3\"},\"assignee\":{\"name\":\"atanasov\"},\"priority\":{\"id\":\"1\"},\"description\":\"description\"," +
                "\"fixVersions\":[{\"id\":\"2\"},{\"id\":\"3\"}]}}";

        ObjectMapper mapper = new ObjectMapper();
        String result = mapper.writeValueAsString(testIssue);
        assertEquals(expected, result);
    }

    @Test
    public void issueIsCorrectlySerializedWithFixVersionsNull() throws JsonProcessingException {
        testIssue.setVersions(null);

        String expected = "{\"update\":{},\"fields\":{\"project\":{\"id\":\"5\"},\"summary\":\"summary\",\"issuetype\":" +
                "{\"id\":\"3\"},\"assignee\":{\"name\":\"atanasov\"},\"priority\":{\"id\":\"1\"},\"description\":\"description\"," +
                "\"labels\":[\"label1\",\"label2\"]}}";

        ObjectMapper mapper = new ObjectMapper();
        String result = mapper.writeValueAsString(testIssue);
        assertEquals(expected, result);
    }

    @Test
    public void issueIsCorrectlySerializedWithFixVersionsEmpty() throws JsonProcessingException {
        testIssue.setVersions(new ArrayList<>());

        String expected = "{\"update\":{},\"fields\":{\"project\":{\"id\":\"5\"},\"summary\":\"summary\",\"issuetype\":" +
                "{\"id\":\"3\"},\"assignee\":{\"name\":\"atanasov\"},\"priority\":{\"id\":\"1\"},\"description\":\"description\"," +
                "\"labels\":[\"label1\",\"label2\"]}}";

        ObjectMapper mapper = new ObjectMapper();
        String result = mapper.writeValueAsString(testIssue);
        assertEquals(expected, result);
    }
}
