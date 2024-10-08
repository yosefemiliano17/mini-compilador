package Utils;
import java.util.HashMap;

import Lexical.TokenPair;

import java.util.ArrayList; 

public class ScopedSymbolTable {

    private ScopedSymbolTable enclosing_scope;  
    private HashMap<String,SymbolInfo> map; 
    private ArrayList<ScopedSymbolTable> child_scopes; 
    private ArrayList<SymbolInfo> all_symbols;

    public ScopedSymbolTable(ScopedSymbolTable enclosing_scope) {
        this.map = new HashMap<>(); 
        this.enclosing_scope = enclosing_scope; 
        this.child_scopes = new ArrayList<>(); 
        this.all_symbols = new ArrayList<>(); 
    }

    public void add_child_scope(ScopedSymbolTable sym_tbl) {
        this.child_scopes.add(sym_tbl); 
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

    public void assign_value(String id, ArrayList<TokenPair> value) {
        ScopedSymbolTable aux_table = this; 
        do {
            if(aux_table.map.containsKey(id)) {
                aux_table.map.get(id).setValue(value);
            }
            aux_table = aux_table.enclosing_scope; 
        }while(aux_table != null); 
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

    public ArrayList<ScopedSymbolTable> getChild_scopes() {
        return child_scopes;
    }

    public ArrayList<SymbolInfo> getAll_symbols() {
        return all_symbols;
    }

    public void fill_all_symbols_arr(ScopedSymbolTable table) {
        for(SymbolInfo info : table.getMap().values()) {
            all_symbols.add(info); 
        }
        for (ScopedSymbolTable childs : table.child_scopes) {
            fill_all_symbols_arr(childs);
        }
    }

}
