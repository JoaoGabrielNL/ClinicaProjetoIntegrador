package br.edu.imepac.clinica.daos;

import br.edu.imepac.clinica.entidades.PortalPaciente;
import java.sql.*;

public class PortalPacienteDao extends BaseDao {

    public void criarAcesso(PortalPaciente pp) throws SQLException {
        // Aqui é o SQL para criar a entrada de login com coisas simples
        String sql = "INSERT INTO portal_paciente (paciente_id, login, senha) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, pp.getPacienteId());
            ps.setString(2, pp.getLogin());
            ps.setString(3, pp.getSenha());
            ps.executeUpdate();
        }
    }
        // Aqui verifica se o lginn existe
    public PortalPaciente buscarPorLoginSenha(String login, String senha) throws SQLException {
        String sql = "SELECT paciente_id, login, senha FROM portal_paciente WHERE login = ? AND senha = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, login);
            ps.setString(2, senha);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    PortalPaciente pp = new PortalPaciente();
                    pp.setPacienteId(rs.getLong("paciente_id"));
                    pp.setLogin(rs.getString("login"));
                    pp.setSenha(rs.getString("senha"));
                    return pp;
                }
            }
        }
        return null;
    }
}