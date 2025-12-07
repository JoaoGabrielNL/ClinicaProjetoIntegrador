package br.edu.imepac.clinica.screens.portal;

import br.edu.imepac.clinica.daos.AtendimentoDao;
import br.edu.imepac.clinica.entidades.Atendimento;
import br.edu.imepac.clinica.interfaces.PesquisadeSatidfacaoAddForm;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

public class PacienteRelatorioForm extends JFrame {

    private final Long pacienteId;
    private final String pacienteNome;
    private JTable tableHistorico;
    private JTextArea txtDetalhes;
    private JLabel lblTitulo;

    public PacienteRelatorioForm(Long pacienteId, String pacienteNome) {
        this.pacienteId = pacienteId;
        this.pacienteNome = pacienteNome;
        setTitle("Histórico de Atendimentos - " + pacienteNome);
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setLocationRelativeTo(null);
        
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent windowEvent) {
                PesquisadeSatidfacaoAddForm form = new PesquisadeSatidfacaoAddForm();
                form.setVisible(true);
            }
        });
        
        initComponents();
        carregarHistorico();
    }

    private void initComponents() {
        lblTitulo = new JLabel("Relatórios de Consultas para: " + pacienteNome);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelTopo.add(lblTitulo);

        tableHistorico = new JTable(new DefaultTableModel(
                new Object[]{"ID", "Data", "Médico", "Diagnóstico"}, 0));
        JScrollPane scrollTabela = new JScrollPane(tableHistorico); 
        
        tableHistorico.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                visualizarDetalhes();
            }
        });

        txtDetalhes = new JTextArea();
        txtDetalhes.setEditable(false);
        txtDetalhes.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollDetalhes = new JScrollPane(txtDetalhes);
        
        JButton btnFechar = new JButton("Fechar Portal");
        btnFechar.addActionListener(e -> this.dispose());
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelBotoes.add(btnFechar);

        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().add(painelTopo, BorderLayout.NORTH);
        
        JPanel centroDividido = new JPanel(new GridLayout(2, 1, 10, 10));
        centroDividido.add(scrollTabela);
        centroDividido.add(scrollDetalhes);
        
        getContentPane().add(centroDividido, BorderLayout.CENTER);
        getContentPane().add(painelBotoes, BorderLayout.SOUTH);
        
        ((JPanel)getContentPane()).setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    private void carregarHistorico() {
        try {
            AtendimentoDao dao = new AtendimentoDao();
            List<Atendimento> lista = dao.listarPorPaciente(pacienteId);
            DefaultTableModel model = (DefaultTableModel) tableHistorico.getModel();
            model.setRowCount(0);

            if (lista.isEmpty()) {
                model.addRow(new Object[]{"-", "Nenhum Atendimento Encontrado", "-", "-"});
                txtDetalhes.setText("Nenhum relatório de consulta disponível para este paciente.");
                return;
            }

            for (Atendimento a : lista) {
                model.addRow(new Object[]{
                    a.getId(), 
                    a.getDataRegistro(), 
                    a.getMedicoResponsavel(),
                    a.getDiagnostico()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar histórico: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void visualizarDetalhes() {
        int row = tableHistorico.getSelectedRow();
        if (row < 0) return;
        
        try {
            Object idObject = tableHistorico.getValueAt(row, 0);
            
            if (idObject == null || idObject.toString().equals("-")) {
                txtDetalhes.setText("Selecione um atendimento válido (a linha deve ter um ID numérico).");
                return;
            }

            Long atendimentoId = Long.parseLong(idObject.toString());
            
            AtendimentoDao dao = new AtendimentoDao();
            Atendimento a = dao.buscarPorId(atendimentoId);
            
            if (a != null) {
                String detalhes = String.format(
                    "--- Detalhes do Atendimento ID %d ---\n" +
                    "Data: %s\n" +
                    "Médico Responsável: %s\n\n" +
                    "ANAMNESE:\n%s\n\n" +
                    "DIAGNÓSTICO:\n%s\n\n" +
                    "CONDUTA (Tratamento):\n%s",
                    a.getId(),
                    a.getDataRegistro(),
                    a.getMedicoResponsavel(),
                    a.getAnamnese(),
                    a.getDiagnostico(),
                    a.getConduta()
                );
                txtDetalhes.setText(detalhes);
            }
            
        } catch (NumberFormatException ex) {
            txtDetalhes.setText("Erro ao ler ID. Certifique-se de que a linha selecionada tem um ID numérico.");
        } catch (Exception ex) {
            txtDetalhes.setText("Erro ao carregar detalhes: " + ex.getMessage());
        }
    }
}