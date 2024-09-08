import java.util.ArrayList; 

public class Parser {

    private ArrayList<Token> token_list;
    private int next_token; 

    public Parser(ArrayList<Token> token_list) {
        this.token_list = token_list; 
        next_token = 0; 
    }

    public boolean analize_code() {
        return check_program(); 
    }

    public boolean check_program() {
        if(!evaluate(next_token, Token.CLASS)) {
            return false; 
        }

        next_token++; 

        if(!evaluate(next_token, Token.IDENTIFICADOR)) {
            return false; 
        }

        next_token++; 

        if(!evaluate(next_token, Token.LLAVE_APERTURA)) {
            return false;
        }

        next_token++; 

        if(!declarations_list()) {
            return false;
        }

        if(!evaluate(next_token, Token.LLAVE_CIERRE)) {
            return false; 
        }

        return true;
    }

    public boolean declarations_list() {
        if(evaluate(next_token, Token.LLAVE_CIERRE)) {
            return true; 
        }
        return (check_print()          || 
                check_read()           || 
                variable_declaration() || 
                if_declaration()       || 
                variable_assignment()  || 
                while_declaration())   && 
                declarations_list(); 
    }

    public boolean check_print() {

        if(!evaluate(next_token, Token.PRINT)) { 
            return false;
        }

        next_token++; 

        if(!evaluate(next_token, Token.PARENTESIS_APERTURA)) { 
            return false; 
        }

        next_token++; 

        if(!evaluate(next_token, Token.IDENTIFICADOR)) { 
            return false;
        }

        next_token++;

        if(!evaluate(next_token, Token.PARENTESIS_CIERRE)) { 
            return false; 
        }

        next_token++; 

        if(!evaluate(next_token, Token.PUNTO_COMA)) {
            return false; 
        }

        next_token++;

        return true; 
    }

    public boolean expression() {
        if(!value(next_token) && !evaluate(next_token, Token.IDENTIFICADOR)) {
            return false; 
        }
        next_token++; 
        if(next_token == token_list.size() || evaluate(next_token, Token.PUNTO_COMA) || evaluate(next_token, Token.PARENTESIS_CIERRE)) {
            return true; 
        }
        if(operator(next_token)) {
            next_token++; 
            if(!expression()) {
                return false; 
            }
        }else {
            return false; 
        }
        return true;
    }

    public boolean variable_assignment() {
        if(!evaluate(next_token, Token.IDENTIFICADOR)) {
            return false; 
        }

        next_token++; 

        if(!evaluate(next_token, Token.SIGNO_IGUAL)) {
            return false; 
        }

        next_token++; 

        if(!expression()) {
            return false; 
        }

        if(!evaluate(next_token, Token.PUNTO_COMA)) {
            return false; 
        }

        next_token++; 

        return true; 
    }

    public boolean if_declaration() {

        if(!evaluate(next_token, Token.IF)) {
            return false;
        }

        next_token++; 

        if(!evaluate(next_token, Token.PARENTESIS_APERTURA)) {
            return false; 
        }

        next_token++; 

        if(!expression()) {
            return false; 
        }

        if(!evaluate(next_token, Token.PARENTESIS_CIERRE)) {
            return false; 
        }

        next_token++; 

        if(!evaluate(next_token, Token.LLAVE_APERTURA)) {
            return false; 
        }

        next_token++;

        if(!declarations_list()) {
            return false; 
        }

        if(!evaluate(next_token, Token.LLAVE_CIERRE)) {
            return false; 
        }

        next_token++; 

        return true;
    }

    public boolean while_declaration() {
        if(!evaluate(next_token, Token.WHILE)) {
            return false;
        }

        next_token++; 

        if(!evaluate(next_token, Token.PARENTESIS_APERTURA)) {
            return false; 
        }

        next_token++; 

        if(!expression()) {
            return false; 
        }

        if(!evaluate(next_token, Token.PARENTESIS_CIERRE)) {
            return false; 
        }

        next_token++; 

        if(!evaluate(next_token, Token.LLAVE_APERTURA)) {
            return false; 
        }

        next_token++;

        if(!declarations_list()) {
            return false; 
        }

        if(!evaluate(next_token, Token.LLAVE_CIERRE)) {
            return false; 
        }

        next_token++; 
        return true; 
    }

    public boolean variable_declaration() {

        if(!data_type(next_token)) {
            return false; 
        }

        next_token++; 

        if(!evaluate(next_token, Token.IDENTIFICADOR)) {
            return false; 
        }

        next_token++; 

        if(!evaluate(next_token, Token.PUNTO_COMA)) {
            return false; 
        }

        next_token++;

        return true;
    }

    public boolean check_read() {

        if(!evaluate(next_token, Token.READ)) { 
            return false;
        }

        next_token++; 

        if(!evaluate(next_token, Token.PARENTESIS_APERTURA)) { 
            return false; 
        }

        next_token++; 

        if(!evaluate(next_token, Token.IDENTIFICADOR)) { 
            return false;
        }

        next_token++;
        
        if(!evaluate(next_token, Token.PARENTESIS_CIERRE)) { 
            return false; 
        }

        next_token++; 

        if(!evaluate(next_token, Token.PUNTO_COMA)) {
            return false; 
        }

        next_token++;

        return true; 
    }

    public boolean value(int index) {
        return evaluate(index, Token.NUMERO) || boolean_value(index);
    }

    public boolean boolean_value(int index) {
        return evaluate(index, Token.FALSE) || evaluate(index, Token.TRUE);
    }

    public boolean data_type(int index) {
        return evaluate(index, Token.BOOLEAN) || evaluate(index, Token.INT); 
    }

    public boolean operator(int index) {
        return  evaluate(index, Token.COMPARADOR_IGUAL) ||
                evaluate(index, Token.MAYOR_QUE)        ||
                evaluate(index, Token.MENOR_QUE)        ||
                evaluate(index, Token.SUMA)             ||
                evaluate(index, Token.RESTA); 
    }

    public boolean evaluate(int index, Token token) {
        return index < token_list.size() && token_list.get(index).equals(token); 
    }
}