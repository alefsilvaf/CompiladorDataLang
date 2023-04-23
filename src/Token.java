public class Token {

    private final String type;
    private final String lexeme;
    private final int lineCount;

    private final int position;

    public Token(String type, String lexeme, int lineCount, int position) {
        this.type = type;
        this.lexeme = lexeme;
        this.lineCount = lineCount;
        this.position = position;
    }

    public String getType() {
        return type;
    }

    public String getLexeme() {
        return lexeme;
    }

    @Override
    public String toString() {
        return " Token: " + type + ", Lexema: '" + lexeme + "' Linha: " + lineCount + " Coluna: " + position +"\n";
    }
}