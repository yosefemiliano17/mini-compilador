import java.util.ArrayList;
import java.util.HashMap;

public class Scanner {
    

    private ArrayList<Token> token_list; 
    private ArrayList<TokenPair> token_pair; 
    private HashMap<String, Token> token_map; 

    public Scanner() {
        this.token_list = new ArrayList<>(); 
        this.token_map = new HashMap<>(); 
        this.token_pair = new ArrayList<>(); 

        //palabras reservadas
        token_map.put("class", Token.PALABRA_RESERVADA);
        token_map.put("if", Token.PALABRA_RESERVADA); 
        token_map.put("print", Token.PALABRA_RESERVADA); 
        token_map.put("read", Token.PALABRA_RESERVADA); 
        token_map.put("while", Token.PALABRA_RESERVADA); 
        token_map.put("int", Token.PALABRA_RESERVADA); 
        token_map.put("boolean", Token.PALABRA_RESERVADA); 
        token_map.put("true", Token.PALABRA_RESERVADA); 
        token_map.put("false", Token.PALABRA_RESERVADA); 

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
                //si en el ciclo detecta algo que sea algun numero esta mal
                while(index < source_code.length() && Character.isDigit(source_code.charAt(index))) {
                    number_token += source_code.charAt(index) + ""; 
                    index++; 
                }
                index--; 
                save_token(number_token, Token.NUMERO);

            }else if(Character.isLetter(source_code.charAt(index))) {

                String id_token = ""; 
                //si en el ciclo detecta un numero esta mal
                while(index < source_code.length() && Character.isLetter(source_code.charAt(index))) {
                    id_token += source_code.charAt(index); 
                    index++; 
                } 
                index--; 

                id_token = id_token.toLowerCase(); 

                if(token_map.containsKey(id_token)) {
                    save_token(id_token, Token.PALABRA_RESERVADA);
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
            string_tokens += "<TKN " + p_token.getToken_str() + " , " + p_token.getToken() + ">\n"; 
        }
        return string_tokens;
    }

    public ArrayList<Token> getTokenList() {
        return token_list;
    }

}
