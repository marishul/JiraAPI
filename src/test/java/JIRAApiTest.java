import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.JSONIssue;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;
import static org.testng.Assert.assertEquals;


public class JIRAApiTest {

    private RequestSpecification given;
    private static final String BASE_ISSUE_URL = "https://jira.hillel.it/rest/api/2/issue";

    @BeforeClass
    public void init() {
        given = given()
                .auth()
                .preemptive()
                .basic("webinar5", "webinar5");
    }

    @Test
    public void getExistingIssue() {
        Response getIssueResponse = given
                .when()
                .get(BASE_ISSUE_URL + "/WEBINAR-9060")
                .then()
                .extract()
                .response();

        Cookies coockies = getIssueResponse.getDetailedCookies();

        assertEquals(getIssueResponse.statusCode(), 200);
        assertEquals(getIssueResponse.path("key"), "WEBINAR-9060");
    }

    @Test
    public void createIssue() {
        Response createCommentResponse = given
                .contentType(ContentType.JSON)
                .body(JSONIssue.produceIssue().toJSONString()) // вызвали метод из статического класса, который возвращает обьект
                .when()
                .post(BASE_ISSUE_URL)
                .then()
                .contentType(ContentType.JSON)
                .time(lessThan(2L), TimeUnit.SECONDS) // проверка что запрос не больше 2 секунд
                .statusCode(201)
                .extract()
                .response();
        System.out.println("reponse time is: " + createCommentResponse.time()); //узнать сколько времени шел запрос
        createCommentResponse.prettyPrint();

        String webinar = createCommentResponse.path("key");

        Response createdIssue = given
                .when()
                .get(BASE_ISSUE_URL + "/" + webinar)
                .then()
                .statusCode(200)
                .extract()
                .response();

        assertEquals(createdIssue.path("fields.summary"), "My postman issue");
        assertEquals(createdIssue.path("fields.reporter.name"), "webinar5");

        // Delete issue
        given.when()
                .delete(BASE_ISSUE_URL + "/" + webinar)
                .then()
                .statusCode(204);

        // Check deleted issue
        given.when()
                .get(BASE_ISSUE_URL + "/" + webinar)
                .then()
                .statusCode(404);
    }

    @Test
    public void addCommentTest() {
        // POST comment
        Response addComment = given
                .contentType(ContentType.JSON)
                .body("{\"body\": \"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque eget venenatis elit. Duis eu justo eget augue iaculis fermentum. Sed semper quam laoreet nisi egestas at posuere augue semper.\"\n}")
                .when()
                .post(BASE_ISSUE_URL + "/WEBINAR-13726/comment")
                .then()
                .contentType(ContentType.JSON)
                .statusCode(201)
                .extract()
                .response();
        addComment.prettyPrint();
        long milli = addComment.getTime();
        System.out.println("milli = " + milli);
        Assert.assertTrue(milli <= 1000);

        String commentId = addComment.path("id");

        //DELETE comment
        given.when()
                .delete(BASE_ISSUE_URL + "/WEBINAR-13726/comment/" + commentId)
                .then()
                .statusCode(204);

        // Check deleted comment
        given.when()
                .get(BASE_ISSUE_URL + "/WEBINAR-13726/comment/" + commentId)
                .then()
                .statusCode(404);
    }

}
