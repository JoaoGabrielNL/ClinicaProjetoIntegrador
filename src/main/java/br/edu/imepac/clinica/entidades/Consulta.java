package br.edu.imepac.clinica.entidades;

public class Consulta {
    private Long id;
    private Long pacienteId;
    private Long medicoId;
    private String dataConsulta; 
    private String horaConsulta; 
    private String observacoes;
    private Long convenioId; 

    public Consulta() {}

    private boolean realizada;

    public boolean isRealizada() {
        return realizada;
    }

    public void setRealizada(boolean realizada) {
        this.realizada = realizada;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPacienteId() { return pacienteId; }
    public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }

    public Long getMedicoId() { return medicoId; }
    public void setMedicoId(Long medicoId) { this.medicoId = medicoId; }

    public String getDataConsulta() { return dataConsulta; }
    public void setDataConsulta(String dataConsulta) { this.dataConsulta = dataConsulta; }

    public String getHoraConsulta() { return horaConsulta; }
    public void setHoraConsulta(String horaConsulta) { this.horaConsulta = horaConsulta; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public Long getConvenioId() { return convenioId; }
    public void setConvenioId(Long convenioId) { this.convenioId = convenioId; }
}