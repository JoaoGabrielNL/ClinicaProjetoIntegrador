package br.edu.imepac.clinica.screens.convenios;

import br.edu.imepac.clinica.daos.ConvenioDao;
import br.edu.imepac.clinica.entidades.Convenio;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ConvenioAddForm extends JFrame {

    private JTextField txtNome;
    private JTextArea txtDescricao;
    private JButton btnSalvar;

    public ConvenioAddForm() {
        setTitle("Cadastrar Novo Convênio");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel painelInputs = new JPanel(new GridLayout(3, 2, 10, 10));

        painelInputs.add(new JLabel("Nome:"));
        txtNome = new JTextField();
        painelInputs.add(txtNome);

        painelInputs.add(new JLabel("Descrição:"));
        txtDescricao = new JTextArea(5, 20);
        JScrollPane scrollDescricao = new JScrollPane(txtDescricao);
        painelInputs.add(scrollDescricao);

        btnSalvar = new JButton("Salvar Convênio");
        btnSalvar.addActionListener(e -> salvarConvenio());
        
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelBotoes.add(btnSalvar);

        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().add(painelInputs, BorderLayout.CENTER);
        getContentPane().add(painelBotoes, BorderLayout.SOUTH);
        
        ((JPanel)getContentPane()).setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
    
    private void salvarConvenio() {
        String nome = txtNome.getText().trim();
        String descricao = txtDescricao.getText().trim();
        
        if (nome.isEmpty() || descricao.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Validação", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Convenio convenio = new Convenio();
            convenio.setNome(nome);
            convenio.setDescricao(descricao);

            ConvenioDao dao = new ConvenioDao();
            boolean salvo = dao.salvar(convenio);

            if (salvo) {
                JOptionPane.showMessageDialog(this, "Convênio cadastrado com sucesso! ID: " + convenio.getId(), "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                txtNome.setText("");
                txtDescricao.setText("");
                this.dispose(); 
            } else {
                JOptionPane.showMessageDialog(this, "Falha ao salvar. Nenhuma linha afetada.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (RuntimeException ex) {
            // Captura erros de Banco de Dados forçados pelo DAO (SQL error)
            JOptionPane.showMessageDialog(this, "Erro de Banco de Dados: " + ex.getMessage(), "Erro Fatal", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro inesperado: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}