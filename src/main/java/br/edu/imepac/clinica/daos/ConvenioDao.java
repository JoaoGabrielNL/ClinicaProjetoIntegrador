package br.edu.imepac.clinica.daos;

import br.edu.imepac.clinica.entidades.Convenio;
import br.edu.imepac.clinica.interfaces.Persistente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ConvenioDao extends BaseDao implements Persistente<Convenio> {

    @Override
    public boolean salvar(Convenio convenio) {
        String sql = "INSERT INTO convenio (nome, descricao) VALUES (?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            ps.setString(1, convenio.getNome());
            ps.setString(2, convenio.getDescricao());
            
            int linhasAfetadas = ps.executeUpdate();
            
            if (linhasAfetadas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        convenio.setId(rs.getLong(1));
                        return true;
                    }
                }
            }
            return false;
            
        } catch (SQLException e) {
            // FORÇA A EXCEÇÃO PARA O CONVENIOADDFORM EXIBIR O ERRO SQL
            throw new RuntimeException("Erro de Banco de Dados ao inserir convênio: " + e.getMessage(), e); 
        } finally {
            fecharRecursos(conn, ps);
        }
    }

    @Override
    public boolean atualizar(Convenio convenio) {
        String sql = "UPDATE convenio SET nome = ?, descricao = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            
            ps.setString(1, convenio.getNome());
            ps.setString(2, convenio.getDescricao());
            ps.setLong(3, convenio.getId());
            int linhas = ps.executeUpdate();
            return linhas > 0;
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro de Banco de Dados ao atualizar convênio: " + e.getMessage(), e);
        } finally {
            fecharRecursos(conn, ps);
        }
    }

    @Override
    public boolean excluir(long id) {
        String sql = "DELETE FROM convenio WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setLong(1, id);
            int linhas = ps.executeUpdate();
            return linhas > 0;
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro de Banco de Dados ao excluir convênio: " + e.getMessage(), e);
        } finally {
            fecharRecursos(conn, ps);
        }
    }

    @Override
    public Convenio buscarPorId(long id) {
        String sql = "SELECT id, nome, descricao FROM convenio WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setLong(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToConvenio(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao buscar convênio por ID: " + e.getMessage());
        } finally {
            fecharRecursos(conn, ps, rs);
        }
        return null;
    }

    @Override
    public List<Convenio> listarTodos() {
        List<Convenio> lista = new ArrayList<>();
        String sql = "SELECT id, nome, descricao FROM convenio ORDER BY nome";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                lista.add(mapResultSetToConvenio(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao listar convênios: " + e.getMessage());
        } finally {
            fecharRecursos(conn, stmt, rs); 
        }
        return lista;
    }

    private Convenio mapResultSetToConvenio(ResultSet rs) throws SQLException {
        Convenio c = new Convenio();
        c.setId(rs.getLong("id"));
        c.setNome(rs.getString("nome"));
        c.setDescricao(rs.getString("descricao"));
        return c;
    }

}