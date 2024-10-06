package Utils;

import Lexical.*;
import java.util.ArrayList; 

public class SymbolInfo {
    private String id; 
    private Token type; 
    private ArrayList<TokenPair> value;
    private String unique_name; 

    public SymbolInfo(String id, Token type, String unique_name) {
        this.id = id; 
        this.type = type; 
        this.unique_name = unique_name; 
    }

    public String getId() {
        return id;
    }

    public Token getType() {
        return type;
    }

    public ArrayList<TokenPair> getValue() {
        return value;
    }

    public void setValue(ArrayList<TokenPair> value) {
        this.value = value;
    }

    public String getUnique_name() {
        return unique_name;
    }

    public void setUnique_name(String unique_name) {
        this.unique_name = unique_name;
    }
}
