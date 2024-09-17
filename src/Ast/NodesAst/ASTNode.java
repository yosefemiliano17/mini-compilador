package Ast.NodesAst;

import java.util.ArrayList;

import Lexical.TokenPair; 

public class ASTNode {

    private ArrayList<ASTNode> neighbors; 

    public ASTNode() { 
        this.neighbors = new ArrayList<>(); 
    }

    public ArrayList<ASTNode> getNeighbors() {
        return neighbors;
    }

    public void add_neighbor(ASTNode node) {
        this.neighbors.add(node); 
    }

    public static void traverse(ASTNode nodev) {
        if(nodev instanceof IfNode) {
            System.out.println("IF");
            System.out.println("IF EXPRESSION");
            ExpressionNode expression = ((IfNode)nodev).getExpression();

            for(TokenPair tok : expression.getExpression()) {
                System.out.println("      " + tok.getToken_str());
            }
        }else if(nodev instanceof PrintNode) {
            System.out.println("PRINT");
        }else if(nodev instanceof Program) {
            System.out.println("PROGRAM");
        }else if(nodev instanceof ReadNode) {
            System.out.println("READ");
        }else if(nodev instanceof VariableAssignmentNode) {
            System.out.println("VAR ASSIGNMENT");
        }else if(nodev instanceof VariableDeclarationNode) {
            System.out.println("VAR DECLARATION");
        }else if(nodev instanceof WhileNode) {
            System.out.println("WHILE");
            System.out.println("WHILE EXPRESSION");
            ExpressionNode expression = ((WhileNode)nodev).getExpression();

            for(TokenPair tok : expression.getExpression()) {
                System.out.println("      " + tok.getToken_str());
            }
        }
        for(ASTNode nodew : nodev.neighbors) {
            traverse(nodew);
        }
    }
}
