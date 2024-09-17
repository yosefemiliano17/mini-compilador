package Ast.NodesAst;

public class PrintNode extends ASTNode{
    private String id; 
    public PrintNode(String id) {
        super(); 
        this.id = id; 
    }
    public String getId() {
        return id;
    }
}
