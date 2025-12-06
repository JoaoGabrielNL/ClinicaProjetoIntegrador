package br.edu.imepac.clinica.entidades;

public class PortalPaciente {
    private Long pacienteId;
    private String login;
    private String senha;

    public PortalPaciente() {}

    public Long getPacienteId() { return pacienteId; }
    public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}