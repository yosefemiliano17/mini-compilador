package ObjectCode;

public class RegInfo {
    private String mod, bin_reg, r_m; 
    private char w; 
    public RegInfo(String mod, String bin_reg, char w, String r_m) {
        this.mod = mod;
        this.bin_reg = bin_reg; 
        this.w = w; 
        this.r_m = r_m; 
    }
    public String getBin_reg() {
        return bin_reg;
    }
    public String getMod() {
        return mod;
    }
    public char getW() {
        return w;
    }
    public String getR_m() {
        return r_m;
    }
    public void setBin_reg(String bin_reg) {
        this.bin_reg = bin_reg;
    }
    public void setMod(String mod) {
        this.mod = mod;
    }
    public void setW(char w) {
        this.w = w;
    }
    public void setR_m(String r_m) {
        this.r_m = r_m;
    }
}
