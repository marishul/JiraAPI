import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.JSONCommentBody;
import utils.JSONIssue;
import utils.JiraAPISteps;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;


public class JIRAApiTest {

    private RequestSpecification given;
    private static final String BASE_ISSUE_URL = "https://jira.hillel.it/rest/api/2/issue";
    private JiraAPISteps steps = new JiraAPISteps();

    @BeforeClass
    public void init() {
        steps.initAuth();
    }

    @Test
    public void getExistingIssue() {
        Response getIssueResponse = steps.getIssue(BASE_ISSUE_URL + "/WEBINAR-9060");

        assertEquals(getIssueResponse.statusCode(), 200);
        assertEquals(getIssueResponse.path("key"), "WEBINAR-9060");
    }

    @Test
    public void createIssue() {
        Response newIssueResponse = steps.createNewIssue(BASE_ISSUE_URL);

        String webinar = newIssueResponse.path("key");
        Response getSuccessResponse = steps.getIssue(BASE_ISSUE_URL + "/" + webinar);
        assertEquals(getSuccessResponse.path("fields.summary"), "My postman issue");
        assertEquals(getSuccessResponse.path("fields.reporter.name"), "webinar5");
        assertTrue(webinar.contains("WEBINAR-")); // new contains assert

        steps.deleteIssue(BASE_ISSUE_URL + "/" + webinar);

        Response deletedIssueResponse = steps.getIssue(BASE_ISSUE_URL + "/" + webinar);
        assertEquals(deletedIssueResponse.statusCode(), 404);
    }

    @Test
    public void addCommentTest() {
        Response newComment = steps.addNewComment(BASE_ISSUE_URL + "/WEBINAR-13726/comment");

        String commentId = newComment.path("id");

        steps.deleteAddedComment(BASE_ISSUE_URL + "/WEBINAR-13726/comment/" + commentId);
        Response deletedCommentResponse = steps.getIssue(BASE_ISSUE_URL + "/WEBINAR-13726/comment/" + commentId);
        assertEquals(deletedCommentResponse.statusCode(), 404);
    }
}
