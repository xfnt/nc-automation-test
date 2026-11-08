package org.example;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;

public class ActionTest {

    @Test
    public void shouldSuccessLogin() {
        RestAssured.given()
                .baseUri("http://localhost:8080")
                .basePath("/endpoint")
                .header("Content-Type", " application/x-www-form-urlencoded ")
                .header("Accept", "application/json")
                .header("X-Api-Key", "qazWSXedc")
                .queryParam("token", "A3F7C91D0B62E84F5A19D03C7E6B2F48")
                .queryParam("action", "LOGIN")
                .when()
                    .post()
                .then()
                    .log().all()
                    .statusCode(200)
                    .body("result", equalTo("OK"));
    }
}
