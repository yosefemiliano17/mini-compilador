import java.awt.Font;
import java.awt.Color;

import javax.swing.*;
import java.awt.event.*;

import java.io.File; 

public class App extends JFrame{

    private JButton scanner_btn, file_btn, save_code_btn; 
    private JTextArea code_area, tokens_area;
    private JLabel program_label, tokens_label; 
    private JFileChooser file_chooser; 

    public App() {
        super("Compilador"); 

        this.scanner_btn = new JButton("Scanner");
        this.file_btn = new JButton("Archivo");
        this.code_area = new JTextArea(); 
        this.tokens_area = new JTextArea(); 
        this.program_label = new JLabel("Programa:");
        this.tokens_label = new JLabel("Tokens:"); 
        this.save_code_btn = new JButton("Guardar"); 
        this.file_chooser = new JFileChooser(); 

        File working_directory = new File(System.getProperty("user.dir"));
        file_chooser.setCurrentDirectory(working_directory); 

        set_frame(); 
    }

    public void set_frame() {
        setSize(1500, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);
        setResizable(false);

        set_program_label(); 
        set_tokens_label(); 
        set_scanner_btn(); 
        set_save_code_btn();
        set_file_btn(); 
        set_code_area();
        set_tokens_area();

        this.setVisible(true); 
    }

    public void set_save_code_btn() {
        save_code_btn.setFont(new Font("Arial", Font.PLAIN, 27));
        save_code_btn.setBounds(800, 200, 150, 70);
        add(save_code_btn); 
    }

    public void set_program_label() {
        program_label.setFont(new Font("Arial", Font.BOLD, 20));
        program_label.setBounds(330, 10, 140, 70);
        add(program_label); 
    }

    public void set_tokens_label() {
        tokens_label.setFont(new Font("Arial", Font.BOLD, 20));
        tokens_label.setBounds(1200,10,100,70);
        add(tokens_label); 
    }

    public void set_file_btn() {
        file_btn.setBounds(800, 300, 150, 70);
        file_btn.setFont(new Font("Arial", Font.PLAIN, 27)); 
        add(file_btn); 
    }

    public void set_scanner_btn() {
        scanner_btn.setBounds(800, 400, 150, 70);
        scanner_btn.setFont(new Font("Arial", Font.PLAIN, 27));
        add(scanner_btn);
    }

    public void set_code_area() {
        code_area.setBounds(40, 70, 700, 720);
        code_area.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        code_area.setFont(new Font("Arial", Font.PLAIN, 30));

        JScrollPane scroll = new JScrollPane(code_area); 
        scroll.setBounds(40, 70, 700, 720);
        add(scroll);
    }

    public void set_tokens_area() {
        tokens_area.setBounds(1020, 70, 440, 720);
        tokens_area.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        tokens_area.setFont(new Font("Arial", Font.PLAIN, 17));
        tokens_area.setEditable(false);

        JScrollPane scroll = new JScrollPane(tokens_area);
        scroll.setBounds(1020, 70, 440, 720); 
        add(scroll); 
    }

    public void add_scanner_listeners(ActionListener listener) {
        this.file_btn.addActionListener(listener);
        this.scanner_btn.addActionListener(listener);
        this.save_code_btn.addActionListener(listener); 
    }

    public JButton getScanner_btn() {
        return scanner_btn;
    }

    public JButton getFile_btn() {
        return file_btn;
    }

    public JTextArea getCode_area() {
        return code_area;
    }

    public JTextArea getTokens_area() {
        return tokens_area;
    }

    public JFileChooser getFile_chooser() {
        return file_chooser;
    }

    public JButton getSave_code_btn() {
        return save_code_btn;
    }

}
