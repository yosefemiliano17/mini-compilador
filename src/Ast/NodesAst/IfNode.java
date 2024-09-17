package Ast.NodesAst;

public class IfNode extends ASTNode{
    private ExpressionNode expression; 
    public IfNode(ExpressionNode expression) {
        super();
        this.expression = expression; 
    }
    public ExpressionNode getExpression() {
        return expression;
    }
}
