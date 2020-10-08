package utils;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;

public class JiraAPISteps {
    private static RequestSpecification given;

    public static void initAuth() {
        given = given()
                .auth()
                .preemptive()
                .basic(JiraCredentials.username, JiraCredentials.password);
    }

    public static Response getIssue(String ticketID) {
        Response getIssueResponse = given
                .when()
                .get(String.format(JiraCredentials.someIssueURL, ticketID))
                .then()
                .extract()
                .response();
        return getIssueResponse;
    }

    public static Response createNewIssue() {
        Response createCommentResponse = given
                .contentType(ContentType.JSON)
                .body(JSONObjectsBuilder.produceIssue("API test summary").toJSONString()) // вызвали метод из статического класса, который возвращает обьект
                .when()
                .post(JiraCredentials.BASE_URL + JiraCredentials.baseIssueURL)
                .then()
                .contentType(ContentType.JSON)
                .time(lessThan(2L), TimeUnit.SECONDS) // проверка что запрос не больше 2 секунд
                .statusCode(201)
                .extract()
                .response();
        System.out.println("reponse time is: " + createCommentResponse.time()); //узнать сколько времени шел запрос
        createCommentResponse.prettyPrint();
        return createCommentResponse;
    }

    public static ValidatableResponse deleteIssue(String ticketID) {
        ValidatableResponse deleteCreatedIssue = given
                .when()
                .delete(String.format(JiraCredentials.someIssueURL, ticketID))
                .then()
                .statusCode(204);
        return deleteCreatedIssue;
    }

    public static Response addNewComment(String ticketID) {
        Response addComment = given
                .contentType(ContentType.JSON)
                .body(JSONObjectsBuilder.commentBody("Lorem ipsum dolor sit amet, consectetur adipiscing elit.").toJSONString())
                .when()
                .post(String.format(JiraCredentials.addCommentURL, ticketID))
                .then()
                .contentType(ContentType.JSON)
                .statusCode(201)
                .extract()
                .response();
        addComment.prettyPrint();
        return addComment;
    }

    public static ValidatableResponse deleteAddedComment(String ticketId, String commentId) {
        ValidatableResponse deleteCreatedComment = given
                .when()
                .delete(String.format(JiraCredentials.commentIdUrl, ticketId, commentId))
                .then()
                .statusCode(204);
        return deleteCreatedComment;

    }
}
