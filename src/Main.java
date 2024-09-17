import Lexical.Scanner;
import Syntactic.Parser;

public class Main {

    public static void main(String[] args) {
        App main_app = new App(); 
        Scanner scanner = new Scanner(); 
        Parser parser = new Parser(); 
        ControllerCompiler controller_compiler = new ControllerCompiler(main_app, scanner, parser); 
    }
}
