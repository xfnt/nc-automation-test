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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.example.util.ErrorMessage.TOKEN_ERROR_MESSAGE;
import static org.example.util.ErrorMessage.TOKEN_MUST_NOT_BE_NULL;
import static org.hamcrest.Matchers.equalTo;

@Epic("Application Management")
@Feature("Token Validation")
public class TokenValidationTest extends Base {

    @ParameterizedTest(name = "Token length: {0}, Action: {1}")
    @Story("Token Exceeds Maximum Length")
    @DisplayName("Reject tokens longer than 32 characters")
    @Description("Verifies that tokens exceeding the maximum allowed length of 32 characters are rejected with HTTP 400, result 'ERROR', and the expected token validation error message for LOGIN, ACTION, and LOGOUT operations.")
    @Severity(SeverityLevel.CRITICAL)
    @CsvSource({
            "33,LOGIN", "33,ACTION", "33,LOGOUT",
            "1000,LOGIN", "1000,ACTION", "1000,LOGOUT"
    })
    public void shouldReturnErrorWhenTokenLengthMoreThan32Characters(int length, Action action) {
        doAction(TokenGenerator.generateToken(length, TokenGenerator.TokenType.NUMBER_UPPER_CASE), action)
                .then()
                .log().all()
                .statusCode(400)
                .body("result", equalTo("ERROR"))
                .body("message", equalTo(TOKEN_ERROR_MESSAGE.getMessage()));
    }

    @ParameterizedTest(name = "Token length: {0}, Action: {1}")
    @Story("Token Below Minimum Length")
    @DisplayName("Reject tokens shorter than 32 characters")
    @Description("Verifies that tokens shorter than the required length of 32 characters are rejected with HTTP 400, result 'ERROR', and the expected token validation error message for LOGIN, ACTION, and LOGOUT operations.")
    @Severity(SeverityLevel.CRITICAL)
    @CsvSource({
            "1,LOGIN", "1,ACTION", "1,LOGOUT",
            "29,LOGIN", "29,ACTION", "29,LOGOUT"
    })
    public void shouldReturnErrorWhenTokenLengthLessThan32Characters(int length, Action action) {
        doAction(TokenGenerator.generateToken(length, TokenGenerator.TokenType.NUMBER_UPPER_CASE), action)
                .then()
                .log().all()
                .statusCode(400)
                .body("result", equalTo("ERROR"))
                .body("message", equalTo(TOKEN_ERROR_MESSAGE.getMessage()));
    }

    @ParameterizedTest(name = "Invalid characters: {0}, Action: {1}")
    @Story("Invalid Token Characters")
    @DisplayName("Reject tokens containing invalid characters")
    @Description("Verifies that tokens containing characters outside the allowed uppercase letters and digits are rejected with HTTP 400, result 'ERROR', and the expected token validation error message for LOGIN, ACTION, and LOGOUT operations.")
    @Severity(SeverityLevel.CRITICAL)
    @CsvSource({
            "%,LOGIN", "#,ACTION", "@,LOGOUT",
            "abcde,LOGIN", "1a2B,ACTION", "@BG#%^,LOGOUT"
    })
    public void shouldReturnErrorWhenLoginWithTokenRandomCharacter(String tokenPart, Action action) {
        doAction(TokenGenerator.generateToken(32 - tokenPart.length(), TokenGenerator.TokenType.NUMBER_UPPER_CASE) + tokenPart, action)
                .then()
                .log().all()
                .statusCode(400)
                .body("result", equalTo("ERROR"))
                .body("message", equalTo(TOKEN_ERROR_MESSAGE.getMessage()));
    }

    @ParameterizedTest(name = "Missing token, Action: {0}")
    @Story("Missing Token")
    @DisplayName("Reject requests without a token")
    @Description("Verifies that requests without the required token parameter are rejected with HTTP 400, result 'ERROR', and the expected missing token error message for LOGIN, ACTION, and LOGOUT operations.")
    @Severity(SeverityLevel.CRITICAL)
    @CsvSource({"LOGIN", "ACTION", "LOGOUT"})
    public void shouldReturnErrorWithoutToken(Action action) {
        createRequestSpecification()
                .queryParam("action", action.name())
                .when()
                .post()
                .then()
                .log().all()
                .statusCode(400)
                .body("result", equalTo("ERROR"))
                .body("message", equalTo(TOKEN_MUST_NOT_BE_NULL.getMessage()));
    }
}
