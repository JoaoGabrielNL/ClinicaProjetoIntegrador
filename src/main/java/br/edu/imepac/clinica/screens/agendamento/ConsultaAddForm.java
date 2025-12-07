package br.edu.imepac.clinica.screens.agendamento;

import br.edu.imepac.clinica.daos.ConsultaDao;
import br.edu.imepac.clinica.daos.ConvenioDao;
import br.edu.imepac.clinica.daos.MedicoDao;
import br.edu.imepac.clinica.daos.PacienteDao;
import br.edu.imepac.clinica.entidades.Consulta;
import br.edu.imepac.clinica.entidades.Convenio;
import br.edu.imepac.clinica.entidades.Medico;
import br.edu.imepac.clinica.entidades.Paciente;
import br.edu.imepac.clinica.exceptions.CampoObrigatorioException;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ConsultaAddForm extends JFrame {

    private JTextField txtPacienteId;
    private JTextField txtData;
    private JTextField txtHora;
    private JComboBox<MedicoWrapper> cbMedico;
    private JComboBox<ConvenioWrapper> cbConvenio;
    private JTextArea txtObservacoes;
    private JButton btnSalvar;
    private JButton btnBuscarPaciente;
    private JLabel lblNomePaciente;
    
    private Paciente pacienteSelecionado;

    public ConsultaAddForm() {
        setTitle("Agendar Nova Consulta");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents(); 
        
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                carregarCombos(); 
            }
        });
        
        carregarCombos(); 
    }

    private void initComponents() {
        JPanel painelInputs = new JPanel(new GridLayout(7, 2, 10, 10));

        painelInputs.add(new JLabel("ID Paciente:"));
        JPanel painelPaciente = new JPanel(new BorderLayout(5, 0));
        txtPacienteId = new JTextField(6);
        btnBuscarPaciente = new JButton("Buscar");
        btnBuscarPaciente.addActionListener(e -> buscarPaciente());
        painelPaciente.add(txtPacienteId, BorderLayout.CENTER);
        painelPaciente.add(btnBuscarPaciente, BorderLayout.EAST);
        painelInputs.add(painelPaciente);
        
        painelInputs.add(new JLabel("Nome:"));
        lblNomePaciente = new JLabel("Nenhum paciente selecionado");
        painelInputs.add(lblNomePaciente);

        painelInputs.add(new JLabel("Médico:"));
        cbMedico = new JComboBox<>();
        painelInputs.add(cbMedico);

        painelInputs.add(new JLabel("Data (yyyy-MM-dd):"));
        txtData = new JTextField();
        painelInputs.add(txtData);

        painelInputs.add(new JLabel("Hora (HH:mm):"));
        txtHora = new JTextField();
        painelInputs.add(txtHora);
        
        painelInputs.add(new JLabel("Convênio (Opcional):"));
        cbConvenio = new JComboBox<>();
        painelInputs.add(cbConvenio);

        painelInputs.add(new JLabel("Observações:"));
        txtObservacoes = new JTextArea(3, 20);
        JScrollPane scrollObs = new JScrollPane(txtObservacoes);
        painelInputs.add(scrollObs);

        btnSalvar = new JButton("Agendar Consulta");
        btnSalvar.addActionListener(e -> salvarConsulta());
        
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelBotoes.add(btnSalvar);

        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().add(painelInputs, BorderLayout.CENTER);
        getContentPane().add(painelBotoes, BorderLayout.SOUTH);
        
        ((JPanel)getContentPane()).setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    private void carregarCombos() {
        carregarMedicos();
        carregarConvenios();
    }
    
    private void carregarMedicos() {
        try {
            MedicoDao dao = new MedicoDao();
            List<Medico> lista = dao.listarTodos();
            cbMedico.removeAllItems();
            
            if (lista.isEmpty()) {
                 cbMedico.addItem(new MedicoWrapper(0, "Nenhum médico cadastrado"));
                 btnSalvar.setEnabled(false); 
            } else {
                 btnSalvar.setEnabled(true);
                 for (Medico m : lista) {
                    cbMedico.addItem(new MedicoWrapper(m.getId(), m.getNome() + " (" + m.getCrm() + ")"));
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar médicos: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void carregarConvenios() {
        try {
            ConvenioDao dao = new ConvenioDao();
            List<Convenio> lista = dao.listarTodos();
            cbConvenio.removeAllItems();
            
            cbConvenio.addItem(new ConvenioWrapper(0, "Sem Convênio")); 
            
            for (Convenio c : lista) {
                cbConvenio.addItem(new ConvenioWrapper(c.getId(), c.getNome()));
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar convênios: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void buscarPaciente() {
        try {
            Long pacienteId = Long.parseLong(txtPacienteId.getText());
            PacienteDao dao = new PacienteDao();
            Paciente p = dao.buscarPorId(pacienteId);
            
            if (p == null || !p.getAtivo()) {
                lblNomePaciente.setText("Paciente não encontrado ou inativo");
                pacienteSelecionado = null;
                throw new Exception("Paciente não encontrado ou inativo.");
            }
            
            lblNomePaciente.setText(p.getNome() + " (Ativo)");
            pacienteSelecionado = p;

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Informe um ID de paciente válido.", "Validação", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void salvarConsulta() {
        try {
            validarCamposObrigatorios();
            
            MedicoWrapper medicoSelecionado = (MedicoWrapper) cbMedico.getSelectedItem();
            ConvenioWrapper convenioSelecionado = (ConvenioWrapper) cbConvenio.getSelectedItem();
            
            Consulta consulta = new Consulta();
            consulta.setPacienteId(pacienteSelecionado.getId());
            consulta.setMedicoId(medicoSelecionado.getId());
            consulta.setDataConsulta(txtData.getText().trim());
            consulta.setHoraConsulta(txtHora.getText().trim());
            consulta.setObservacoes(txtObservacoes.getText());
            
            if (convenioSelecionado.getId() > 0) {
                 consulta.setConvenioId(convenioSelecionado.getId());
            }

            ConsultaDao dao = new ConsultaDao();
            boolean status = dao.salvar(consulta);

            if (status) {
                JOptionPane.showMessageDialog(this, "Consulta agendada com sucesso! ID: " + consulta.getId(), "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao agendar Consulta. Verifique o log.", "Erro", JOptionPane.ERROR_MESSAGE);
            }

        } catch (CampoObrigatorioException ex) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios (Paciente, Médico, Data, Hora).", "Validação", JOptionPane.ERROR_MESSAGE);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de data ou hora inválido. Data: yyyy-MM-dd, Hora: HH:mm.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void validarCamposObrigatorios() throws CampoObrigatorioException, ParseException {
        if (pacienteSelecionado == null) throw new CampoObrigatorioException(List.of("Paciente"));
        
        MedicoWrapper medicoSelecionado = (MedicoWrapper) cbMedico.getSelectedItem();
        if (medicoSelecionado == null || medicoSelecionado.getId() == 0) throw new CampoObrigatorioException(List.of("Médico"));
        
        String data = txtData.getText().trim();
        String hora = txtHora.getText().trim();
        
        if (data.isBlank()) throw new CampoObrigatorioException(List.of("Data"));
        if (hora.isBlank()) throw new CampoObrigatorioException(List.of("Hora"));
        
        new SimpleDateFormat("yyyy-MM-dd").parse(data);
        new SimpleDateFormat("HH:mm").parse(hora);
    }
    
    private void limparCampos() {
        txtPacienteId.setText("");
        lblNomePaciente.setText("Nenhum paciente selecionado");
        pacienteSelecionado = null;
        txtData.setText("");
        txtHora.setText("");
        txtObservacoes.setText("");
        cbMedico.setSelectedIndex(0);
        cbConvenio.setSelectedIndex(0);
    }

    private class MedicoWrapper {
        private final long id;
        private final String nome;

        public MedicoWrapper(long id, String nome) {
            this.id = id;
            this.nome = nome;
        }
        public long getId() { return id; }
        @Override
        public String toString() { return nome; }
    }
    
    private class ConvenioWrapper {
        private final long id;
        private final String nome;

        public ConvenioWrapper(long id, String nome) {
            this.id = id;
            this.nome = nome;
        }
        public long getId() { return id; }
        @Override
        public String toString() { return nome; }
    }
}