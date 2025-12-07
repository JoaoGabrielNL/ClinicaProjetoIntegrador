package br.edu.imepac.clinica.daos;

import br.edu.imepac.clinica.entidades.Medico;
import br.edu.imepac.clinica.interfaces.Persistente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MedicoDao extends BaseDao implements Persistente<Medico> {

    @Override
    public boolean salvar(Medico medico) {
        String sql = "INSERT INTO medico (nome, crm, telefone, email, especialidade_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, medico.getNome());
            ps.setString(2, medico.getCrm());
            ps.setString(3, medico.getTelefone());
            ps.setString(4, medico.getEmail());
            ps.setLong(5, medico.getEspecialidadeId());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) medico.setId(rs.getLong(1));
            }
            return medico.getId() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao salvar médico: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean atualizar(Medico medico) {
        String sql = "UPDATE medico SET nome=?, crm=?, telefone=?, email=?, especialidade_id=? WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, medico.getNome());
            ps.setString(2, medico.getCrm());
            ps.setString(3, medico.getTelefone());
            ps.setString(4, medico.getEmail());
            ps.setLong(5, medico.getEspecialidadeId());
            ps.setLong(6, medico.getId());

            int linhas = ps.executeUpdate();
            return linhas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar médico: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean excluir(long id) {
        String sql = "DELETE FROM medico WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            int linhas = ps.executeUpdate();
            return linhas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao excluir médico: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Medico buscarPorId(long id) {
        String sql = "SELECT id, nome, crm, telefone, email, especialidade_id FROM medico WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMedico(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar médico por ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Medico> listarTodos() {
        List<Medico> lista = new ArrayList<>();
        String sql = "SELECT id, nome, crm, telefone, email, especialidade_id FROM medico ORDER BY nome";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapResultSetToMedico(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar médicos: " + e.getMessage());
        }
        return lista;
    }
    
    
    private Medico mapResultSetToMedico(ResultSet rs) throws SQLException {
        Medico m = new Medico();
        m.setId(rs.getLong("id"));
        m.setNome(rs.getString("nome"));
        m.setCrm(rs.getString("crm"));
        m.setTelefone(rs.getString("telefone"));
        m.setEmail(rs.getString("email"));
        m.setEspecialidadeId(rs.getLong("especialidade_id"));
        return m;
    }
}