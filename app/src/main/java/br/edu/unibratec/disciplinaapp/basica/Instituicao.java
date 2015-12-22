package br.edu.unibratec.disciplinaapp.basica;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Leopoldo on 15/10/2015.
 */
public class Instituicao {

    @SerializedName("unibratec")
    public List<Curso> cursos;
}
