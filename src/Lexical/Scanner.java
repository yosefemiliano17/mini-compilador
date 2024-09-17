package Lexical;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Scanner {

    private ArrayList<Token> token_list; 
    private ArrayList<TokenPair> token_pair; 
    private HashMap<String, Token> token_map;
    private HashSet<String> reserved_words;  

    public Scanner() {
        this.token_list = new ArrayList<>(); 
        this.token_map = new HashMap<>(); 
        this.token_pair = new ArrayList<>(); 
        this.reserved_words = new HashSet<>(); 

        this.reserved_words.add("class");
        this.reserved_words.add("if");
        this.reserved_words.add("print");
        this.reserved_words.add("read");
        this.reserved_words.add("while");
        this.reserved_words.add("int");
        this.reserved_words.add("boolean");
        this.reserved_words.add("true");
        this.reserved_words.add("false");

        //palabras reservadas
        token_map.put("class", Token.CLASS);
        token_map.put("if", Token.IF); 
        token_map.put("print", Token.PRINT); 
        token_map.put("read", Token.READ); 
        token_map.put("while", Token.WHILE); 
        token_map.put("int", Token.INT); 
        token_map.put("boolean", Token.BOOLEAN); 
        token_map.put("true", Token.TRUE); 
        token_map.put("false", Token.FALSE); 

        //llaves
        token_map.put("{", Token.LLAVE_APERTURA); 
        token_map.put("}", Token.LLAVE_CIERRE); 

        //corchetes
        token_map.put("(", Token.PARENTESIS_APERTURA); 
        token_map.put(")", Token.PARENTESIS_CIERRE); 

        //punto y coma
        token_map.put(";", Token.PUNTO_COMA); 

        //operadores
        token_map.put("<", Token.MENOR_QUE); 
        token_map.put(">", Token.MAYOR_QUE); 
        token_map.put("=", Token.SIGNO_IGUAL); 
        token_map.put("==", Token.COMPARADOR_IGUAL); 
        token_map.put("+", Token.SUMA); 
        token_map.put("-", Token.RESTA); 
    }

    public boolean is_bad_character(char character) {
        return character == '\n' || character == ' '; 
    }

    public ArrayList<Token> scan_code(String source_code) {

        token_list.clear();
        token_pair.clear();

        for(int index = 0; index < source_code.length(); index++) {

            if(source_code.charAt(index) == '\n' || Character.isWhitespace(source_code.charAt(index))) {
                continue; 
            }

            if(Character.isDigit(source_code.charAt(index))) {

                String number_token = ""; 

                while(index < source_code.length() && Character.isDigit(source_code.charAt(index))) {
                    number_token += source_code.charAt(index) + ""; 
                    index++; 
                }
                index--; 
                save_token(number_token, Token.NUMERO);

            }else if(Character.isLetter(source_code.charAt(index))) {

                String id_token = ""; 

                while(index < source_code.length() && Character.isLetter(source_code.charAt(index))) {
                    id_token += source_code.charAt(index); 
                    index++; 
                } 
                index--; 

                id_token = id_token.toLowerCase(); 

                if(token_map.containsKey(id_token)) {
                    save_token(id_token, token_map.get(id_token));
                }else {
                    save_token(id_token, Token.IDENTIFICADOR);
                }

            }else if(source_code.charAt(index) == '=') {
                if(index < source_code.length()-1 && source_code.charAt(index+1) == '=') {
                    save_token("==", Token.COMPARADOR_IGUAL);
                    index++; 
                }else {
                    save_token("=", Token.SIGNO_IGUAL);
                }
            }else if(token_map.containsKey(source_code.charAt(index)+"")){
                save_token(source_code.charAt(index) + "", token_map.get(source_code.charAt(index) + "")); 
            }else {
                save_token(source_code.charAt(index)+"", Token.ERROR);
            }

        }

        return token_list; 
    }

    public void save_token(String str, Token token) {
        token_list.add(token); 
        token_pair.add(new TokenPair(str, token)); 
    }

    public String get_string_tokens() {
        String string_tokens = ""; 
        for(TokenPair p_token : token_pair) {
            Token tok; 
            if(reserved_words.contains(p_token.getToken_str())) {
                tok = Token.PALABRA_RESERVADA; 
            }else {
                tok = p_token.getToken(); 
            }
            string_tokens += "<TKN " + p_token.getToken_str() + " , " + tok + ">\n"; 
        }
        return string_tokens;
    }

    public ArrayList<Token> getTokenList() {
        return token_list;
    }

    public ArrayList<TokenPair> getToken_pair() {
        return token_pair;
    }

}
