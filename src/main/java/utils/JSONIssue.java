package utils;

import org.json.simple.JSONObject;

public class JSONIssue {
    public static JSONObject produceIssue() {
        JSONObject issueType = new JSONObject();
        JSONObject project = new JSONObject();
        JSONObject reporter = new JSONObject();
        JSONObject fields = new JSONObject();
        JSONObject finalObj = new JSONObject();

        issueType.put("id", "10105");
        project.put("id", "10508");
        reporter.put("name", "webinar5");

        fields.put("reporter", reporter);
        fields.put("project", project);
        fields.put("issuetype", issueType);
        fields.put("summary", "My postman issue");

        finalObj.put("fields", fields);

        return finalObj;
    }
}
