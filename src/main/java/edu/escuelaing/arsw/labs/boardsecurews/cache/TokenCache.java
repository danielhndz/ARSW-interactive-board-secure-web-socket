package edu.escuelaing.arsw.labs.boardsecurews.cache;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TokenCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenCache.class);

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int SECURE_TOKEN_LENGTH = 32;
    private static final char[] SYMBOLS = CHARACTERS.toCharArray();
    private static final char[] BUF = new char[SECURE_TOKEN_LENGTH];

    private static Collection<String> tokens;

    private TokenCache() {
    }

    public static String generateAndAddNewToken() {
        if (tokens == null || tokens.size() > 1000) {
            tokens = new HashSet<>();
        }
        String token = nextToken();
        while (tokens.contains(token)) {
            token = nextToken();
        }
        LOGGER.info("generateAndAddNewToken\n\ttoken: {}", token);
        tokens.add(token);
        return token;
    }

    /**
     * Generate the next secure random token in the series.
     */
    private static String nextToken() {
        for (int idx = 0; idx < BUF.length; ++idx)
            BUF[idx] = SYMBOLS[SECURE_RANDOM.nextInt(SYMBOLS.length)];
        return new String(BUF);
    }

    public static Collection<String> getTokens() {
        if (tokens == null || tokens.size() > 1000) {
            tokens = new HashSet<>();
        }
        return tokens;
    }
}
