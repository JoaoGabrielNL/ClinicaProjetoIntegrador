package br.edu.imepac.clinica.screens;

import br.edu.imepac.clinica.enums.PerfilUsuario;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginForm extends JFrame {

    private JTextField txtLogin;
    private JPasswordField txtSenha;

    public LoginForm() {
        setTitle("Login Administrativo");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel painelCentral = new JPanel(new GridLayout(2, 2, 10, 10));
        
        painelCentral.add(new JLabel("Login:"));
        txtLogin = new JTextField(15);
        painelCentral.add(txtLogin);

        painelCentral.add(new JLabel("Senha:"));
        txtSenha = new JPasswordField(15);
        painelCentral.add(txtSenha);

        JButton btnLogin = new JButton("Entrar");
        btnLogin.addActionListener(this::realizarLogin);
        
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelBotoes.add(btnLogin);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(painelCentral, BorderLayout.CENTER);
        getContentPane().add(painelBotoes, BorderLayout.SOUTH);
        
        ((JPanel)getContentPane()).setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 10, 20));
    }

    private void realizarLogin(ActionEvent evt) {
        String login = txtLogin.getText();
        String senha = new String(txtSenha.getPassword());
        
        // --- Lógica simples para demonstração ---
        if (login.equals("secretaria") && senha.equals("123")) { 
            new MainMenu(PerfilUsuario.SECRETARIA).setVisible(true);
            this.dispose();
        } else if (login.equals("medico") && senha.equals("123")) { 
            new MainMenu(PerfilUsuario.MEDICO).setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Credenciais inválidas.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}