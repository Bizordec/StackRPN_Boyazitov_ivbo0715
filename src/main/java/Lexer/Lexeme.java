package Lexer;

import java.util.regex.Pattern;

class Lexeme {

    private String type;
    private Pattern pattern;
    private int priority;

    Lexeme(String type, Pattern pattern, int priority) {
        this.type = type;
        this.pattern = pattern;
        this.priority = priority;
    }

    Lexeme(String type, Pattern pattern) {
        this.type = type;
        this.pattern = pattern;
    }

    String getType() {
        return type;
    }

    Pattern getPattern() {
        return pattern;
    }

    int getPriority() {
        return priority;
    }
}