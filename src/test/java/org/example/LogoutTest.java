package org.example;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.example.util.Action;
import org.example.util.ErrorMessage;
import org.example.util.TokenGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;

@Epic("Application Management")
@Feature("Authentication")
public class LogoutTest extends Base {

    @Test
    @Story("Successful Logout")
    @DisplayName("Logout with a successfully logged-in token")
    @Description("Verifies that logging out with a previously registered token returns HTTP 200 and result 'OK'.")
    @Severity(SeverityLevel.CRITICAL)
    public void shouldSuccessLogout() {
        String token = TokenGenerator.generateSuccessfulToken();

        // Login step
        doAction(token, Action.LOGIN);

        // Logout step
        doAction(token, Action.LOGOUT)
                .then()
                .log().all()
                .statusCode(200)
                .body("result", equalTo("OK"));
    }

    @Test
    @Story("Duplicate Logout")
    @DisplayName("Logout twice with the same token")
    @Description("Verifies that attempting to log out twice with the same token returns HTTP 403, result 'ERROR', and the expected token not found error message.")
    @Severity(SeverityLevel.NORMAL)
    public void shouldReturnErrorWhenLogoutIfAlreadyLogout() {
        String token = TokenGenerator.generateSuccessfulToken();

        // Login step
        doAction(token, Action.LOGIN);

        // Logout step 1
        doAction(token, Action.LOGOUT);

        // Logout step 2
        doAction(token, Action.LOGOUT)
                .then()
                .log().all()
                .statusCode(403)
                .body("result", equalTo("ERROR"))
                .body("message", equalTo(ErrorMessage.TOKEN_NOT_FOUND.getMessage().formatted(token)));
    }

    @Test
    @Story("Logout Without Login")
    @DisplayName("Logout with a token that does not exist")
    @Description("Verifies that attempting to log out with a token that has not been registered returns HTTP 403, result 'ERROR', and the expected token not found error message.")
    @Severity(SeverityLevel.NORMAL)
    public void shouldReturnErrorWhenLogoutIfTokenIsNotContainsInService() {
        String token = TokenGenerator.generateToken(
                32, TokenGenerator.TokenType.NUMBER_UPPER_CASE);

        doAction(token, Action.LOGOUT)
                .then()
                .log().all()
                .statusCode(403)
                .body("result", equalTo("ERROR"))
                .body("message", equalTo(ErrorMessage.TOKEN_NOT_FOUND.getMessage().formatted(token)));
    }
}
