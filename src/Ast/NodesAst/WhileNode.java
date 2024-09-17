package Ast.NodesAst;

public class WhileNode extends ASTNode{
    private ExpressionNode expression; 
    public WhileNode(ExpressionNode expression) {
        super(); 
        this.expression = expression; 
    }
    public ExpressionNode getExpression() {
        return expression;
    }
}
