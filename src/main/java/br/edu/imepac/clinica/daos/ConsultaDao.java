package br.edu.imepac.clinica.daos;

import br.edu.imepac.clinica.entidades.Consulta;
import br.edu.imepac.clinica.interfaces.Persistente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ConsultaDao extends BaseDao implements Persistente<Consulta> {

    @Override
    public boolean salvar(Consulta consulta) {
        String sql = "INSERT INTO consulta (paciente_id, medico_id, data_consulta, hora_consulta, observacoes, convenio_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, consulta.getPacienteId());
            ps.setLong(2, consulta.getMedicoId());
            ps.setString(3, consulta.getDataConsulta());
            ps.setString(4, consulta.getHoraConsulta());
            ps.setString(5, consulta.getObservacoes());
            
            if (consulta.getConvenioId() != null && consulta.getConvenioId() != 0) {
                 ps.setLong(6, consulta.getConvenioId());
            } else {
                 ps.setNull(6, java.sql.Types.BIGINT); 
            }
           
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) consulta.setId(rs.getLong(1));
            }
            return consulta.getId() != null && consulta.getId() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao salvar consulta: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean atualizar(Consulta consulta) {
        String sql = "UPDATE consulta SET paciente_id=?, medico_id=?, data_consulta=?, hora_consulta=?, observacoes=?, convenio_id=? WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, consulta.getPacienteId());
            ps.setLong(2, consulta.getMedicoId());
            ps.setString(3, consulta.getDataConsulta());
            ps.setString(4, consulta.getHoraConsulta());
            ps.setString(5, consulta.getObservacoes());
            
            if (consulta.getConvenioId() != null && consulta.getConvenioId() != 0) {
                 ps.setLong(6, consulta.getConvenioId());
            } else {
                 ps.setNull(6, java.sql.Types.BIGINT);
            }
            ps.setLong(7, consulta.getId());

            int linhas = ps.executeUpdate();
            return linhas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar consulta: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean excluir(long id) {
        String sql = "DELETE FROM consulta WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            int linhas = ps.executeUpdate();
            return linhas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao excluir consulta: " + e.getMessage());
            return false;
        }
    }

    private Consulta mapResultSetToConsulta(ResultSet rs) throws SQLException {
        Consulta c = new Consulta();
        c.setId(rs.getLong("id"));
        c.setPacienteId(rs.getLong("paciente_id"));
        c.setMedicoId(rs.getLong("medico_id"));
        c.setDataConsulta(rs.getString("data_consulta"));
        c.setHoraConsulta(rs.getString("hora_consulta"));
        c.setObservacoes(rs.getString("observacoes"));
        
        Long convenioId = rs.getLong("convenio_id");
        if (rs.wasNull()) {
            c.setConvenioId(null);
        } else {
            c.setConvenioId(convenioId);
        }
        return c;
    }

    @Override
    public Consulta buscarPorId(long id) {
        String sql = "SELECT id, paciente_id, medico_id, data_consulta, hora_consulta, observacoes, convenio_id FROM consulta WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapResultSetToConsulta(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar consulta por ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Consulta> listarTodos() {
        List<Consulta> lista = new ArrayList<>();
        String sql = "SELECT id, paciente_id, medico_id, data_consulta, hora_consulta, observacoes, convenio_id FROM consulta ORDER BY data_consulta, hora_consulta";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapResultSetToConsulta(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar consultas: " + e.getMessage());
        }
        return lista;
    }
}