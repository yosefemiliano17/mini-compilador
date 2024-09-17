package Semantic;
import Ast.NodesAst.ASTNode;
import Ast.NodesAst.ExpressionNode;
import Ast.NodesAst.IfNode;
import Ast.NodesAst.PrintNode;
import Ast.NodesAst.ReadNode;
import Ast.NodesAst.VariableAssignmentNode;
import Ast.NodesAst.VariableDeclarationNode;
import Ast.NodesAst.WhileNode;
import Lexical.TokenPair;
import Utils.ScopedSymbolTable;
import Utils.SymbolInfo;
import Lexical.Token;
import java.util.HashSet; 

import java.util.ArrayList;

public class SemanticAnalizer {

    private ScopedSymbolTable current_symbol_table;

    public SemanticAnalizer() {
        this.current_symbol_table = new ScopedSymbolTable(null);
    }

    public boolean check_program(ASTNode node) {

        for(ASTNode neighbor : node.getNeighbors()) {

            if(neighbor instanceof VariableDeclarationNode){

                String id = ((VariableDeclarationNode)neighbor).getId(); 
                Token type = ((VariableDeclarationNode)neighbor).getType();  
                System.out.println(id + " " + type);
                if(!current_symbol_table.lookup(id)) {
                    current_symbol_table.insert(id, new SymbolInfo(id, type));
                }else {
                    return false; 
                }

            }else if(neighbor instanceof VariableAssignmentNode) {

                String id = ((VariableAssignmentNode)neighbor).getId(); 
                
                if(!current_symbol_table.lookup(id)) {
                    return false; 
                } 

                ExpressionNode exp = ((VariableAssignmentNode)neighbor).getExpression(); 
                SymbolInfo info = current_symbol_table.get_symbol_info(id); 
                Token type = info.getType(); 
                System.out.println("var assignment " + id + " " + type);

                if(!check_expression(exp.getExpression(), type)) {
                    return false;
                }

            }else if(neighbor instanceof IfNode) {

                ExpressionNode exp = ((IfNode)neighbor).getExpression(); 
                if(!check_block_expression(exp.getExpression())) {
                    return false; 
                }

                ScopedSymbolTable new_symbol_table = new ScopedSymbolTable(current_symbol_table);
                current_symbol_table = new_symbol_table; 
                if(!check_program(neighbor)) {
                    return false; 
                }
                current_symbol_table = current_symbol_table.getEnclosing_scope();

            }else if(neighbor instanceof WhileNode) {

                ExpressionNode exp = ((WhileNode)neighbor).getExpression(); 
                if(!check_block_expression(exp.getExpression())) {
                    return false; 
                }

                ScopedSymbolTable new_symbol_table = new ScopedSymbolTable(current_symbol_table);
                current_symbol_table = new_symbol_table; 
                if(!check_program(neighbor)) {
                    return false; 
                }
                current_symbol_table = current_symbol_table.getEnclosing_scope();

            }else if(neighbor instanceof PrintNode) {

                String id = ((PrintNode)neighbor).getId(); 
                if(!current_symbol_table.lookup(id)) {
                    return false;
                }

            }else if(neighbor instanceof ReadNode) {

                String id = ((ReadNode)neighbor).getId(); 
                if(!current_symbol_table.lookup(id)) {
                    return false; 
                }

            }
        }
        return true; 
    }

    //checa si los identificadores son todos iguales y ademas checa si existen 
    public boolean check_ids(ArrayList<TokenPair> arr_expression) {
        TokenPair first_token = arr_expression.get(0);
        Token type = current_symbol_table.get_symbol_info(first_token.getToken_str()).getType(); 
        for(TokenPair token : arr_expression) {
            if(token.getToken() == Token.IDENTIFICADOR) {
                SymbolInfo info = current_symbol_table.get_symbol_info(token.getToken_str()); 
                if(info == null || info.getType() != type){
                    return false; 
                }
            }
        }
        return true; 
    }

    public boolean check_block_expression(ArrayList<TokenPair> arr_expression) {
        if(!check_ids(arr_expression)) {
            return false; 
        }
        TokenPair first_token = arr_expression.get(0); 
        if(first_token.getToken() == Token.BOOLEAN) {
            for(TokenPair token : arr_expression) {
                if(token.getToken() != Token.IDENTIFICADOR){
                    if(token.getToken() != Token.COMPARADOR_IGUAL) {
                        return false;
                    }
                }
            }
        }else {
            //INT
            int cont = 0; 
            HashSet<Token> relational_operators = new HashSet<>();
            relational_operators.add(Token.MENOR_QUE); 
            relational_operators.add(Token.MAYOR_QUE); 
            relational_operators.add(Token.COMPARADOR_IGUAL); 
            for(TokenPair token : arr_expression) {
                if(relational_operators.contains(token.getToken())) {
                    cont++; 
                    if(cont > 1) {
                        return false; 
                    }
                }
            }
        }
        return true; 
    }

    public boolean check_expression(ArrayList<TokenPair> arr_expression, Token type) {
        if(type == Token.BOOLEAN) {
            if(arr_expression.size() == 1) {
                TokenPair pair = arr_expression.get(0); 
                if(pair.getToken() == Token.IDENTIFICADOR) {
                    SymbolInfo info = current_symbol_table.getInfo(pair.getToken_str()); 
                    if(info == null || info.getType() != Token.BOOLEAN) {
                        return false; 
                    }
                }
                System.out.println(pair.getToken_str() + " " + pair.getToken());
                if(pair.getToken() != Token.FALSE && pair.getToken() != Token.TRUE) {
                    return false; 
                }
                return true; 
            }else {
                HashSet<Token> tokens = new HashSet<>();
                tokens.add(Token.MENOR_QUE); 
                tokens.add(Token.MAYOR_QUE); 
                tokens.add(Token.COMPARADOR_IGUAL); 
                int cont = 0;
                for(TokenPair tok : arr_expression) {
                    if(tok.getToken() == Token.IDENTIFICADOR) {
                        SymbolInfo info = current_symbol_table.get_symbol_info(tok.getToken_str()); 
                        if(info == null || info.getType() != Token.INT) {
                            return false; 
                        }
                    }
                    if(tokens.contains(tok.getToken())) {
                        cont++; 
                        if(cont > 1) {
                            return false; 
                        }
                    }
                }
                return true; 
            }
        }else {
            //INT
            HashSet<Token> bad_tokens = new HashSet<>();
            bad_tokens.add(Token.MENOR_QUE); 
            bad_tokens.add(Token.MAYOR_QUE); 
            bad_tokens.add(Token.COMPARADOR_IGUAL); 
            bad_tokens.add(Token.FALSE); 
            bad_tokens.add(Token.TRUE); 
            for(TokenPair tok : arr_expression) {
                System.out.println(tok.getToken_str() + " " + tok.getToken());
                if(bad_tokens.contains(tok.getToken())) {
                    System.out.println("entro");
                    return false; 
                }
                if(tok.getToken() == Token.IDENTIFICADOR) {
                    SymbolInfo info = current_symbol_table.get_symbol_info(tok.getToken_str()); 
                    if(info == null || info.getType() != Token.INT) {
                        return false; 
                    }
                }
            }
            return true; 
        }
    }

    public ScopedSymbolTable getCurrent_symbol_table() {
        return current_symbol_table;
    }

}
