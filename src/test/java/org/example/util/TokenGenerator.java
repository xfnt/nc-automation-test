package org.example.util;

import java.security.SecureRandom;

public abstract class TokenGenerator {
    private static final SecureRandom RANDOM = new SecureRandom();

    private static final String NUMERIC = "0123456789";
//    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String UPPERCASE = "ABCDEF";

    // According to the task requirements, the token must follow this pattern: [A-Z0-9]{32}.
    public static String generateSuccessfulToken() {
        return generateToken(32, TokenType.NUMBER_UPPER_CASE);
    }

    public static String generateToken(int length, TokenType type) {
        char[] characters = type.getCharacters();
        char[] result = new char[length];

        for (int i = 0; i < length; i++) {
            result[i] = characters[RANDOM.nextInt(characters.length)];
        }

        return new String(result);
    }

    public enum TokenType {
        NUMBER_UPPER_CASE(NUMERIC + UPPERCASE);

        private final char[] characters;

        TokenType(String characters) {
            this.characters = characters.toCharArray();
        }

        public char[] getCharacters() {
            return characters;
        }
    }
}
