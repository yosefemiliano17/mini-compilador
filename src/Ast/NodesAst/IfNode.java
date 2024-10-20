package Ast.NodesAst;

public class IfNode extends ASTNode{
    private ExpressionNode expression;
    private int id; 
    public IfNode(ExpressionNode expression) {
        super();
        this.expression = expression; 
    }
    public ExpressionNode getExpression() {
        return expression;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
}
