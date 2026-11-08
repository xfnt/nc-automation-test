package org.example;

import io.qameta.allure.AllureId;
import io.qameta.allure.Description;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestApplication {

    @Test
    @AllureId("Test id 1")
    @Description("Some Description")
    public void test() {
        assertEquals(1, 1);
    }
}
