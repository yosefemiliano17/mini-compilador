public class TokenPair {
    private String token_str;
    private Token token; 
    public TokenPair(String token_str, Token token) {
        this.token_str = token_str; 
        this.token = token; 
    }
    public Token getToken() {
        return token;
    }
    public String getToken_str() {
        return token_str;
    }
    public void setToken(Token token) {
        this.token = token;
    }
    public void setToken_str(String token_str) {
        this.token_str = token_str;
    }
}
