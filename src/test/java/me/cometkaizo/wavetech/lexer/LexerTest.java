package me.cometkaizo.wavetech.lexer;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertThrows;

class LexerTest {

    private final Lexer lexer = new Lexer();

    @Test
    void testIllegalKeywordPattern() {
        assertThrows(CompilationException.class, () ->
                lexer.tokenize(new File("src/main/resources/testsource/illegal_keyword_pattern.txt")));
    }
}