package br.edu.imepac.clinica.daos;

import br.edu.imepac.clinica.entidades.Consulta;
import br.edu.imepac.clinica.interfaces.Persistente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class ConsultaDao extends BaseDao implements Persistente<Consulta> {

    @Override
    public boolean salvar(Consulta c) {
        String sql = "INSERT INTO consulta (paciente_id, medico_id, data_consulta, hora_consulta, convenio_id, observacoes, realizada) VALUES (?, ?, ?, ?, ?, ?, 0)";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            ps.setLong(1, c.getPacienteId());
            ps.setLong(2, c.getMedicoId());
            ps.setString(3, c.getDataConsulta());
            ps.setString(4, c.getHoraConsulta());
       
            if (c.getConvenioId() != null && c.getConvenioId() > 0) {
                 ps.setLong(5, c.getConvenioId());
            } else {
                 ps.setNull(5, Types.BIGINT);
            }
            
            ps.setString(6, c.getObservacoes());
            
            int linhasAfetadas = ps.executeUpdate();
            
            if (linhasAfetadas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        c.setId(rs.getLong(1));
                        return true;
                    }
                }
            }
            return false;

        } catch (SQLException e) {
            System.err.println("Erro ao salvar consulta: " + e.getMessage());
            throw new RuntimeException("Erro de Banco de Dados ao salvar consulta: " + e.getMessage(), e);
        } finally {
            fecharRecursos(conn, ps);
        }
    }

  
    public boolean marcarComoRealizada(long id) {
        String sql = "UPDATE consulta SET realizada = 1 WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setLong(1, id);
            
            int linhas = ps.executeUpdate();
            return linhas > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao marcar consulta como realizada: " + e.getMessage());
            throw new RuntimeException("Erro ao marcar consulta como realizada: " + e.getMessage(), e);
        } finally {
            fecharRecursos(conn, ps);
        }
    }
    
   
    private Consulta mapResultSetToConsulta(ResultSet rs) throws SQLException {
        Consulta c = new Consulta();
        c.setId(rs.getLong("id"));
        c.setPacienteId(rs.getLong("paciente_id"));
        c.setMedicoId(rs.getLong("medico_id"));
        c.setDataConsulta(rs.getString("data_consulta"));
        c.setHoraConsulta(rs.getString("hora_consulta"));
        c.setConvenioId(rs.getLong("convenio_id"));
        c.setObservacoes(rs.getString("observacoes"));
        c.setRealizada(rs.getBoolean("realizada")); // Certifique-se que Consulta.java tem o campo
        return c;
    }

    @Override
    public boolean atualizar(Consulta entidade) {
        throw new UnsupportedOperationException("Não implementado ainda.");
    }

    @Override
    public boolean excluir(long id) {
        throw new UnsupportedOperationException("Não implementado ainda.");
    }

    @Override
    public Consulta buscarPorId(long id) {
        throw new UnsupportedOperationException("Não implementado ainda.");
    }

    @Override
    public List<Consulta> listarTodos() {
        List<Consulta> lista = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        String sql = "SELECT id, paciente_id, medico_id, data_consulta, hora_consulta, convenio_id, observacoes, realizada FROM consulta WHERE realizada = 0 ORDER BY data_consulta, hora_consulta";

        try {
            conn = getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                lista.add(mapResultSetToConsulta(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao listar consultas: " + e.getMessage());
        } finally {
            fecharRecursos(conn, stmt, rs); 
        }
        return lista;
    }
    public List<Consulta> listarRealizadas() {
    List<Consulta> lista = new ArrayList<>();
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;

    String sql = "SELECT id, paciente_id, medico_id, data_consulta, hora_consulta, convenio_id, observacoes, realizada FROM consulta WHERE realizada = 1 ORDER BY data_consulta DESC, hora_consulta DESC";

    try {
        conn = getConnection();
        stmt = conn.createStatement();
        rs = stmt.executeQuery(sql);
        
        while (rs.next()) {
            lista.add(mapResultSetToConsulta(rs));
        }
        
    } catch (SQLException e) {
        System.err.println("Erro ao listar consultas realizadas: " + e.getMessage());
    } finally {
  
        fecharRecursos(conn, stmt, rs); 
    }
    return lista;
}
}