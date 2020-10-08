package utils;

public interface JiraCredentials {

    String username = "webinar5";
    String password = "webinar5";

    String BASE_URL = "https://jira.hillel.it";
    String baseIssueURL = "/rest/api/2/issue";
    String someIssueURL = BASE_URL + baseIssueURL + "/%s";
    String addCommentURL = BASE_URL + baseIssueURL + "/%s/comment";
    String commentIdUrl = BASE_URL + baseIssueURL + "/%s/comment/%s";

}
