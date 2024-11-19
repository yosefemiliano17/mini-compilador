import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

import javax.swing.*;

import Lexical.Scanner;
import ObjectCode.ObjectCodeGenerator;
import Semantic.SemanticAnalizer;
import Syntactic.Parser;
import Utils.ScopedSymbolTable;

public class ControllerCompiler implements ActionListener{

    private App view_app; 
    private Scanner scanner; 
    private SemanticAnalizer semantic; 
    private Parser parser; 
    
    public ControllerCompiler(App view_app, Scanner scanner, Parser parser, SemanticAnalizer semantic) {
        this.view_app = view_app; 
        this.scanner = scanner; 
        this.parser = parser; 
        this.semantic = semantic; 
        this.view_app.add_scanner_listeners(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == view_app.getScanner_btn()) {
            scanner.scan_code(view_app.getCode_area().getText()); 
            view_app.getTokens_area().setText(scanner.get_string_tokens());
            this.parser.clean();
            view_app.revalidate();
            view_app.repaint(); 
            return; 
        }
        if(e.getSource() == view_app.getFile_btn()) {
            int option = view_app.getFile_chooser().showOpenDialog(view_app); 
            if(option == JFileChooser.APPROVE_OPTION) {
                File file = view_app.getFile_chooser().getSelectedFile(); 
                try {
                    String source_code = FileReader.readFile(file); 
                    view_app.getCode_area().setText(source_code);
                    view_app.getTokens_area().setText("");
                    view_app.getParser_area().setText("");
                    view_app.getSemantic_Area().setText("");
                    this.parser.clean(); 
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
            return; 
        }
        if(e.getSource() == view_app.getSave_code_btn()) {
            String source_code = view_app.getCode_area().getText(); 
            view_app.getFile_chooser().showOpenDialog(null); 
            view_app.getFile_chooser().setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            
            File file = view_app.getFile_chooser().getSelectedFile(); 
            if(file == null) {
                JOptionPane.showMessageDialog(view_app, 
                "El archivo no se ha guardado", 
                "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            FileWriter writer; 
            try {
                writer = new FileWriter(file,false); 
                writer.write(source_code);
                writer.close(); 
            } catch (FileNotFoundException ex) {
                // TODO: handle exception
                JOptionPane.showMessageDialog(view_app, "Error al guardar, poner nombre al archivo");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(view_app, "Error al guardar, en la salida");
            }

            return; 
        }
        if(e.getSource() == view_app.getParser_btn()) {
            parser.setToken_list(scanner.getTokenList());
            parser.setToken_pair(scanner.getToken_pair());
            if(parser.analize_code()) {
                view_app.getParser_area().setText("OK");
                /*ASTNode node = parser.getAst_root(); 
                ASTNode.traverse(node);*/
                view_app.repaint(); 
                view_app.revalidate();
            }else {
                view_app.getParser_area().setText("Error");
                view_app.repaint();
                view_app.revalidate();
            } 
            return;
        }
        if(e.getSource() == view_app.getSemantic_btn()) {
            semantic = new SemanticAnalizer(); 
            if(semantic.check_program(parser.getAst_root())) {
                view_app.getSemantic_Area().setText("OK");
                view_app.repaint();
                view_app.revalidate();
            }else {
                view_app.getSemantic_Area().setText("Error Semantico");
                view_app.repaint();
                view_app.revalidate();
            }
            return; 
        }
        if(e.getSource() == view_app.getIntermediate_code_btn()) {
            ScopedSymbolTable table = semantic.getCurrent_symbol_table();

            semantic.getCurrent_symbol_table().fill_all_symbols_arr(table);
            semantic.getInt_code_generator().setSymbols(semantic.getCurrent_symbol_table().getAll_symbols());
            this.view_app.getIntermediate_code_area().setText(semantic.getInt_code_generator().getAllCode());
            return; 
        }
        if(e.getSource() == view_app.getObject_code_btn()) {
            String intermediate_code = view_app.getIntermediate_code_area().getText(); 
            ObjectCodeGenerator obj_gen = new ObjectCodeGenerator(intermediate_code); 
            String obj_str = obj_gen.generate(); 
            this.view_app.getObject_code_area().setText(obj_str);
            this.view_app.repaint();
            this.view_app.revalidate(); 
        }
    }
}
