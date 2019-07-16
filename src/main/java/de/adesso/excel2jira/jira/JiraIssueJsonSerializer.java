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

        gen.writeFieldName("update");
        gen.writeStartObject();
        gen.writeEndObject();

        gen.writeFieldName("fields");
        gen.writeStartObject();

        gen.writeFieldName("project");
        gen.writeStartObject();
        gen.writeStringField("id", String.valueOf(value.getFields().getProjectId()));
        gen.writeEndObject();

        gen.writeStringField("summary", value.getFields().getSummary());

        gen.writeFieldName("issuetype");
        gen.writeStartObject();
        gen.writeStringField("id", String.valueOf(value.getFields().getIssueType()));
        gen.writeEndObject();

        gen.writeFieldName("assignee");
        gen.writeStartObject();
        gen.writeStringField("name", value.getFields().getAssignee());
        gen.writeEndObject();

        gen.writeFieldName("priority");
        gen.writeStartObject();
        gen.writeStringField("id", String.valueOf(value.getFields().getPriority()));
        gen.writeEndObject();

        //customfield_10321
        gen.writeObjectField("labels", value.getFields().getLabels());

        gen.writeEndObject();
        gen.writeEndObject();
    }
}
