package br.edu.unibratec.disciplinaapp;

import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;

        import br.edu.unibratec.disciplinaapp.basica.Disciplina;

/**
 * Created by Leopoldo on 09/10/2015.
 *
 *  ÍNDICE
 *  ======
 *  01) onCreate
 */

public class DetalheDisciplinaActivity extends AppCompatActivity {

    /** 01
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_disciplina);

        if(savedInstanceState == null) {
            // MÉTODO PARA RECEBER O PARÂMETRO DA INTENT
            Disciplina disciplina = (Disciplina)getIntent().getSerializableExtra("disciplina");

            // ADICIONAR O Fragment DE DETALHE NA TELA
            DetalheDisciplinaFragment ddf = DetalheDisciplinaFragment.newInstance(disciplina);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.detalhe, ddf)
                    .commit();
        }
    }
}
