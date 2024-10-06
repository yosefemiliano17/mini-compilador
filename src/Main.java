import Lexical.Scanner;
import Semantic.SemanticAnalizer;
import Syntactic.Parser;

public class Main {

    public static void main(String[] args) {
        App main_app = new App(); 
        Scanner scanner = new Scanner(); 
        Parser parser = new Parser(); 
        SemanticAnalizer semantic = new SemanticAnalizer(); 
        ControllerCompiler controller_compiler = new ControllerCompiler(main_app, scanner, parser, semantic); 
    }
}
