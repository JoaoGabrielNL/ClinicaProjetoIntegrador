/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.imepac.clinica.interfaces;
/**
 *
 * @author [Geovani]
 */

public class PesquisaSatisfacao {
    private String pergunta;
    private int nota; // 0 a 10
    private String comentario;

    public PesquisaSatisfacao() {}

    public PesquisaSatisfacao(String pergunta, int nota, String comentario) {
        this.pergunta = pergunta;
        this.nota = nota;
        this.comentario = comentario;
    }

    public String getPergunta() {
        return pergunta;
    }

    public void setPergunta(String pergunta) {
        this.pergunta = pergunta;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    @Override
    public String toString() {
        return "PesquisaSatisfacao{" +
               "pergunta='" + pergunta + '\'' +
               ", nota=" + nota +
               ", comentario='" + comentario + '\'' +
               '}';
    }
}
