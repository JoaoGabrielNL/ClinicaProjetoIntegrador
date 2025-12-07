package br.edu.imepac.clinica.entidades;

public class Atendimento {
    private Long id;
    private Long pacienteId;
    private String dataRegistro; 
    private String anamnese;
    private String diagnostico;
    private String conduta;
    private String medicoResponsavel;

    public Atendimento() {}


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPacienteId() { return pacienteId; }
    public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }

    public String getDataRegistro() { return dataRegistro; }
    public void setDataRegistro(String dataRegistro) { this.dataRegistro = dataRegistro; }

    public String getAnamnese() { return anamnese; }
    public void setAnamnese(String anamnese) { this.anamnese = anamnese; }

    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }

    public String getConduta() { return conduta; }
    public void setConduta(String conduta) { this.conduta = conduta; }

    public String getMedicoResponsavel() { return medicoResponsavel; }
    public void setMedicoResponsavel(String medicoResponsavel) { this.medicoResponsavel = medicoResponsavel; }
}