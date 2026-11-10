package org.example;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.example.util.Action;

public abstract class Base {

    protected Response doAction(String token, Action action) {
        return createRequestSpecification()
                .queryParam("token", token)
                .queryParam("action", action.name())
                .when()
                .post();
    }

    protected RequestSpecification createRequestSpecification() {
        return RestAssured.given()
                .baseUri("http://localhost:8080")
                .basePath("/endpoint")
                .header("Content-Type", " application/x-www-form-urlencoded ")
                .header("Accept", "application/json")
                .header("X-Api-Key", "qazWSXedc")
                .log().all();
    }
}
