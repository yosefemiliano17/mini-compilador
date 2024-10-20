package IntermediateCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import Lexical.Token;
import Lexical.TokenPair;
import Utils.SymbolInfo;

public class IntermediateCodeGenerator {

    private ArrayList<SymbolInfo> symbols; 
    private String dotCode; 

    public IntermediateCodeGenerator() {
        this.symbols = new ArrayList<>();
        dotCode = "\n";  
    }

    public void generateVariableAssignment(Token type, String id, ArrayList<TokenPair> expression) {
        if(type == Token.INT) {
            if(expression.size() > 1) {
                dotCode += "       MOV AX, " + expression.get(0).getToken_str() + "\n"; 
                for(int i = 1; i < expression.size()-1; i++) {
                    if(expression.get(i).getToken() == Token.SUMA){
                        dotCode += "       ADD AX" + ", " + expression.get(i+1).getToken_str() + "\n";  
                        i++; 
                    }else if(expression.get(i).getToken() == Token.RESTA) {
                        dotCode += "       SUB AX" + ", " + expression.get(i+1).getToken_str() + "\n";  
                        i++; 
                    }
                }
                dotCode += "       MOV " + id + ", AX\n"; 
            }else {
                if(expression.get(0).getToken() == Token.IDENTIFICADOR) {
                    dotCode += "       MOV AX, " + expression.get(0).getToken_str() + '\n'; 
                    dotCode += "       MOV " + id + ", AX\n"; 
                }else {
                    dotCode += "       MOV " + id + ", " + expression.get(0).getToken_str() + "\n"; 
                }
            }
        }else if(type == Token.STRING){
            String value = expression.get(0).getToken_str();
            dotCode += "       MOV [" + id + "], " + "'" + value.charAt(1) + "'" + "\n"; 
            int i = 3; 
            for(; i < value.length(); i++) {
                dotCode += "       MOV [" + id + "+" + (i-2) + "], " + "'" + value.charAt(i-1) + "'" + "\n"; 
            }
            dotCode += "       MOV [" + id + "+" + (i-2) + "], " + "'$'" + "\n"; 
        }

    }

    public void generateIf(int if_id, ArrayList<TokenPair> expression) {
        ArrayList<TokenPair> op1 = new ArrayList<>();
        ArrayList<TokenPair> op2 = new ArrayList<>();  
        boolean flag = false; 
        Token operator = null; 
        for(int i = 0; i < expression.size(); i++) {
            if(expression.get(i).getToken() == Token.MAYOR_QUE) {
                flag = true; 
                operator = Token.MAYOR_QUE; 
                continue; 
            }
            if(expression.get(i).getToken() == Token.MENOR_QUE) {
                flag = true; 
                operator = Token.MENOR_QUE; 
                continue; 
            }
            if(expression.get(i).getToken() == Token.COMPARADOR_IGUAL) {
                flag = true; 
                operator = Token.COMPARADOR_IGUAL; 
                continue; 
            }

            if(!flag) {
                op1.add(expression.get(i)); 
            }else {
                op2.add(expression.get(i)); 
            }
        }

        dotCode += "       MOV AX, " + op1.get(0).getToken_str() + '\n'; 
        for(int i = 1; i < op1.size(); i++) {
            if(op1.get(i).getToken() == Token.SUMA){
                dotCode += "       ADD AX" + ", " + op1.get(i+1).getToken_str() + "\n";  
                i++; 
            }else if(op1.get(i).getToken() == Token.RESTA) {
                dotCode += "       SUB AX" + ", " + op1.get(i+1).getToken_str() + "\n";  
                i++; 
            }
        }
        dotCode += '\n'; 
        dotCode += "       MOV BX, " + op2.get(0).getToken_str() + "\n"; 
        for(int i = 1; i < op2.size(); i++) {
            if(op2.get(i).getToken() == Token.SUMA){
                dotCode += "       ADD BX" + ", " + op2.get(i+1).getToken_str() + "\n";  
                i++; 
            }else if(op2.get(i).getToken() == Token.RESTA) {
                dotCode += "       SUB BX" + ", " + op2.get(i+1).getToken_str() + "\n";  
                i++; 
            }
        }

        dotCode += "       CMP AX, BX\n"; 
        if(operator == Token.MAYOR_QUE) {
            dotCode += "       JLE END_IF" + if_id + "\n"; 
        }else if(operator == Token.MENOR_QUE) {
            dotCode += "       JGE END_IF" + if_id + "\n";
        }else {
            dotCode += "       JNE END_IF" + if_id + "\n"; 
        }
    }

    public void generatePrint(Token type, String id) {
        if(type == Token.STRING) {
            dotCode += "       MOV BX, 0001H\n";
            dotCode += "       LEA DX, " + id + "\n"; 
            dotCode += "       MOV AH, 09H\n"; 
            dotCode += "       INT 21H\n\n"; 
            dotCode += "       MOV AH, 02H\n";
            dotCode += "       MOV DL, 0DH\n"; 
            dotCode += "       INT 21H\n"; 
            dotCode += "       MOV DL, 0AH\n"; 
            dotCode += "       INT 21H\n"; 
        }else {
            //es entero y haz el cagadero
            
        }
    }

    public void generateWhile(int while_id, ArrayList<TokenPair> expression) {
        ArrayList<TokenPair> op1 = new ArrayList<>(); 
        ArrayList<TokenPair> op2 = new ArrayList<>();

        dotCode += "BEGIN_WHILE" + while_id + ":\n"; 
        boolean flag = false; 
        Token operator = null; 
        for(int i = 0; i < expression.size(); i++) {
            if(expression.get(i).getToken() == Token.MAYOR_QUE) {
                flag = true; 
                operator = Token.MAYOR_QUE; 
                continue; 
            }
            if(expression.get(i).getToken() == Token.MENOR_QUE) {
                flag = true; 
                operator = Token.MENOR_QUE; 
                continue; 
            }
            if(expression.get(i).getToken() == Token.COMPARADOR_IGUAL) {
                flag = true; 
                operator = Token.COMPARADOR_IGUAL; 
                continue; 
            }

            if(!flag) {
                op1.add(expression.get(i)); 
            }else {
                op2.add(expression.get(i)); 
            }
        }

        dotCode += "       MOV AX, " + op1.get(0).getToken_str() + '\n'; 
        for(int i = 1; i < op1.size(); i++) {
            if(op1.get(i).getToken() == Token.SUMA){
                dotCode += "       ADD AX" + ", " + op1.get(i+1).getToken_str() + "\n";  
                i++; 
            }else if(op1.get(i).getToken() == Token.RESTA) {
                dotCode += "       SUB AX" + ", " + op1.get(i+1).getToken_str() + "\n";  
                i++; 
            }
        }
        dotCode += '\n'; 
        dotCode += "       MOV BX, " + op2.get(0).getToken_str() + "\n"; 
        for(int i = 1; i < op2.size(); i++) {
            if(op2.get(i).getToken() == Token.SUMA){
                dotCode += "       ADD BX" + ", " + op2.get(i+1).getToken_str() + "\n";  
                i++; 
            }else if(op2.get(i).getToken() == Token.RESTA) {
                dotCode += "       SUB BX" + ", " + op2.get(i+1).getToken_str() + "\n";  
                i++; 
            }
        }

        dotCode += "       CMP AX, BX\n"; 
        if(operator == Token.MAYOR_QUE) {
            dotCode += "       JLE END_WHILE" + while_id + "\n"; 
        }else if(operator == Token.MENOR_QUE) {
            dotCode += "       JGE END_WHILE" + while_id + "\n";
        }else {
            dotCode += "       JNE END_WHILE" + while_id + "\n"; 
        }
    }

    public void generateRead() {

    }

    public String getData() {
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
                    code += id_name + " ".repeat(spaces) + "DB     " + 80 + " dup(\"$\")\n";
                    break;
            }
        }

        code += "       .CODE";

        return code; 
    }

    public String getAllCode() {
        return getData() + dotCode; 
    }

    public String getCode() {
        return dotCode;
    }

    public void setCode(String code) {
        this.dotCode = code;
    }

    public void setSymbols(ArrayList<SymbolInfo> symbols) {
        this.symbols = symbols;
    }
}
