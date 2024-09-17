package Utils;

import Lexical.*;

public class SymbolInfo {
    private String id; 
    private Token type; 

    public SymbolInfo(String id, Token type) {
        this.id = id; 
        this.type = type; 
    }

    public String getId() {
        return id;
    }

    public Token getType() {
        return type;
    }
}
