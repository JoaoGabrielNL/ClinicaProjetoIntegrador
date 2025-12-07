package br.edu.imepac.clinica.screens.pacientes;

import br.edu.imepac.clinica.daos.PacienteDao;
import br.edu.imepac.clinica.daos.PortalPacienteDao;
import br.edu.imepac.clinica.entidades.Paciente;
import br.edu.imepac.clinica.entidades.PortalPaciente;
import br.edu.imepac.clinica.screens.portal.PacienteRelatorioForm;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class PacienteLoginForm extends JFrame {

    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private JButton btnLogin;

    public PacienteLoginForm() {
        setTitle("Portal do Paciente - Login");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel painelInputs = new JPanel(new GridLayout(2, 2, 10, 10));
        
        painelInputs.add(new JLabel("Login:"));
        txtLogin = new JTextField(15);
        painelInputs.add(txtLogin);
        
        painelInputs.add(new JLabel("Senha:"));
        txtSenha = new JPasswordField(15);
        painelInputs.add(txtSenha);

        btnLogin = new JButton("Entrar e Ver Relatórios");
        btnLogin.addActionListener(e -> realizarLogin());

        JPanel painelPrincipal = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelPrincipal.add(painelInputs);
        painelPrincipal.add(btnLogin);

        getContentPane().add(painelPrincipal);
        ((JPanel)getContentPane()).setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    private void realizarLogin() {
        String login = txtLogin.getText();
        String senha = new String(txtSenha.getPassword());

        if (login.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha o login e a senha.", "Erro de Acesso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            PortalPacienteDao ppDao = new PortalPacienteDao();
            PortalPaciente portal = ppDao.buscarPorLoginSenha(login, senha);

            if (portal != null) {
                
                PacienteDao pDao = new PacienteDao();
                Paciente paciente = pDao.buscarPorId(portal.getPacienteId());

                if (paciente != null && paciente.getAtivo()) {
                    JOptionPane.showMessageDialog(this, "Bem-vindo, " + paciente.getNome() + "!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    
                    new PacienteRelatorioForm(paciente.getId(), paciente.getNome()).setVisible(true);
                    this.dispose(); 
                } else {
                    JOptionPane.showMessageDialog(this, "Usuário inativo ou não encontrado.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
                }
                
            } else {
                 JOptionPane.showMessageDialog(this, "Login ou senha inválidos.", "Erro de Acesso", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao tentar login: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}