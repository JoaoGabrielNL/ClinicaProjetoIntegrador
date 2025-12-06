package br.edu.imepac.clinica.daos;

import br.edu.imepac.clinica.entidades.Paciente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteDao extends BaseDao {

    public void inserir(Paciente p) throws SQLException {
        String sql = "INSERT INTO paciente (nome, cpf, data_nascimento, telefone, email, endereco, ativo) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, p.getNome());
            ps.setString(2, p.getCpf());
            ps.setString(3, p.getDataNascimento());
            ps.setString(4, p.getTelefone());
            ps.setString(5, p.getEmail());
            ps.setString(6, p.getEndereco());
            ps.setBoolean(7, p.getAtivo() != null ? p.getAtivo() : true);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) p.setId(rs.getLong(1));
            }
        }
    }

    public void atualizar(Paciente p) throws SQLException {
        String sql = "UPDATE paciente SET nome=?, cpf=?, data_nascimento=?, telefone=?, email=?, endereco=?, ativo=? WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNome());
            ps.setString(2, p.getCpf());
            ps.setString(3, p.getDataNascimento());
            ps.setString(4, p.getTelefone());
            ps.setString(5, p.getEmail());
            ps.setString(6, p.getEndereco());
            ps.setBoolean(7, p.getAtivo() != null ? p.getAtivo() : true);
            ps.setLong(8, p.getId());
            ps.executeUpdate();
        }
    }

    public void inativar(Long id) throws SQLException {
        String sql = "UPDATE paciente SET ativo = 0 WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    public Paciente buscarPorId(Long id) throws SQLException {
        String sql = "SELECT id, nome, cpf, data_nascimento, telefone, email, endereco, ativo FROM paciente WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Paciente p = new Paciente();
                    p.setId(rs.getLong("id"));
                    p.setNome(rs.getString("nome"));
                    p.setCpf(rs.getString("cpf"));
                    p.setDataNascimento(rs.getString("data_nascimento"));
                    p.setTelefone(rs.getString("telefone"));
                    p.setEmail(rs.getString("email"));
                    p.setEndereco(rs.getString("endereco"));
                    p.setAtivo(rs.getBoolean("ativo"));
                    return p;
                }
            }
        }
        return null;
    }

    public List<Paciente> listarTodos() throws SQLException {
        List<Paciente> lista = new ArrayList<>();
        String sql = "SELECT id, nome, cpf, data_nascimento, telefone, email, endereco, ativo FROM paciente WHERE ativo = 1";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Paciente p = new Paciente();
                p.setId(rs.getLong("id"));
                p.setNome(rs.getString("nome"));
                p.setCpf(rs.getString("cpf"));
                p.setDataNascimento(rs.getString("data_nascimento"));
                p.setTelefone(rs.getString("telefone"));
                p.setEmail(rs.getString("email"));
                p.setEndereco(rs.getString("endereco"));
                p.setAtivo(rs.getBoolean("ativo"));
                lista.add(p);
            }
        }
        return lista;
    }

    public List<Paciente> buscarPorNomeOuCpf(String termo) throws SQLException {
        List<Paciente> lista = new ArrayList<>();
        String sql = "SELECT id, nome, cpf, data_nascimento, telefone, email, endereco, ativo FROM paciente WHERE (nome LIKE ? OR cpf LIKE ?) AND ativo = 1";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String like = "%" + termo + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Paciente p = new Paciente();
                    p.setId(rs.getLong("id"));
                    p.setNome(rs.getString("nome"));
                    p.setCpf(rs.getString("cpf"));
                    p.setDataNascimento(rs.getString("data_nascimento"));
                    p.setTelefone(rs.getString("telefone"));
                    p.setEmail(rs.getString("email"));
                    p.setEndereco(rs.getString("endereco"));
                    p.setAtivo(rs.getBoolean("ativo"));
                    lista.add(p);
                }
            }
        }
        return lista;
    }
}