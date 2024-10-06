package IntermediateCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import Lexical.Token;
import Utils.SymbolInfo;

public class IntermediateCodeGenerator {

    private ArrayList<SymbolInfo> symbols; 

    public IntermediateCodeGenerator() {
        this.symbols = new ArrayList<>(); 
    }

    public String getCode() {
        String code = ""; 

        Collections.sort(symbols, new Comparator<SymbolInfo>() {
            @Override
            public int compare(SymbolInfo o1, SymbolInfo o2) {
                int len1 = o1.getId().length();
                int len2 = o1.getUnique_name().length(); 
                int len3 = o2.getId().length(); 
                int len4 = o2.getUnique_name().length(); 
                return Integer.compare(Math.max(len3,len4), Math.max(len1,len2));
            }
        });

        int max_len = Math.max(symbols.get(0).getId().length(), symbols.get(0).getUnique_name().length()); 

        code += "       .DATA\n";

        for (SymbolInfo symbol : symbols) {
            int len1 = symbol.getId().length(); 
            int len2 = symbol.getUnique_name().length(); 
            int spaces = max_len - Math.max(len1, len2) + 5; 
            String id_name = (!symbol.getId().equals(symbol.getUnique_name())) ? symbol.getUnique_name() : symbol.getId(); 
            switch (symbol.getType()) {
                case Token.INT:
                    code += id_name + " ".repeat(spaces) + "DW     ?\n"; 
                    break;
                case Token.BOOLEAN:
                    code += id_name + " ".repeat(spaces) + "DB     ?\n"; 
                    break;
                case Token.STRING:
                    int str_len = symbol.getValue().get(0).getToken_str().length(); 
                    code += id_name + " ".repeat(spaces) + "DB     " + str_len + " dup(\"$\")\n";
                    break;
            }
        }

        code += "       .CODE";

        return code; 
    }

    public void setSymbols(ArrayList<SymbolInfo> symbols) {
        this.symbols = symbols;
    }
}
