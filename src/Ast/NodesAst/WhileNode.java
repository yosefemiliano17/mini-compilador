package Ast.NodesAst;

public class WhileNode extends ASTNode{
    private ExpressionNode expression; 
    private int id; 
    public WhileNode(ExpressionNode expression) {
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
