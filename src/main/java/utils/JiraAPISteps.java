package utils;

import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;
import static org.testng.Assert.assertEquals;

public class JiraAPISteps {
    private static RequestSpecification given;

    public void initAuth() {
            given = given()
                    .auth()
                    .preemptive()
                    .basic("webinar5", "webinar5");
    }

    public Response getIssue(String url) {
        Response getIssueResponse = given
                .when()
                .get(url)
                .then()
                .extract()
                .response();
        return getIssueResponse;
    }

    public Response createNewIssue(String url) {
        Response createCommentResponse = given
                .contentType(ContentType.JSON)
                .body(JSONIssue.produceIssue().toJSONString()) // вызвали метод из статического класса, который возвращает обьект
                .when()
                .post(url)
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

    public ValidatableResponse deleteIssue(String url) {
        ValidatableResponse deleteCreatedIssue = given
                .when()
                .delete(url)
                .then()
                .statusCode(204);
        return deleteCreatedIssue;
    }

    public Response addNewComment(String url) {
        Response addComment = given
                .contentType(ContentType.JSON)
                .body(JSONCommentBody.commentBody().toJSONString())
                .when()
                .post(url)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(201)
                .extract()
                .response();
        addComment.prettyPrint();
        return addComment;
    }

    public ValidatableResponse deleteAddedComment(String url) {
        ValidatableResponse deleteCreatedComment = given
                .when()
                .delete(url)
                .then()
                .statusCode(204);
        return deleteCreatedComment;

    }





}
