package Ast.NodesAst;

public class ReadNode extends ASTNode{
    private String id; 
    public ReadNode(String id) {
        super(); 
        this.id = id; 
    }
    public String getId() {
        return id;
    }
}
