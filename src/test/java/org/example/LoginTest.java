package org.example;

import io.qameta.allure.*;
import org.example.util.Action;
import org.example.util.TokenGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.example.util.ErrorMessage.TOKEN_ALREADY_EXISTS;
import static org.hamcrest.Matchers.equalTo;

@Epic("Application Management")
@Feature("Authentication")
public class LoginTest extends Base {

    @Test
    @Story("Successful Login")
    @DisplayName("Login with a valid token")
    @Description("Verifies that a valid token can be successfully registered using the LOGIN action.")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldSuccessLogin() {
        doAction(TokenGenerator.generateSuccessfulToken(), Action.LOGIN)
                .then()
                .log().all()
                .statusCode(200)
                .body("result", equalTo("OK"));
    }

    @Test
    @Story("Duplicate Login")
    @DisplayName("Login twice with the same token")
    @Description("Verifies that attempting to register an already registered token returns HTTP 409 and the expected error message.")
    @Severity(SeverityLevel.NORMAL)
    public void shouldReturnErrorWhenLoginTwice() {
        String token = TokenGenerator.generateSuccessfulToken();

        // First login
        doAction(token, Action.LOGIN);

        // Second login
        doAction(token, Action.LOGIN)
                .then()
                .log().all()
                .statusCode(409)
                .body("result", equalTo("ERROR"))
                .body("message", equalTo(TOKEN_ALREADY_EXISTS.getMessage().formatted(token)));
    }
}
