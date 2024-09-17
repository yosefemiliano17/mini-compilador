package Ast.NodesAst;

import Lexical.Token;

public class VariableDeclarationNode extends ASTNode{
    private Token type;
    private String id; 
    public VariableDeclarationNode(Token type, String id) {
        super(); 
        this.id = id; 
        this.type = type; 
    }
    public Token getType() {
        return type;
    }
    public String getId() {
        return id;
    }
}
