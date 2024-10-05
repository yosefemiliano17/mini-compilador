package Syntactic;
import java.util.ArrayList;

import Lexical.Token;
import Lexical.TokenPair;
import Ast.NodesAst.*;

public class Parser {

    private ArrayList<Token> token_list;
    private ArrayList<TokenPair> token_pair; 
    private int next_token; 
    private ASTNode ast_root;

    public Parser(){
        next_token = 0; 
        this.ast_root = new Program(); 
    }

    public Parser(ArrayList<Token> token_list, ArrayList<TokenPair> token_pair) {
        this.token_list = token_list; 
        this.token_pair = token_pair; 
        next_token = 0; 
        this.ast_root = new Program(); 
    }

    public void clean() {
        next_token = 0; 
        this.ast_root = new Program(); 
    }

    public boolean analize_code() {
        return check_program(ast_root); 
    }

    public boolean check_program(ASTNode root) {
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

        if(!declarations_list(ast_root)) {
            return false;
        }

        if(!evaluate(next_token, Token.LLAVE_CIERRE)) {
            return false; 
        }

        return true;
    }

    public boolean declarations_list(ASTNode root) {
        if(evaluate(next_token, Token.LLAVE_CIERRE)) {
            return true; 
        }
        return (check_print(root)          || 
                check_read(root)           || 
                variable_declaration(root) || 
                if_declaration(root)       || 
                variable_assignment(root)  || 
                while_declaration(root))   && 
                declarations_list(root); 
    }

    public boolean check_print(ASTNode root) {

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

        String id = token_pair.get(next_token).getToken_str(); 

        next_token++;

        if(!evaluate(next_token, Token.PARENTESIS_CIERRE)) { 
            return false; 
        }

        next_token++; 

        if(!evaluate(next_token, Token.PUNTO_COMA)) {
            return false; 
        }

        next_token++;

        PrintNode print_node = new PrintNode(id); 

        root.add_neighbor(print_node);

        return true; 
    }

    public boolean expression(ArrayList<TokenPair> arr_expression) {
        if(!value(next_token) && !evaluate(next_token, Token.IDENTIFICADOR)) {
            return false; 
        }
        arr_expression.add(token_pair.get(next_token)); 
        next_token++; 
        if(next_token == token_list.size() || evaluate(next_token, Token.PUNTO_COMA) || evaluate(next_token, Token.PARENTESIS_CIERRE)) {
            return true; 
        }
        if(operator(next_token)) {
            arr_expression.add(token_pair.get(next_token)); 
            next_token++; 
            if(!expression(arr_expression)) {
                return false; 
            }
        }else {
            return false; 
        }
        return true;
    }

    public boolean variable_assignment(ASTNode root) {
        if(!evaluate(next_token, Token.IDENTIFICADOR)) {
            return false; 
        }

        String id = token_pair.get(next_token).getToken_str(); 

        next_token++; 

        if(!evaluate(next_token, Token.SIGNO_IGUAL)) {
            return false; 
        }

        next_token++; 

        ArrayList<TokenPair> arr_expression = new ArrayList<>(); 

        if(!expression(arr_expression)) {
            return false; 
        }

        ExpressionNode expression = new ExpressionNode(arr_expression);

        if(!evaluate(next_token, Token.PUNTO_COMA)) {
            return false; 
        }

        next_token++; 

        VariableAssignmentNode variable_assignment = new VariableAssignmentNode(id, expression); 

        root.add_neighbor(variable_assignment);

        return true; 
    }

    public boolean if_declaration(ASTNode root) {

        if(!evaluate(next_token, Token.IF)) {
            return false;
        }

        next_token++; 

        if(!evaluate(next_token, Token.PARENTESIS_APERTURA)) {
            return false; 
        }

        next_token++; 

        ArrayList<TokenPair> arr_expression = new ArrayList<>(); 

        if(!expression(arr_expression)) {
            return false; 
        }

        ExpressionNode expression = new ExpressionNode(arr_expression); 

        if(!evaluate(next_token, Token.PARENTESIS_CIERRE)) {
            return false; 
        }

        next_token++; 

        if(!evaluate(next_token, Token.LLAVE_APERTURA)) {
            return false; 
        }

        next_token++;

        IfNode if_node = new IfNode(expression); 

        if(!declarations_list(if_node)) {
            return false; 
        }

        root.add_neighbor(if_node);

        if(!evaluate(next_token, Token.LLAVE_CIERRE)) {
            return false; 
        }

        next_token++; 

        return true;
    }

    public boolean while_declaration(ASTNode root) {
        if(!evaluate(next_token, Token.WHILE)) {
            return false;
        }

        next_token++; 

        if(!evaluate(next_token, Token.PARENTESIS_APERTURA)) {
            return false; 
        }

        next_token++; 

        ArrayList<TokenPair> arr_expression = new ArrayList<>(); 

        if(!expression(arr_expression)) {
            return false; 
        }

        ExpressionNode expression = new ExpressionNode(arr_expression); 

        if(!evaluate(next_token, Token.PARENTESIS_CIERRE)) {
            return false; 
        }

        next_token++; 

        if(!evaluate(next_token, Token.LLAVE_APERTURA)) {
            return false; 
        }

        next_token++;

        WhileNode while_node = new WhileNode(expression);

        if(!declarations_list(while_node)) {
            return false; 
        }

        root.add_neighbor(while_node);

        if(!evaluate(next_token, Token.LLAVE_CIERRE)) {
            return false; 
        }

        next_token++; 
        return true; 
    }

    public boolean variable_declaration(ASTNode root) {

        if(!data_type(next_token)) {
            return false; 
        }

        Token data_type = token_list.get(next_token); 

        next_token++; 

        if(!evaluate(next_token, Token.IDENTIFICADOR)) {
            return false; 
        }

        String id = token_pair.get(next_token).getToken_str(); 

        next_token++; 

        if(!evaluate(next_token, Token.PUNTO_COMA)) {
            return false; 
        }

        next_token++;

        VariableDeclarationNode var_declaration = new VariableDeclarationNode(data_type, id); 

        root.add_neighbor(var_declaration);

        return true;
    }

    public boolean check_read(ASTNode root) {

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

        String id = token_pair.get(next_token).getToken_str(); 

        next_token++;
        
        if(!evaluate(next_token, Token.PARENTESIS_CIERRE)) { 
            return false; 
        }

        next_token++; 

        if(!evaluate(next_token, Token.PUNTO_COMA)) {
            return false; 
        }

        next_token++;

        ReadNode read_node = new ReadNode(id); 

        root.add_neighbor(read_node);

        return true; 
    }

    public boolean value(int index) {
        return evaluate(index, Token.NUMERO) || boolean_value(index) || evaluate(index, Token.CADENA);
    }

    public boolean boolean_value(int index) {
        return evaluate(index, Token.FALSE) || evaluate(index, Token.TRUE);
    }

    public boolean data_type(int index) {
        return evaluate(index, Token.BOOLEAN) || evaluate(index, Token.INT) || evaluate(index, Token.STRING); 
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

    public ASTNode getAst_root() {
        return ast_root;
    }

    public void setToken_list(ArrayList<Token> token_list) {
        this.token_list = token_list;
    }

    public void setToken_pair(ArrayList<TokenPair> token_pair) {
        this.token_pair = token_pair;
    }
}