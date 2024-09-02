public class Main {
    public static void main(String[] args) {
        App main_app = new App(); 
        Scanner scanner = new Scanner(); 
        ControllerCompiler controller_compiler = new ControllerCompiler(main_app, scanner); 
    }
}
