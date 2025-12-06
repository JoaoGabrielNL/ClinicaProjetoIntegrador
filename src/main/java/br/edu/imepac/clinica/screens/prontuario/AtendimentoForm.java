package br.edu.imepac.clinica.screens.prontuario;

import br.edu.imepac.clinica.daos.AtendimentoDao;
import br.edu.imepac.clinica.daos.PacienteDao;
import br.edu.imepac.clinica.entidades.Atendimento;
import br.edu.imepac.clinica.entidades.Paciente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AtendimentoForm extends JFrame {

    private JTextField txtPacienteId;
    private JTextField txtPacienteNome;
    private JTextArea txtAnamnese;
    private JTextArea txtDiagnostico;
    private JTextArea txtConduta;
    private JTextField txtMedico;
    private JTable tableHistorico;
    private JButton btnSalvarAtendimento, btnBuscarPaciente;

    public AtendimentoForm() {
        setTitle("Atendimento / Prontuário");
        setSize(900,600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topo.add(new JLabel("Paciente ID:"));
        txtPacienteId = new JTextField(6);
        topo.add(txtPacienteId);
        btnBuscarPaciente = new JButton("Carregar Paciente");
        topo.add(btnBuscarPaciente);
        txtPacienteNome = new JTextField(30);
        txtPacienteNome.setEditable(false);
        topo.add(new JLabel("Nome:"));
        topo.add(txtPacienteNome);

        btnBuscarPaciente.addActionListener(e -> carregarPaciente());

        JPanel centro = new JPanel(new GridLayout(3,1));
        txtAnamnese = new JTextArea(5, 50);
        txtDiagnostico = new JTextArea(5,50);
        txtConduta = new JTextArea(4,50);
        centro.add(labeledPanel("Anamnese", new JScrollPane(txtAnamnese)));
        centro.add(labeledPanel("Diagnóstico", new JScrollPane(txtDiagnostico)));
        centro.add(labeledPanel("Conduta", new JScrollPane(txtConduta)));

        JPanel sul = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sul.add(new JLabel("Médico:"));
        txtMedico = new JTextField(20);
        sul.add(txtMedico);
        btnSalvarAtendimento = new JButton("Salvar Atendimento");
        sul.add(btnSalvarAtendimento);

        btnSalvarAtendimento.addActionListener(e -> salvarAtendimento());

        tableHistorico = new JTable(new DefaultTableModel(new Object[]{"ID","Data","Anamnese resumida","Diagnóstico"},0));
        JScrollPane scroll = new JScrollPane(tableHistorico);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(topo, BorderLayout.NORTH);
        getContentPane().add(centro, BorderLayout.CENTER);
        getContentPane().add(sul, BorderLayout.PAGE_END);
        getContentPane().add(scroll, BorderLayout.EAST);
    }

    private JPanel labeledPanel(String title, Component comp) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createTitledBorder(title));
        p.add(comp, BorderLayout.CENTER);
        return p;
    }

    private void carregarPaciente() {
        try {
            Long id = Long.parseLong(txtPacienteId.getText());
            PacienteDao pdao = new PacienteDao();
            Paciente p = pdao.buscarPorId(id);
            if (p == null) { JOptionPane.showMessageDialog(this, "Paciente não encontrado"); return; }
            txtPacienteNome.setText(p.getNome());
            carregarHistorico(id);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Informe um ID válido");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }

    private void carregarHistorico(Long pacienteId) {
        try {
            AtendimentoDao dao = new AtendimentoDao();
            List<Atendimento> lista = dao.listarPorPaciente(pacienteId);
            DefaultTableModel model = (DefaultTableModel) tableHistorico.getModel();
            model.setRowCount(0);
            for (Atendimento a : lista) {
                String resumo = a.getAnamnese();
                if (resumo != null && resumo.length() > 40) resumo = resumo.substring(0,40) + "...";
                model.addRow(new Object[]{a.getId(), a.getDataRegistro(), resumo, a.getDiagnostico()});
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void salvarAtendimento() {
        try {
            Long pacienteId = Long.parseLong(txtPacienteId.getText());
            Atendimento a = new Atendimento();
            a.setPacienteId(pacienteId);
            a.setDataRegistro(java.time.LocalDate.now().toString());
            a.setAnamnese(txtAnamnese.getText());
            a.setDiagnostico(txtDiagnostico.getText());
            a.setConduta(txtConduta.getText());
            a.setMedicoResponsavel(txtMedico.getText());

            AtendimentoDao dao = new AtendimentoDao();
            dao.inserir(a);

            JOptionPane.showMessageDialog(this, "Atendimento salvo.");
            carregarHistorico(pacienteId);
            limparCampos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID do paciente inválido");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }

    private void limparCampos() {
        txtAnamnese.setText("");
        txtDiagnostico.setText("");
        txtConduta.setText("");
        txtMedico.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AtendimentoForm f = new AtendimentoForm();
            f.setVisible(true);
        });
    }
}