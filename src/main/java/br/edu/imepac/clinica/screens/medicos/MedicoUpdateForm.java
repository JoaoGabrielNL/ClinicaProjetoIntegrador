package br.edu.imepac.clinica.screens.medicos;

import br.edu.imepac.clinica.daos.EspecialidadeDao;
import br.edu.imepac.clinica.daos.MedicoDao;
import br.edu.imepac.clinica.entidades.Especialidade;
import br.edu.imepac.clinica.entidades.Medico;
import br.edu.imepac.clinica.exceptions.CampoObrigatorioException;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MedicoUpdateForm extends JFrame {

    private Medico medicoOriginal; 
    private JTextField txtNome;
    private JTextField txtCrm;
    private JTextField txtTelefone;
    private JTextField txtEmail;
    private JComboBox<EspecialidadeWrapper> cbEspecialidade;
    private JButton btnSalvar;

    public MedicoUpdateForm(Long medicoId) {
        setTitle("Atualizar Médico ID: " + medicoId);
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        loadData(medicoId); 
        if (medicoOriginal == null) {
            JOptionPane.showMessageDialog(this, "Médico não encontrado para o ID: " + medicoId, "Erro", JOptionPane.ERROR_MESSAGE);
            this.dispose();
            return;
        }
        
        initComponents();
        carregarEspecialidades();
        setDataForm(); 
    }

    private void initComponents() {
        JPanel painel = new JPanel(new GridLayout(6, 2, 10, 10));

        painel.add(new JLabel("Nome:"));
        txtNome = new JTextField();
        painel.add(txtNome);

        painel.add(new JLabel("CRM:"));
        txtCrm = new JTextField();
        painel.add(txtCrm);

        painel.add(new JLabel("Telefone:"));
        txtTelefone = new JTextField();
        painel.add(txtTelefone);

        painel.add(new JLabel("E-mail:"));
        txtEmail = new JTextField();
        painel.add(txtEmail);

        painel.add(new JLabel("Especialidade:"));
        cbEspecialidade = new JComboBox<>();
        painel.add(cbEspecialidade);

        btnSalvar = new JButton("Atualizar Médico");
        btnSalvar.addActionListener(e -> atualizarMedico());
        
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelBotoes.add(btnSalvar);

        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().add(painel, BorderLayout.CENTER);
        getContentPane().add(painelBotoes, BorderLayout.SOUTH);
        
        ((JPanel)getContentPane()).setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
    
    private void loadData(Long medicoId) {
        MedicoDao dao = new MedicoDao();
        this.medicoOriginal = dao.buscarPorId(medicoId);
    }
    
    private void setDataForm() {
        txtNome.setText(medicoOriginal.getNome());
        txtCrm.setText(medicoOriginal.getCrm());
        txtTelefone.setText(medicoOriginal.getTelefone());
        txtEmail.setText(medicoOriginal.getEmail());
    }

    private void carregarEspecialidades() {
        try {
            EspecialidadeDao dao = new EspecialidadeDao();
            List<Especialidade> lista = dao.listarTodos();
            
            cbEspecialidade.removeAllItems();
            int indexSelecionar = 0;
            
            if (lista.isEmpty()) {
                 cbEspecialidade.addItem(new EspecialidadeWrapper(0, "Nenhuma especialidade cadastrada"));
                 btnSalvar.setEnabled(false);
            } else {
                 btnSalvar.setEnabled(true);
                 int i = 0;
                 for (Especialidade e : lista) {
                    EspecialidadeWrapper wrapper = new EspecialidadeWrapper(e.getId(), e.getNome());
                    cbEspecialidade.addItem(wrapper);
                    if (e.getId() == medicoOriginal.getEspecialidadeId()) {
                        indexSelecionar = i;
                    }
                    i++;
                }
                cbEspecialidade.setSelectedIndex(indexSelecionar);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar especialidades: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarMedico() {
        try {
            validarCamposObrigatorios();
            
            EspecialidadeWrapper especialidadeSelecionada = (EspecialidadeWrapper) cbEspecialidade.getSelectedItem();
            
            // Usamos o objeto original para garantir que o ID está correto
            medicoOriginal.setNome(txtNome.getText());
            medicoOriginal.setCrm(txtCrm.getText());
            medicoOriginal.setTelefone(txtTelefone.getText());
            medicoOriginal.setEmail(txtEmail.getText());
            medicoOriginal.setEspecialidadeId(especialidadeSelecionada.getId());

            MedicoDao dao = new MedicoDao();
            boolean status = dao.atualizar(medicoOriginal);

            if (status) {
                JOptionPane.showMessageDialog(this, "Médico atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                this.dispose(); 
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar Médico. Verifique o log.", "Erro", JOptionPane.ERROR_MESSAGE);
            }

        } catch (CampoObrigatorioException ex) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios (Nome, CRM e Especialidade).", "Validação", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void validarCamposObrigatorios() throws CampoObrigatorioException {
        if (txtNome.getText().isBlank() || txtCrm.getText().isBlank() || cbEspecialidade.getSelectedItem() == null || ((EspecialidadeWrapper)cbEspecialidade.getSelectedItem()).getId() == 0) {
            throw new CampoObrigatorioException(List.of("Nome", "CRM", "Especialidade"));
        }
    }
    
    private class EspecialidadeWrapper {
        private final long id;
        private final String nome;

        public EspecialidadeWrapper(long id, String nome) {
            this.id = id;
            this.nome = nome;
        }

        public long getId() { return id; }

        @Override
        public String toString() { return nome; }
    }
}