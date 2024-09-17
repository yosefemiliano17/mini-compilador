package Ast.NodesAst;

public class VariableAssignmentNode extends ASTNode{
    private String id; 
    private ExpressionNode expression; 
    public VariableAssignmentNode(String id, ExpressionNode expression) {
        super(); 
        this.id = id; 
        this.expression = expression; 
    }
    public String getId() {
        return id;
    }
    public ExpressionNode getExpression() {
        return expression;
    }
}
