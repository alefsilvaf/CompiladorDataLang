import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.BufferedReader;
import java.io.FileReader;

public class PythonLexer {

    private final String input;
    private int position;
    private final List<Character> buffer = new ArrayList<>();

    private final Map<String, String> keywords = new HashMap<>() {{
        put("and", "and");
        put("or", "or");
        put("class", "class");
        put("def", "def");
        put("if", "if");
        put("else", "else");
        put("elif", "elif");
        put("while", "while");
        put("for", "for");
        put("return", "return");
        put("True", "true");
        put("False", "false");
        put("None", "null");
        put("try", "try");
        put("except", "except");
        put("finally", "finally");
        put("raise", "raise");
        put("assert", "assert");
        put("import", "import");
        put("from", "from");
        put("as", "as");
        put("lambda", "lambda");
        put("nonlocal", "nonlocal");
        put("global", "global");
        put("async", "async");
        put("await", "await");
    }};

    public PythonLexer(String input) {
        this.input = input;
    }

    private char advance() {
        char currentChar = peek();
        buffer.add(currentChar);
        position++;
        return currentChar;
    }

    private char peek() {
        if (position >= input.length()) {
            return '\0';
        }
        return input.charAt(position);
    }

    private boolean isAlphaNumeric(char c) {
        return Character.isLetterOrDigit(c) || c == '_';
    }

    private boolean isDigit(char c) {
        return Character.isDigit(c);
    }

    private String getBufferContent() {
        StringBuilder builder = new StringBuilder();
        for (char c : buffer) {
            builder.append(c);
        }
        buffer.clear();
        return builder.toString();
    }

    private Token createToken(String type, String lexeme) {
        return new Token(type, lexeme);
    }

    Token getNextToken() {
        while (peek() != '\0') {
            if (peek() == ' ' || peek() == '\t' || peek() == '\r' || peek() == '\n') {
                advance();
            } else if (peek() == '#') {
                // Ignore comments
                while (peek() != '\n') {
                    advance();
                }
            } else if (isAlphaNumeric(peek())) {
                while (isAlphaNumeric(peek())) {
                    advance();
                }
                String lexeme = getBufferContent();
                String keyword = keywords.getOrDefault(lexeme, null);
                if (keyword != null) {
                    return createToken(keyword, lexeme);
                } else {
                    return createToken("IDENTIFIER", lexeme);
                }
            } else if (isDigit(peek())) {
                while (isDigit(peek())) {
                    advance();
                }
                if (peek() == '.') {
                    advance();
                    while (isDigit(peek())) {
                        advance();
                    }
                }
                String lexeme = getBufferContent();
                return createToken("NUMBER", lexeme);
            } else {
                switch (peek()) {
                    case '+':
                        advance();
                        return createToken("PLUS", "+");
                    case '-':
                        advance();

                        return createToken("MINUS", "-");
                    case '*':
                        advance();
                        return createToken("MULTIPLY", "*");
                    case '/':
                        advance();
                        return createToken("DIVIDE", "/");
                    case '%':
                        advance();
                        return createToken("MODULO", "%");
                    case '=':
                        advance();
                        if (peek() == '=') {
                            advance();
                            return createToken("EQUALS", "==");
                        }
                        return createToken("ASSIGN", "=");
                    case '<':
                        advance();
                        if (peek() == '=') {
                            advance();
                            return createToken("LESS_EQUALS", "<=");
                        }
                        return createToken("LESS", "<");
                    case '>':
                        advance();
                        if (peek() == '=') {
                            advance();
                            return createToken("GREATER_EQUALS", ">=");
                        }
                        return createToken("GREATER", ">");
                    case '!':
                        advance();
                        if (peek() == '=') {
                            advance();
                            return createToken("NOT_EQUALS", "!=");
                        }
                        throw new RuntimeException("Unexpected character: " + peek());
                    case '(':
                        advance();
                        return createToken("LEFT_PAREN", "(");
                    case ')':
                        advance();
                        return createToken("RIGHT_PAREN", ")");
                    case '[':
                        advance();
                        return createToken("LEFT_BRACKET", "[");
                    case ']':
                        advance();
                        return createToken("RIGHT_BRACKET", "]");
                    case '{':
                        advance();
                        return createToken("LEFT_BRACE", "{");
                    case '}':
                        advance();
                        return createToken("RIGHT_BRACE", "}");
                    case ',':
                        advance();
                        return createToken("COMMA", ",");
                    case '.':
                        advance();
                        if (isDigit(peek())) {
                            while (isDigit(peek())) {
                                advance();
                            }
                            String lexeme = getBufferContent();
                            return createToken("NUMBER", "." + lexeme);
                        }
                        return createToken("DOT", ".");
                    case ':':
                        advance();
                        return createToken("COLON", ":");
                    case ';':
                        advance();
                        return createToken("SEMICOLON", ";");
                    case '\'':
                    case '\"':
                        char quote = peek();
                        advance();
                        while (peek() != quote) {
                            if (peek() == '\\') {
                                advance();
                            }
                            if (peek() == '\0') {
                                throw new RuntimeException("Unterminated string literal");
                            }
                            advance();
                        }
                        advance(); // consume the closing quote
                        String lexeme = getBufferContent();
                        return createToken("STRING", lexeme);
                    default:
                        throw new RuntimeException("Unexpected character: " + peek());
                }
            }
        }
        return createToken("EOF", "");
    }

    public List<Token> lex() {
        List<Token> tokens = new ArrayList<>();
        Token token;
        do {
            token = getNextToken();
            tokens.add(token);
        } while (!token.getType().equals("EOF"));
        return tokens;
    }
        public static String main() {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Selecione um arquivo de entrada");
            chooser.setFileFilter(new FileNameExtensionFilter("Arquivos de texto", "txt"));
            int resultado = chooser.showOpenDialog(null);
            String output = null;
            if (resultado == JFileChooser.APPROVE_OPTION) {
                String caminhoDoArquivo = chooser.getSelectedFile().getPath();
                String input = readFile(caminhoDoArquivo);
                output = "";
                PythonLexer lexer = new PythonLexer(input);
                List<Token> tokens = lexer.lex();
                for (Token token : tokens) {
                    output += token + "\n";
                }
            } else {
                output = "Nenhum arquivo selecionado.";
            }
            return output;
        }

        private static String readFile(String filePath) {
            StringBuilder content = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    content.append(line).append("\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return content.toString();
        }
}