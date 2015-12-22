package br.edu.unibratec.disciplinaapp.basica;

import java.io.Serializable;

/**
 * Created by Leopoldo on 09/10/2015.
 *               Updated: 29/10/2015.
 */
public class Disciplina implements Serializable {
    // ATRIBUTES
    public String nome;
    public String graduacao;
    public String professor;
    public int periodo;
    public String capaDisciplina;

    // CONSTRUCTOR
    public Disciplina(String nome, String graduacao, String professor, int periodo, String capa) {
        this.nome = nome;
        this.graduacao = graduacao;
        this.professor = professor;
        this.periodo = periodo;
        this.capaDisciplina = capa;
    }

    @Override
    public String toString() {
        return nome + " - " + graduacao + " - " + professor + " - " + periodo + "° período.";
    }
}
