package de.adesso.excel2jira.jira;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import de.adesso.excel2jira.jira.domain.JiraIssue;

import java.io.IOException;

public class JiraIssueJsonSerializer extends JsonSerializer<JiraIssue> {

    @Override
    public void serialize(JiraIssue value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeObjectField("update", new Object());

        gen.writeFieldName("fileds");
        gen.writeStartObject();

        gen.writeFieldName("project");
        gen.writeStartObject();
        gen.writeNumberField("id", value.getFields().getProjectId());
        gen.writeEndObject();

        gen.writeStringField("summary", value.getFields().getSummary());

        gen.writeFieldName("issueType");
        gen.writeStartObject();
        gen.writeNumberField("id", value.getFields().getIssueType());
        gen.writeEndObject();

        gen.writeFieldName("assignee");
        gen.writeStartObject();
        gen.writeStringField("name", value.getFields().getAssignee());
        gen.writeEndObject();

        gen.writeFieldName("priority");
        gen.writeStartObject();
        gen.writeNumberField("id", value.getFields().getPriority());
        gen.writeEndObject();

        gen.writeObjectField("labels", value.getFields().getLabels());

        gen.writeEndObject();
        gen.writeEndObject();
    }
}
