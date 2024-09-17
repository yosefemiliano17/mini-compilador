package Utils;
import java.util.HashMap;

public class ScopedSymbolTable {

    private ScopedSymbolTable enclosing_scope;  
    private HashMap<String,SymbolInfo> map; 

    public ScopedSymbolTable(ScopedSymbolTable enclosing_scope) {
        this.map = new HashMap<>(); 
        this.enclosing_scope = enclosing_scope; 
    }

    public void insert(String id, SymbolInfo info) {
        this.map.put(id, info); 
    }

    public SymbolInfo get_symbol_info(String id) {
        ScopedSymbolTable aux_table = this; 
        SymbolInfo info = null; 
        do {
            if(aux_table.map.containsKey(id)) {
                info = aux_table.map.get(id); 
                break;  
            }
            aux_table = aux_table.enclosing_scope; 
        } while (aux_table != null);
        return info; 
    }

    public boolean lookup(String id) {
        ScopedSymbolTable aux_table = this; 
        do {
            if(aux_table.map.containsKey(id)) {
                return true; 
            }
            aux_table = aux_table.enclosing_scope; 
        } while (aux_table != null);
        return false; 
    }

    public void setEnclosing_scope(ScopedSymbolTable enclosing_scope) {
        this.enclosing_scope = enclosing_scope;
    }

    public ScopedSymbolTable getEnclosing_scope() {
        return enclosing_scope;
    }

    public SymbolInfo getInfo(String str) {
        return map.get(str); 
    }

    public HashMap<String, SymbolInfo> getMap() {
        return map;
    }

}
