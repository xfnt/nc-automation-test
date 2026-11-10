package org.example;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.example.util.Action;
import org.example.util.TokenGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.example.util.ErrorMessage.INVALID_ACTION;
import static org.example.util.ErrorMessage.TOKEN_NOT_FOUND;
import static org.hamcrest.Matchers.equalTo;

@Epic("Application Management")
@Feature("Actions")
public class ActionTest extends Base {

    @Test
    @Story("Successful Action")
    @DisplayName("Execute ACTION with a successfully logged-in token")
    @Description("Verifies that executing the ACTION operation with a previously registered token returns HTTP 200 and result 'OK'.")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldSuccessfulActon() {
        String token = TokenGenerator.generateSuccessfulToken();

        // Login step
        doAction(token, Action.LOGIN);

        // Action step
        doAction(token, Action.ACTION)
                .then()
                .log().all()
                .statusCode(200)
                .body("result", equalTo("OK"));
    }

    @Test
    @Story("Action Without Login")
    @DisplayName("Execute ACTION without a successful login")
    @Description("Verifies that executing the ACTION operation with a token that has not been registered returns HTTP 403, result 'ERROR', and the expected error message.")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldReturnErrorWhenDoActionWithoutSuccessfulLogin() {
        String token = TokenGenerator.generateToken(
                32, TokenGenerator.TokenType.NUMBER_UPPER_CASE);

        // Action step
        doAction(token, Action.ACTION)
                .then()
                .log().all()
                .statusCode(403)
                .body("result", equalTo("ERROR"))
                .body("message", equalTo(TOKEN_NOT_FOUND.getMessage().formatted(token)));
    }

    @Test
    @Story("Invalid Action")
    @DisplayName("Execute request without the action parameter")
    @Description("Verifies that a request without the action parameter returns HTTP 400, result 'ERROR', and the expected invalid action error message.")
    @Severity(SeverityLevel.NORMAL)
    public void shouldReturnErrorWithoutAction() {
        String token = TokenGenerator.generateToken(
                32, TokenGenerator.TokenType.NUMBER_UPPER_CASE);

        createRequestSpecification()
                .queryParam("token", token)
                .when()
                .post()
                .then()
                .log().all()
                .statusCode(400)
                .body("result", equalTo("ERROR"))
                .body("message", equalTo(INVALID_ACTION.getMessage().formatted("null")));
    }

    @ParameterizedTest(name = "Execute request with invalid action: {0}")
    @NullSource
    @ValueSource(strings = {"*1#", "SOME_TEXT"})
    @Story("Invalid Action")
    @DisplayName("Execute request with an invalid action")
    @Description("Verifies that a request with an invalid or null action parameter returns HTTP 400, result 'ERROR', and the corresponding invalid action error message.")
    @Severity(SeverityLevel.NORMAL)
    public void shouldReturnErrorWhenWrongAction(String action) {
        String token = TokenGenerator.generateToken(
                32, TokenGenerator.TokenType.NUMBER_UPPER_CASE);

        createRequestSpecification()
                .queryParam("token", token)
                .queryParam("action", action)
                .when()
                .post()
                .then()
                .log().all()
                .statusCode(400)
                .body("result", equalTo("ERROR"))
                .body("message", equalTo(INVALID_ACTION.getMessage().formatted(action)));
    }
}
