package br.edu.imepac.clinica.entidades;

public class Medico {
    private long id;
    private String nome;
    private String crm;
    private String telefone;
    private String email;
    private long especialidadeId;

    public Medico() {}

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCrm() { return crm; }
    public void setCrm(String crm) { this.crm = crm; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public long getEspecialidadeId() { return especialidadeId; }
    public void setEspecialidadeId(long especialidadeId) { this.especialidadeId = especialidadeId; }
}