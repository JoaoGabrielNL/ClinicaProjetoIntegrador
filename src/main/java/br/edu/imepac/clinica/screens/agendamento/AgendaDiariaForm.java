package br.edu.imepac.clinica.screens.agendamento;

import br.edu.imepac.clinica.daos.ConsultaDao;
import br.edu.imepac.clinica.daos.MedicoDao;
import br.edu.imepac.clinica.daos.PacienteDao;
import br.edu.imepac.clinica.entidades.Consulta;
import br.edu.imepac.clinica.entidades.Medico;
import br.edu.imepac.clinica.entidades.Paciente;
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
import javax.swing.table.DefaultTableModel;
import java.util.HashMap;
import java.util.Map;

public class AgendaDiariaForm extends JFrame {

    private JTable tableAgenda;
    private final Map<Long, String> medicosCache = new HashMap<>();
    private final Map<Long, String> pacientesCache = new HashMap<>();

    public AgendaDiariaForm() {
        setTitle("Agenda de Consultas Marcadas");
        setSize(850, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        carregarAgenda();
    }

    private void initComponents() {
       JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.CENTER));
    painelTopo.add(new JLabel("Consultas Agendadas"));

    tableAgenda = new JTable(new DefaultTableModel(
            new Object[]{"ID", "Data", "Hora", "Paciente", "Médico", "Convênio"}, 0)); 
    JScrollPane scroll = new JScrollPane(tableAgenda);

    JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
    
    JButton btnMarcarRealizada = new JButton("Marcar como Realizada");
    btnMarcarRealizada.addActionListener(e -> marcarConsultaComoRealizada());
    
    JButton btnAtualizar = new JButton("Atualizar Agenda");
    btnAtualizar.addActionListener(e -> carregarAgenda());
    
    painelBotoes.add(btnMarcarRealizada);
    painelBotoes.add(btnAtualizar);

    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(painelTopo, BorderLayout.NORTH);
    getContentPane().add(scroll, BorderLayout.CENTER);
    getContentPane().add(painelBotoes, BorderLayout.SOUTH);
}
    
    private String getNomeMedico(Long id) {
        if (id == null || id == 0) return "N/A";
        if (medicosCache.containsKey(id)) return medicosCache.get(id);
        
        try {
            MedicoDao dao = new MedicoDao();
            Medico m = dao.buscarPorId(id);
            String nome = (m != null) ? m.getNome() : "Médico ID " + id + " (Não encontrado)";
            medicosCache.put(id, nome);
            return nome;
        } catch (Exception e) {
            return "Erro ao buscar Médico";
        }
    }
    
    private String getNomePaciente(Long id) {
        if (id == null || id == 0) return "N/A";
        if (pacientesCache.containsKey(id)) return pacientesCache.get(id);
        
        try {
            PacienteDao dao = new PacienteDao();
            Paciente p = dao.buscarPorId(id);
            String nome = (p != null) ? p.getNome() : "Paciente ID " + id + " (Não encontrado)";
            pacientesCache.put(id, nome);
            return nome;
        } catch (Exception e) {
            return "Erro ao buscar Paciente";
        }
    }

    private void carregarAgenda() {
        try {
            ConsultaDao dao = new ConsultaDao();
            List<Consulta> lista = dao.listarTodos();
            DefaultTableModel model = (DefaultTableModel) tableAgenda.getModel();
            model.setRowCount(0);

            medicosCache.clear();
            pacientesCache.clear();
            
            for (Consulta c : lista) {
                String nomePaciente = getNomePaciente(c.getPacienteId());
                String nomeMedico = getNomeMedico(c.getMedicoId());
                
                model.addRow(new Object[]{
                    c.getId(),
                    c.getDataConsulta(),
                    c.getHoraConsulta(),
                    nomePaciente,
                    nomeMedico,
                    c.getConvenioId() != null && c.getConvenioId() > 0 ? "Convênio ID " + c.getConvenioId() : "Particular"
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar a agenda: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void marcarConsultaComoRealizada() {
    int linhaSelecionada = tableAgenda.getSelectedRow();
    
    if (linhaSelecionada == -1) {
        JOptionPane.showMessageDialog(this, "Selecione uma consulta na tabela.", "Atenção", JOptionPane.WARNING_MESSAGE);
        return;
    }

    try {
     
        long consultaId = (long) tableAgenda.getModel().getValueAt(linhaSelecionada, 0);

        int confirmacao = JOptionPane.showConfirmDialog(this, 
                "Tem certeza que deseja marcar a consulta ID " + consultaId + " como realizada?", 
                "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirmacao == JOptionPane.YES_OPTION) {
            ConsultaDao dao = new ConsultaDao();
            boolean sucesso = dao.marcarComoRealizada(consultaId);

            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Consulta marcada como realizada e movida para o histórico.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarAgenda(); 
            } else {
                JOptionPane.showMessageDialog(this, "Falha ao atualizar o status da consulta.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Erro ao marcar consulta: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
}
}