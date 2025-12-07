package br.edu.imepac.clinica.screens.pacientes;

import br.edu.imepac.clinica.daos.PacienteDao;
import br.edu.imepac.clinica.daos.PortalPacienteDao; 
import br.edu.imepac.clinica.entidades.Paciente;
import br.edu.imepac.clinica.entidades.PortalPaciente; 

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat; 

public class PacienteAddForm extends JFrame {

    private JTextField txtNome;
    private JTextField txtCpf;
    private JTextField txtDataNascimento; 
    private JTextField txtTelefone;
    private JTextField txtEmail;
    private JTextField txtEndereco;
    
    private JTextField txtLogin; 
    private JPasswordField txtSenha; 
    
    private JTable tablePacientes;
    private JButton btnSalvar, btnEditar, btnExcluir, btnNovo, btnBuscar;
    private JTextField txtBusca;

   public PacienteAddForm() {
        setTitle("Cadastro de Pacientes");
        setSize(800, 700); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
        carregarTabela();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4,4,4,4);
        c.fill = GridBagConstraints.HORIZONTAL; 

        c.gridx = 0; c.gridy = 0;
        form.add(new JLabel("Nome:"), c);
        c.gridx = 1; c.gridy = 0; c.gridwidth = 3;
        txtNome = new JTextField(30);
        form.add(txtNome, c);

        c.gridx = 0; c.gridy = 1; c.gridwidth = 1;
        form.add(new JLabel("CPF:"), c);
        c.gridx = 1; c.gridy = 1;
        txtCpf = new JTextField(15);
        form.add(txtCpf, c);

        c.gridx = 2; c.gridy = 1;
        form.add(new JLabel("Data Nasc (yyyy-MM-dd):"), c); 
        c.gridx = 3; c.gridy = 1;
        txtDataNascimento = new JTextField(10);
        form.add(txtDataNascimento, c);

        c.gridx = 0; c.gridy = 2;
        form.add(new JLabel("Telefone:"), c);
        c.gridx = 1; c.gridy = 2;
        txtTelefone = new JTextField(12);
        form.add(txtTelefone, c);

        c.gridx = 2; c.gridy = 2;
        form.add(new JLabel("E-mail:"), c);
        c.gridx = 3; c.gridy = 2;
        txtEmail = new JTextField(20);
        form.add(txtEmail, c);

        c.gridx = 0; c.gridy = 3;
        form.add(new JLabel("Endereço:"), c);
        c.gridx = 1; c.gridy = 3; c.gridwidth = 3;
        txtEndereco = new JTextField(40);
        form.add(txtEndereco, c);
        
        c.gridx = 0; c.gridy = 4; c.gridwidth = 1;
        form.add(new JLabel("Login:"), c);
        c.gridx = 1; c.gridy = 4;
        txtLogin = new JTextField(15);
        form.add(txtLogin, c);

        c.gridx = 2; c.gridy = 4;
        form.add(new JLabel("Senha:"), c);
        c.gridx = 3; c.gridy = 4;
        txtSenha = new JPasswordField(10);
        form.add(txtSenha, c);


        JPanel botoes = new JPanel();
        btnSalvar = new JButton("Salvar e Criar Acesso"); 
        btnEditar = new JButton("Editar");
        btnExcluir = new JButton("Inativar");
        btnNovo = new JButton("Novo");
        botoes.add(btnSalvar);
        botoes.add(btnEditar);
        botoes.add(btnExcluir);
        botoes.add(btnNovo);

        btnSalvar.addActionListener(e -> salvarPaciente());
        btnEditar.addActionListener(e -> editarPaciente());
        btnExcluir.addActionListener(e -> inativarPaciente());
        btnNovo.addActionListener(e -> limparCampos());


        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtBusca = new JTextField(20);
        btnBuscar = new JButton("Buscar");
        painelBusca.add(new JLabel("Buscar (nome/CPF):"));
        painelBusca.add(txtBusca);
        painelBusca.add(btnBuscar);
        btnBuscar.addActionListener(e -> buscar());


        tablePacientes = new JTable(new DefaultTableModel(new Object[]{"ID","Nome","CPF","Telefone"}, 0));
        tablePacientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(tablePacientes);


        tablePacientes.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    carregarDadosSelecionado();
                }
            }
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(form, BorderLayout.NORTH);
        topPanel.add(painelBusca, BorderLayout.CENTER);
        
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(scroll, BorderLayout.CENTER); 
        getContentPane().add(botoes, BorderLayout.SOUTH);
    }
    
    private String processarDataUI(String dataInput) throws ParseException {
        if (dataInput == null || dataInput.trim().isEmpty()) {
            return null; 
        }
        
        SimpleDateFormat formatoDB = new SimpleDateFormat("yyyy-MM-dd");
        formatoDB.setLenient(false); 

        try {
            java.util.Date data = formatoDB.parse(dataInput);
            return formatoDB.format(data); 
        } catch (ParseException e) {
            throw new ParseException("Formato de data inválido. Use yyyy-MM-dd.", 0);
        }
    }


    private void salvarPaciente() {
        try {
            String nome = txtNome.getText();
            String login = txtLogin.getText();
            String senha = new String(txtSenha.getPassword());
            String dataNascUI = txtDataNascimento.getText(); 

            if (nome.isBlank()) {
                JOptionPane.showMessageDialog(this, "O campo Nome é obrigatório.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (login.isBlank() || senha.isBlank()) {
                JOptionPane.showMessageDialog(this, "Login e Senha são obrigatórios para criar o acesso.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String dataNascDB = processarDataUI(dataNascUI);
            
            Paciente p = new Paciente();
            p.setNome(nome);
            p.setCpf(txtCpf.getText());
            p.setDataNascimento(dataNascDB); 
            p.setTelefone(txtTelefone.getText());
            p.setEmail(txtEmail.getText());
            p.setEndereco(txtEndereco.getText());

            PacienteDao dao = new PacienteDao();
            dao.inserir(p);

            PortalPaciente pp = new PortalPaciente();
            pp.setPacienteId(p.getId());
            pp.setLogin(login);
            pp.setSenha(senha); 

            PortalPacienteDao ppDao = new PortalPacienteDao();
            ppDao.criarAcesso(pp);


            JOptionPane.showMessageDialog(this, 
                "Paciente salvo. ID: " + p.getId() + "\n" + 
                "Acesso criado para o login: " + pp.getLogin(), 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            carregarTabela();
            limparCampos();
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Data", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro de banco de dados ao salvar: " + ex.getMessage(), "Erro de Persistência", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage(), "Erro Inesperado", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarPaciente() {
        int row = tablePacientes.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Selecione um paciente na tabela para editar."); return; }
        
        try {
            String nome = txtNome.getText();
            if (nome.isBlank()) {
                JOptionPane.showMessageDialog(this, "O campo Nome é obrigatório.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Long id = Long.parseLong(tablePacientes.getValueAt(row, 0).toString());
            PacienteDao dao = new PacienteDao();
            Paciente p = dao.buscarPorId(id);
            if (p == null) { JOptionPane.showMessageDialog(this, "Paciente não encontrado"); return; }
            
            String dataNascDB = processarDataUI(txtDataNascimento.getText()); 

            p.setNome(nome);
            p.setCpf(txtCpf.getText());
            p.setDataNascimento(dataNascDB); 
            p.setTelefone(txtTelefone.getText());
            p.setEmail(txtEmail.getText());
            p.setEndereco(txtEndereco.getText());

            dao.atualizar(p);
            JOptionPane.showMessageDialog(this, "Paciente atualizado");
            carregarTabela();
            limparCampos();
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Data", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao editar: " + ex.getMessage());
        }
    }
    
    private void carregarDadosSelecionado() {
        int row = tablePacientes.getSelectedRow();
        if (row < 0) return;
        Long id = Long.parseLong(tablePacientes.getValueAt(row, 0).toString());
        try {
            PacienteDao dao = new PacienteDao();
            Paciente p = dao.buscarPorId(id);
            if (p != null) {
                txtNome.setText(p.getNome());
                txtCpf.setText(p.getCpf());
                
                txtDataNascimento.setText(p.getDataNascimento() != null ? p.getDataNascimento() : "");
                
                txtTelefone.setText(p.getTelefone());
                txtEmail.setText(p.getEmail());
                txtEndereco.setText(p.getEndereco());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void inativarPaciente() {
        int row = tablePacientes.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Selecione um paciente na tabela"); return; }
        Long id = Long.parseLong(tablePacientes.getValueAt(row, 0).toString());
        int confirm = JOptionPane.showConfirmDialog(this, "Confirma inativar o paciente ID " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        try {
            PacienteDao dao = new PacienteDao();
            dao.inativar(id);
            carregarTabela();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao inativar: " + ex.getMessage());
        }
    }

    private void buscar() {
        String termo = txtBusca.getText();
        if (termo == null || termo.trim().isEmpty()) { carregarTabela(); return; }
        try {
            PacienteDao dao = new PacienteDao();
            List<Paciente> lista = dao.buscarPorNomeOuCpf(termo.trim());
            DefaultTableModel model = (DefaultTableModel) tablePacientes.getModel();
            model.setRowCount(0);
            for (Paciente p : lista) {
                model.addRow(new Object[]{p.getId(), p.getNome(), p.getCpf(), p.getTelefone()});
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }

    private void carregarTabela() {
        try {
            PacienteDao dao = new PacienteDao();
            List<Paciente> lista = dao.listarTodos();
            DefaultTableModel model = (DefaultTableModel) tablePacientes.getModel();
            model.setRowCount(0);
            for (Paciente p : lista) {
                model.addRow(new Object[]{p.getId(), p.getNome(), p.getCpf(), p.getTelefone()});
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void limparCampos() {
        txtNome.setText("");
        txtCpf.setText("");
        txtDataNascimento.setText("");
        txtTelefone.setText("");
        txtEmail.setText("");
        txtEndereco.setText("");
        txtLogin.setText(""); 
        txtSenha.setText(""); 
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PacienteAddForm f = new PacienteAddForm();
            f.setVisible(true);
        });
    }
}