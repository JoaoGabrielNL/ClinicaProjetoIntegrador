package br.edu.imepac.clinica.daos;

import br.edu.imepac.clinica.entidades.Atendimento;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AtendimentoDao extends BaseDao {

    public void inserir(Atendimento a) throws SQLException {
        String sql = "INSERT INTO atendimento (paciente_id, data_registro, anamnese, diagnostico, conduta, medico_responsavel) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, a.getPacienteId());
            ps.setString(2, a.getDataRegistro());
            ps.setString(3, a.getAnamnese());
            ps.setString(4, a.getDiagnostico());
            ps.setString(5, a.getConduta());
            ps.setString(6, a.getMedicoResponsavel());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) a.setId(rs.getLong(1));
            }
        }
    }

    public List<Atendimento> listarPorPaciente(Long pacienteId) throws SQLException {
        List<Atendimento> lista = new ArrayList<>();
        String sql = "SELECT id, paciente_id, data_registro, anamnese, diagnostico, conduta, medico_responsavel FROM atendimento WHERE paciente_id = ? ORDER BY data_registro DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, pacienteId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSetToAtendimento(rs));
                }
            }
        }
        return lista;
    }
    
    // MÉTODO ESSENCIAL PARA A TELA DE RELATÓRIOS
    public Atendimento buscarPorId(long id) {
        String sql = "SELECT id, paciente_id, data_registro, anamnese, diagnostico, conduta, medico_responsavel FROM atendimento WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAtendimento(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar atendimento por ID: " + e.getMessage());
        }
        return null;
    }
    
    // MÉTODO AUXILIAR (MAPPER)
    private Atendimento mapResultSetToAtendimento(ResultSet rs) throws SQLException {
        Atendimento a = new Atendimento();
        a.setId(rs.getLong("id"));
        a.setPacienteId(rs.getLong("paciente_id"));
        a.setDataRegistro(rs.getString("data_registro"));
        a.setAnamnese(rs.getString("anamnese"));
        a.setDiagnostico(rs.getString("diagnostico"));
        a.setConduta(rs.getString("conduta"));
        a.setMedicoResponsavel(rs.getString("medico_responsavel"));
        return a;
    }
}