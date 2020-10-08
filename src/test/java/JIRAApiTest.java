import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.JiraAPISteps;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;


public class JIRAApiTest {

    @BeforeClass
    public void init() {
        JiraAPISteps.initAuth();
    }

    @Test
    public void getExistingIssue() {
        Response getIssueResponse = JiraAPISteps.getIssue("WEBINAR-9060");

        assertEquals(getIssueResponse.statusCode(), 200);
        assertEquals(getIssueResponse.path("key"), "WEBINAR-9060");
    }

    @Test
    public void createIssue() {
        Response newIssueResponse = JiraAPISteps.createNewIssue();

        String webinar = newIssueResponse.path("key");
        Response getSuccessResponse = JiraAPISteps.getIssue(webinar);
        assertEquals(getSuccessResponse.path("fields.summary"), "API test summary");
        assertEquals(getSuccessResponse.path("fields.reporter.name"), "webinar5");
        assertTrue(webinar.contains("WEBINAR-")); // new contains assert

        JiraAPISteps.deleteIssue(webinar);

        Response deletedIssueResponse = JiraAPISteps.getIssue(webinar);
        assertEquals(deletedIssueResponse.statusCode(), 404);
    }

    @Test
    public void addCommentTest() {
        Response newComment = JiraAPISteps.addNewComment("WEBINAR-13726");

        String commentId = newComment.path("id");

        JiraAPISteps.deleteAddedComment("WEBINAR-13726", commentId);
        Response deletedCommentResponse = JiraAPISteps.getIssue( "WEBINAR-13726/comment/" + commentId);
        assertEquals(deletedCommentResponse.statusCode(), 404);
    }

}
