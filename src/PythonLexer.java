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
    public static int openParenCount = 0;
    public static int openBraceCount = 0;
    public static int openBracketCount = 0 ;

    public static int lineCount = 0;

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
        put("IFSULDEMINAS", "IFSULDEMINAS");
        put("INICIO", "INICIO");
        put("FIM", "FIM");
        put("COMPILADORES", "COMPILADORES");
    }};

    public PythonLexer(String input) {
        this.input = input;
    }

    private char advance() {
        char currentChar = peek();
        buffer.add(currentChar);

        switch (currentChar) {
            case '(':
                openParenCount++;
                break;
            case '{':
                System.out.println("Alo");
                openBraceCount ++;
                System.out.println(openBraceCount);
                break;
            case '[':
                openBracketCount ++;
                break;
            case ')':
                openParenCount--;
                break;
            case '}':
                openBraceCount --;
                System.out.println(openBraceCount);
                break;
            case ']':
                openBracketCount--;
                break;
        }
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
            if(c != '\n' && c != '\t') builder.append(c);
        }
        buffer.clear();
        return builder.toString();
    }

    private Token createToken(String type, String lexeme, int linhaAtual, int position) {
        return new Token(type, lexeme, linhaAtual, position);
    }

    Token getNextToken() {

        while (peek() != '\0') {
            if(peek() == '\n'){
                lineCount ++;
            }
            if (peek() == '\n' || peek() == ' ' || peek() == '\t' || peek() == '\r') {
                advance();
            }
            else if (peek() == '#') {
                // Ignore comments
                while (peek() != '\n') {
                    advance();
                }
            }
            else if (isAlphaNumeric(peek())) {
                while (isAlphaNumeric(peek())) {
                    advance();
                }
                String lexeme = getBufferContent();
                String keyword = keywords.getOrDefault(lexeme, null);
                if (keyword != null) {
                    return createToken(keyword, lexeme, lineCount, position );
                } else {
                    return createToken("IDENTIFIER", lexeme, lineCount, position);
                }
            }
            else if (isDigit(peek())) {
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
                return createToken("NUMBER", lexeme, lineCount, position);
            }
            else {
                switch (peek()) {
                    case '+':
                        advance();
                        return createToken("PLUS", "+", lineCount, position);
                    case '-':
                        advance();

                        return createToken("MINUS", "-", lineCount, position);
                    case '*':
                        advance();
                        return createToken("MULTIPLY", "*", lineCount, position);
                    case '/':
                        advance();
                        return createToken("DIVIDE", "/", lineCount, position);
                    case '%':
                        advance();
                        return createToken("MODULO", "%", lineCount, position);
                    case '=':
                        advance();
                        if (peek() == '=') {
                            advance();
                            return createToken("EQUALS", "==", lineCount, position);
                        }
                        return createToken("ASSIGN", "=", lineCount, position);
                    case '<':
                        advance();
                        if (peek() == '=') {
                            advance();
                            return createToken("LESS_EQUALS", "<=", lineCount, position);
                        }
                        return createToken("LESS", "<", lineCount, position);
                    case '>':
                        advance();
                        if (peek() == '=') {
                            advance();
                            return createToken("GREATER_EQUALS", ">=", lineCount, position);
                        }
                        return createToken("GREATER", ">", lineCount, position);
                    case '!':
                        advance();
                        if (peek() == '=') {
                            advance();
                            return createToken("NOT_EQUALS", "!=", lineCount, position);
                        }
                    case '(':
                        advance();
                        return createToken("LEFT_PAREN", "(", lineCount, position);
                    case ')':
                        advance();
                        return createToken("RIGHT_PAREN", ")", lineCount, position);
                    case '[':
                        advance();
                        return createToken("LEFT_BRACKET", "[", lineCount, position);
                    case ']':
                        advance();
                        return createToken("RIGHT_BRACKET", "]", lineCount, position);
                    case '{':
                        advance();
                        return createToken("LEFT_BRACE", "{", lineCount, position);
                    case '}':
                        advance();
                        return createToken("RIGHT_BRACE", "}", lineCount, position);
                    case ',':
                        advance();
                        return createToken("COMMA", ",", lineCount, position);
                    case '.':
                        advance();
                        if (isDigit(peek())) {
                            while (isDigit(peek())) {
                                advance();
                            }
                            String lexeme = getBufferContent();
                            return createToken("NUMBER", "." + lexeme, lineCount, position);
                        }
                        return createToken("DOT", ".", lineCount, position);
                    case ':':
                        advance();
                        return createToken("COLON", ":", lineCount, position);
                    case ';':
                        advance();
                        return createToken("SEMICOLON", ";", lineCount, position);
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
                        return createToken("STRING", lexeme, lineCount, position);
                    default:
                        String lexeme1 = String.valueOf(peek());
                        advance();
                        return createToken("null", "CARACTERE NÃO RECONHECIDO: " + lexeme1, lineCount, position);

                }
            }
        }
        return createToken("EOF", "", lineCount, position);
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
    public static String[] main() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Selecione um arquivo de entrada");
        chooser.setFileFilter(new FileNameExtensionFilter("Arquivos de texto", "txt"));
        int resultado = chooser.showOpenDialog(null);
        String[] output = new String[2];
        if (resultado == JFileChooser.APPROVE_OPTION) {
            String caminhoDoArquivo = chooser.getSelectedFile().getPath();
            String input = readFile(caminhoDoArquivo);
            output[0] = "";
            output[1] = caminhoDoArquivo;
            PythonLexer lexer = new PythonLexer(input);
            List<Token> tokens = lexer.lex();
            for (Token token : tokens) {
                output[0] += token;
            }

            System.out.println(tokens);

            if(openParenCount != 0 ){
                output[0] += "Você se esqueceu de fechar parênteses! ')'!";
            } else if (openBraceCount != 0 ) {
                output[0] += "Você se esqueceu de fechar chaves! '}'!";
            } else if (openBracketCount != 0) {
                output[0] += "Você se esqueceu de fechar colchetes! ']'!";
            }
            output[1]=input;
        } else {
            output[0] = "Nenhum arquivo selecionado.";
            output[1] = "";
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