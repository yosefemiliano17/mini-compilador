package ObjectCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ObjectCodeGenerator {

    private String intermediate_code; 
    private HashMap<String,RegInfo> reg; 
    private HashMap<String,HashMap<InstructionFormat,String>> encodings; 
    private HashMap<String,String> jumps; 
    private HashMap<String,Integer> offsets; 

    private final String CBW_ENCODING = "1001 1000"; 
    private final String INT_21H = "1100 1101 0010 0001"; 

    public ObjectCodeGenerator(String intermediate_code) {
        this.intermediate_code = intermediate_code;
        this.reg = new HashMap<>(); 
        this.encodings = new HashMap<>(); 
        this.jumps = new HashMap<>(); 
        this.offsets = new HashMap<>();

        reg.put("AX", new RegInfo("11", "000", '1', "000"));
        reg.put("AL", new RegInfo("11", "000", '0', "000"));
        reg.put("BX", new RegInfo("11", "011", '1', "011"));
        reg.put("BL", new RegInfo("11", "011", '0', "011"));
        reg.put("DX", new RegInfo("11", "010", '1', "010"));
        reg.put("DL", new RegInfo("11", "010", '0', "010"));

        encodings.put("MOV", new HashMap<>()); 
        encodings.get("MOV").put(InstructionFormat.REG_MEM, "1000 101 w mod reg r/m"); 
        encodings.get("MOV").put(InstructionFormat.REG_IMM, "1100 011 w 11 000 reg imm"); 
        encodings.get("MOV").put(InstructionFormat.MEM_REG, "1000 100 w mod reg r/m"); 
        encodings.get("MOV").put(InstructionFormat.MEM_IMM, "1100 011 w mod 000 r/m imm"); 
        encodings.get("MOV").put(InstructionFormat.AREG_MEM, "1010 000 w fd"); 

        //fd : full displacement
        //imm : immediate data
        encodings.put("ADD", new HashMap<>()); 
        encodings.get("ADD").put(InstructionFormat.REG_MEM, "0000 001 w mod reg r/m"); 
        encodings.get("ADD").put(InstructionFormat.REG_IMM, "1000 001 w 11 000 reg imm");
        encodings.get("ADD").put(InstructionFormat.AREG_IMM, "0000 010 w imm");

        encodings.put("SUB", new HashMap<>()); 
        encodings.get("SUB").put(InstructionFormat.REG_MEM, "0010 101 w mod reg r/m"); 
        encodings.get("SUB").put(InstructionFormat.REG_IMM, "1000 001 w 11 101 reg imm");
        encodings.get("SUB").put(InstructionFormat.AREG_IMM, "0010 110 w imm");

        encodings.put("CMP", new HashMap<>()); 
        encodings.get("CMP").put(InstructionFormat.REG_REG, "0011 1001 11 reg1 reg2"); 

        encodings.put("DIV", new HashMap<>());
        encodings.get("DIV").put(InstructionFormat.REG, "1111 0110 11 110 reg"); 

        encodings.put("LEA", new HashMap<>());
        //encodings.get("LEA").put(, "");
    }

    public boolean binary_string(String str) {
        for(int i = 0; i < str.length(); i++) {
            if(str.charAt(i) != '0' && str.charAt(i) != '1') {
                return false; 
            }
        }
        return true; 
    }

    public String get_data() {
        String dotData = "";
        String[] aux = intermediate_code.split("\n"); 
        for(int i = 1; i < aux.length-1; i++) {
            aux[i] = aux[i].trim(); 
            dotData += aux[i].trim() + '\n'; 
            if(aux[i+1].trim().equals(".CODE")) {
                break; 
            }
        }
        return dotData; 
    }

    public String get_code() {
        String dotCode = ""; 
        String[] aux = intermediate_code.split("\n"); 
        boolean code = false; 
        for(int i = 0; i < aux.length; i++) {
            aux[i] = aux[i].trim(); 
            if(aux[i].equals(".CODE")) {
                code = true;
                i++;  
            }
            if(code) {
                dotCode += aux[i].trim() + '\n'; 
            }
        }
        return dotCode; 
    }

    public String get_encoding(String operation, InstructionFormat type, int offset) {
        String prefix = "00A0 : " + get_hex(offset) + " "; 
        String binary = ""; 
        if(operation.equals("CBW")) {
            binary += CBW_ENCODING; 
            return binary; 
        }
        if(operation.equals("INT 21H")) {
            binary += INT_21H; 
            return binary; 
        }

        String[] operation_arr = operation.split("\\s+"); 

        if(operation_arr[0].equals("MOV")) {
            operation_arr[1] = operation_arr[1].substring(0,operation_arr[1].length()-1); 
            if(type == InstructionFormat.REG_MEM) {
                char w = (operation_arr[1].charAt(1) == 'L') ? '0' : '1'; 
                String regi = reg.get(operation_arr[1]).getBin_reg(); 
                String r_m = reg.get(operation_arr[1]).getR_m();
                String str_offset = get_offset(offsets.get(operation_arr[2])); 
                binary += "1000101" + w + "11" + regi + r_m + str_offset; 
            }
            if(type == InstructionFormat.REG_IMM) {
                String regi = reg.get(operation_arr[1]).getBin_reg(); 
                int w = (operation_arr[1].charAt(operation_arr[1].length()-1) == 'L') ? 0 : 1; 
                int val; 
                String val_str = ""; 
                if(operation_arr[2].charAt(operation_arr[2].length()-1) == 'H') {
                    val_str = hexToBin(operation_arr[2].substring(0,operation_arr[2].length()-1)); 
                }else {
                    val = Integer.parseInt(operation_arr[2]); 
                    val_str = get_bin(val, w); 
                }
                binary += "1100011111000" + regi + val_str;
            }
            if(type == InstructionFormat.MEM_REG) {
                String regi = reg.get(operation_arr[2]).getBin_reg(); 
                String r_m = reg.get(operation_arr[2]).getR_m(); 
                String str_offset = get_offset(offsets.get(operation_arr[1]));

                binary += "1000100111" + regi + r_m + str_offset; 
            }
            if(type == InstructionFormat.MEM_IMM) {
                char w = (is_char(operation_arr[2])) ? '0' : '1'; 
                String str_offset = ""; 
                int val = 0; 
                //System.out.println(is_char(operation_arr[2]) + " " + operation_arr[2]);
                if(is_char(operation_arr[2])) {
                    String op_aux = operation_arr[1].substring(1,operation_arr[1].length()-1); 
                    String[] op_aux_arr = op_aux.split("\\+"); 
                    val = operation_arr[2].charAt(1); 
                    if (op_aux_arr.length == 1) {
                        str_offset = get_offset(offsets.get(op_aux_arr[0])); 
                    }else {
                        str_offset = get_offset(offsets.get(op_aux_arr[0]) + Integer.parseInt(op_aux_arr[1])); 
                    }
                    
                }else {
                    str_offset = get_offset(offsets.get(operation_arr[1]));
                    val = Integer.parseInt(operation_arr[2]); 
                }
                binary += "1100011" + w + "00000101" + str_offset + get_bin(val, w - '0');
            }
            if(type == InstructionFormat.AREG_MEM) {
                String str_offset = get_offset(offsets.get(operation_arr[2]));
                binary += "10100001" + str_offset; 
            }
        }
        if(operation_arr[0].equals("ADD")) {
            operation_arr[1] = operation_arr[1].substring(0,operation_arr[1].length()-1); 
            if(type == InstructionFormat.REG_MEM) { 
                String regi = reg.get(operation_arr[1]).getBin_reg(); 
                String r_m = reg.get(operation_arr[1]).getR_m();
                String str_offset = get_offset(offsets.get(operation_arr[2])); 
                binary += "0000001100" + regi + r_m + str_offset; 
            }
            if(type == InstructionFormat.REG_IMM) {
                String regi = reg.get(operation_arr[1]).getBin_reg(); 
                int val = Integer.parseInt(operation_arr[2]); 
                binary += "1000001111000" + regi + get_bin(val, 1); 
            }
            if(is_areg(operation_arr[1]) && is_mem(operation_arr[2])) {
                String str_offset = get_offset(offsets.get(operation_arr[2])); 
                binary += "10100001" + str_offset; 
            }
            if(type == InstructionFormat.AREG_IMM) {
                int val = Integer.parseInt(operation_arr[2]); 
                binary += "00000101" + get_bin(val, 1); 
            }
        }
        if(operation_arr[0].equals("SUB")) {
            operation_arr[1] = operation_arr[1].substring(0,operation_arr[1].length()-1); 
            if(type == InstructionFormat.REG_MEM) {
                String regi = reg.get(operation_arr[1]).getBin_reg(); 
                String r_m = reg.get(operation_arr[1]).getR_m();
                String str_offset = get_offset(offsets.get(operation_arr[2])); 
                binary += "0010101100" + regi+ r_m + str_offset; 
            }
            if(type == InstructionFormat.REG_IMM) {
                String regi = reg.get(operation_arr[1]).getBin_reg(); 
                int val = Integer.parseInt(operation_arr[2]); 
                binary += "1000001111101" + regi+ get_bin(val, 1); 
            }
            if(type == InstructionFormat.AREG_IMM) {
                int val = Integer.parseInt(operation_arr[2]); 
                binary += "00101101" + get_bin(val, 1);    
            }
        }
        if(operation_arr[0].equals("CMP")) {
            binary += "0011100111000011"; 
        }
        if(operation_arr[0].equals("DIV")) {
            binary += "1111011011110010";
        }
        if(operation_arr[0].equals("JNE"))  {
            binary += "01110101"; 
        }
        if(operation_arr[0].equals("JGE")) {
            binary += "01111101"; 
        }
        if(operation_arr[0].equals("JLE")) {
            binary += "01111110"; 
        }
        if(binary.isEmpty()) {
            return prefix; 
        }
        return prefix + group_nibbles(binary); 
    }

    public boolean is_label(String str) {
        return str.charAt(str.length()-1) == ':' ;
    }

    public String generate() {
        String object_code = ""; 
        String[] data = get_data().split("\n"); 
        int offset_data = 0; 
        for(int i = 0; i < data.length; i++) {
            String[] instruction = data[i].split("\\s+"); 
            if(instruction.length == 3){
                if(instruction[1].equals("DB")) {
                    object_code += "0000 : " + get_hex(offset_data);
                    if(instruction[2].equals("?")) {
                        object_code += " 0000 0000\n"; 
                    }else {
                        object_code += (instruction[0].equals("true") ? " 0000 0001\n" : " 0000 0000\n"); 
                    }
                    offset_data += 1; 
                }else {
                    object_code += "0000 : " + get_hex(offset_data) + " 0000 0000 0000 0000\n";
                    offset_data += 2; 
                }
            }else {
                object_code += "0000 : " + get_hex(offset_data);
                int len = Integer.parseInt(instruction[2]); 
                for(int x = 0; x < len; x++) {
                    object_code += " 0010 0100 "; 
                }
                object_code += '\n'; 
                offset_data += len; 
            }

            offsets.put(instruction[0], offset_data); 
        }


        String[] code = get_code().split("\n"); 
        int offset_code = 0, start = -1, end = -1; 
        int pos = 0; 
        ArrayList<String> binary_code = new ArrayList<>(); 
        for(int i = 0; i < code.length; i++) {
            if(code[i].isEmpty()) {
                continue; 
            }

            InstructionFormat format = get_instruction_format(code[i]); 

            String[] code_arr = code[i].split("\\s+"); 

            if(is_jcc(code_arr[0])) {
                start = offset_code; 
                pos = i; 
            }

            if(is_label(code_arr[0])) {
                end = offset_code; 
            }

            String encoding = get_encoding(code[i], format, offset_code); 

            binary_code.add(encoding); 


            //object_code += encoding; 
            offset_code += bytes(encoding); 
        }

        if(start > -1) {
            int jump = end - start - 1; 
            String jump_bytes = get_bin(jump, 0);
            binary_code.set(pos-1, binary_code.get(pos-1) + " " + group_nibbles(jump_bytes)); 
        }
        String answer = ""; 
        for (String string : binary_code) {
            answer += string + '\n'; 
        }
        return object_code + "\n" + answer;  
    }

    public boolean is_jcc(String op) {
        return op.equals("JNE") || op.equals("JLE") || op.equals("JGE"); 
    }

    public String getIntermediate_code() {
        return intermediate_code;
    }

    public InstructionFormat get_instruction_format(String instruction) {
        String[] op_args = instruction.split("\\s+"); 
        if(op_args.length == 3) {
            op_args[1] = op_args[1].substring(0,op_args[1].length()-1);
            if(is_areg(op_args[1]) && is_mem(op_args[2])) {
                return InstructionFormat.AREG_MEM; 
            }
            if(is_areg(op_args[1]) && is_imm(op_args[2])) {
                return InstructionFormat.AREG_IMM; 
            }
            if(is_register(op_args[1]) && is_register(op_args[2])) {
                return InstructionFormat.REG_REG; 
            }
            if(is_register(op_args[1]) && is_imm(op_args[2])) {
                return InstructionFormat.REG_IMM; 
            }
            if(is_register(op_args[1]) && is_mem(op_args[2])) {
                return InstructionFormat.REG_MEM;
            }
            if(is_mem(op_args[1]) && is_register(op_args[2])) {
                return InstructionFormat.MEM_REG; 
            }
            if(is_mem(op_args[1]) && is_imm(op_args[2])) {
                return InstructionFormat.MEM_IMM;
            }
        }else if(op_args.length == 2) {
            if (is_register(op_args[1])) {
                return InstructionFormat.REG; 
            }
        }
        return null; 
    }

    private boolean is_register(String arg) {
        HashSet<String> registers = new HashSet<>();
        registers.add("AX"); 
        registers.add("AL"); 
        registers.add("BX"); 
        registers.add("BL"); 
        registers.add("DL"); 
        registers.add("DX");  
        return registers.contains(arg); 
    }

    private boolean is_areg(String arg) {
        return arg.equals("AX") || arg.equals("AL");    
    }

    private boolean is_imm(String arg) {
        return is_char(arg) || is_number(arg) || arg.charAt(arg.length()-1) == 'H'; 
    }

    private boolean is_mem(String arg) {
        if(offsets.containsKey(arg) || arg.indexOf("[") > -1) {
            return true; 
        }
        return false; 
    }

    private boolean is_number(String str) {
        for(int i = 0; i < str.length(); i++) {
            if(!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true; 
    }

    private boolean is_char(String str) {
        if(str.length() == 3 && str.charAt(0) == '\'' && str.charAt(2) == '\'') {
            return true;
        }
        return false; 
    }

    private String get_hex(int num) {
        String aux_hex = Integer.toHexString(num); 
        int len = 4 - aux_hex.length();
        String zeros = ""; 
        for(int i = 0; i < len; i++) {
            zeros += '0'; 
        }
        return zeros + aux_hex; 
    }

    private String get_bin(int num, int w) {
        String aux_bin = Integer.toBinaryString(num); 
        int len; 
        if(w == 1) {
            len = 16 - aux_bin.length(); 
        }else {
            len = 8 - aux_bin.length(); 
        }
        String zeros = ""; 
        for(int i = 0; i < len; i++) {
            zeros += '0'; 
        }

        return little_en(zeros + aux_bin); 
    }

    public String little_en(String str) {
        String resp = "";
        str += "$";
        ArrayList<String> arr = new ArrayList<>(); 
        for(int i = 1; i <= str.length(); i++) {
            if(i % 8 == 0) {
                resp += str.charAt(i-1); 
                arr.add(resp); 
                resp = ""; 
                continue; 
            }
            resp += str.charAt(i-1); 

        } 
        resp = "";
        for (int i = arr.size()-1; i >= 0; i--) {
            resp += arr.get(i); 
        }
        return resp; 
    }

    private String group_nibbles(String str) {
        String resp = ""; 
        for(int i = 1; i <= str.length(); i++) {
            if(i % 4 == 0) {
                resp += str.charAt(i-1) ;
                resp += " ";
            }else {
                resp += str.charAt(i-1); 
            }
        }
        return resp; 
    }

    private int bytes(String encoding) {
        int cont = 0;
        int cont2 = 0;  
        for(int i = 0; i < encoding.length(); i++) {
            if(encoding.charAt(i) != ' ' && encoding.charAt(i) != ':') {
                cont++; 
            }
            if(cont > 8){
                if(encoding.charAt(i) == '0' || encoding.charAt(i) == '1') {
                    cont2++; 
                }
            }
        }
        return cont2 / 8;
    }

    public String get_offset(int offset) {
        String aux_hex = Integer.toHexString(offset); 
        int len = 4 - aux_hex.length();
        String zeros = ""; 
        for(int i = 0; i < len; i++) {
            zeros += '0'; 
        }
        String complete_hex = zeros + aux_hex; 
        return little_en(hexToBin(complete_hex));
    }

























    private String hexToBin(String hex){
        hex = hex.replaceAll("0", "0000");
        hex = hex.replaceAll("1", "0001");
        hex = hex.replaceAll("2", "0010");
        hex = hex.replaceAll("3", "0011");
        hex = hex.replaceAll("4", "0100");
        hex = hex.replaceAll("5", "0101");
        hex = hex.replaceAll("6", "0110");
        hex = hex.replaceAll("7", "0111");
        hex = hex.replaceAll("8", "1000");
        hex = hex.replaceAll("9", "1001");
        hex = hex.replaceAll("a", "1010");
        hex = hex.replaceAll("b", "1011");
        hex = hex.replaceAll("c", "1100");
        hex = hex.replaceAll("d", "1101");
        hex = hex.replaceAll("e", "1110");
        hex = hex.replaceAll("f", "1111");
        return hex;
    }
}