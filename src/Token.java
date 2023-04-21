public class Token {

    private final String type;
    private final String lexeme;

    public Token(String type, String lexeme) {
        this.type = type;
        this.lexeme = lexeme;
    }

    public String getType() {
        return type;
    }

    public String getLexeme() {
        return lexeme;
    }

    @Override
    public String toString() {
        return " Token: " + type + ", Lexema: '" + lexeme + "'\n";
    }
}