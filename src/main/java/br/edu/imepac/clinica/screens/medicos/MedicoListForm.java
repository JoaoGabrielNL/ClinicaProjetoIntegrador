package br.edu.imepac.clinica.screens.medicos;

import br.edu.imepac.clinica.daos.MedicoDao;
import br.edu.imepac.clinica.entidades.Medico;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class MedicoListForm extends JFrame {

    private JTable tableMedicos;
    private JTextField txtBusca;
    private JButton btnBuscar, btnNovo, btnEditar, btnExcluir;

    public MedicoListForm() {
        setTitle("Lista de Médicos");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        carregarTabela();
    }

    private void initComponents() {
        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtBusca = new JTextField(20);
        btnBuscar = new JButton("Buscar");
        
        painelBusca.add(new JLabel("Buscar (Nome/CRM):"));
        painelBusca.add(txtBusca);
        painelBusca.add(btnBuscar);
        
        btnBuscar.addActionListener(e -> buscarMedicos()); 

        tableMedicos = new JTable(new DefaultTableModel(
                new Object[]{"ID", "Nome", "CRM", "Telefone", "Especialidade ID"}, 0));
        JScrollPane scroll = new JScrollPane(tableMedicos);

        JPanel painelAcoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnNovo = new JButton("Novo Médico");
        btnEditar = new JButton("Editar Selecionado");
        btnExcluir = new JButton("Excluir Médico");
        
        btnNovo.addActionListener(e -> abrirFormNovo());
        btnEditar.addActionListener(e -> abrirFormEditar());
        btnExcluir.addActionListener(e -> excluirMedico());
        
        painelAcoes.add(btnNovo);
        painelAcoes.add(btnEditar);
        painelAcoes.add(btnExcluir); 

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(painelBusca, BorderLayout.NORTH);
        getContentPane().add(scroll, BorderLayout.CENTER);
        getContentPane().add(painelAcoes, BorderLayout.SOUTH);
    }

    private void carregarTabela() {
        try {
            MedicoDao dao = new MedicoDao();
            List<Medico> lista = dao.listarTodos();
            DefaultTableModel model = (DefaultTableModel) tableMedicos.getModel();
            model.setRowCount(0);

            for (Medico m : lista) {
                model.addRow(new Object[]{
                    m.getId(), 
                    m.getNome(), 
                    m.getCrm(), 
                    m.getTelefone(), 
                    m.getEspecialidadeId()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar lista de médicos: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void buscarMedicos() {
        carregarTabela();
    }

    private void abrirFormNovo() {
        new MedicoAddForm().setVisible(true);
    }
    
    private void abrirFormEditar() {
        int row = tableMedicos.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um médico na tabela.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Long medicoId = (Long) tableMedicos.getValueAt(row, 0);
      
        new MedicoUpdateForm(medicoId).setVisible(true);
    }
    
    private void excluirMedico() {
        int row = tableMedicos.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um médico na tabela para excluir.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Long medicoId = (Long) tableMedicos.getValueAt(row, 0);
        String nomeMedico = (String) tableMedicos.getValueAt(row, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
                "Deseja realmente excluir o médico " + nomeMedico + "?", 
                "Confirmar Exclusão", 
                JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                MedicoDao dao = new MedicoDao();
                if (dao.excluir(medicoId)) {
                    JOptionPane.showMessageDialog(this, "Médico excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    carregarTabela(); 
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao excluir médico. Verifique as dependências.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public static void main(String[] args) {
        new MedicoListForm().setVisible(true);
    }
}