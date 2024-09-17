package Ast.NodesAst;

import java.util.ArrayList;

import Lexical.TokenPair;

public class ExpressionNode extends ASTNode{
    
    private ArrayList<TokenPair> expression;

    public ExpressionNode(ArrayList<TokenPair> expression) {
        super();
        this.expression = expression; 
    }

    public ArrayList<TokenPair> getExpression() {
        return expression;
    }
}
