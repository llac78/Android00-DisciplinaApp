package br.edu.unibratec.disciplinaapp;

import br.edu.unibratec.disciplinaapp.basica.Disciplina;

/**
 *  Created by Leopoldo.
 *
 *  INTERFACE - PARA DESACOPLAR A CLASSE DetalheActivity DA ListFragment
 *  DETERMINAR UM EVENTO QUE ESTÁ ACONTECENDO NO ListFragment (clique)
 */
interface AoClicarNaDisciplina{
    void disciplinaClicada(Disciplina disciplina);
}