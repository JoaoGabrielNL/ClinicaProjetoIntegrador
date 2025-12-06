/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.imepac.clinica.interfaces;
/**
 *
 * @author [Geovani]
 */
public class RelatorioBasico {
    private String titulo;
    private String conteudo;

    public RelatorioBasico() {}

    public RelatorioBasico(String titulo, String conteudo) {
        this.titulo = titulo;
        this.conteudo = conteudo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    @Override
    public String toString() {
        return "RelatorioBasico{" +
               "titulo='" + titulo + '\'' +
               ", conteudo='" + conteudo + '\'' +
               '}';
    }
}
