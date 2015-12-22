package br.edu.unibratec.disciplinaapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import br.edu.unibratec.disciplinaapp.basica.Disciplina;

/** ÍNDICE
 *  ======
 *  01) onCreate
 *  02) disciplinaClicada - VERIFICAR SE É SMARTPHONE OU TABLET
 *  03) PaginasAdapter    - INNER CLASS ONDE SE CRIAM OS FRAGMENTS
 *      3.1 - getItem
 *      3.2 - getCount
 *      3.3 - getPageTitle
 */

public class DisciplinaActivity extends AppCompatActivity implements AoClicarNaDisciplina{

    /** 01
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disciplina);

        // Configurar os Fragments: pegar a referência do ViewPager
        ViewPager viewPager = (ViewPager)findViewById(R.id.viewPager);

        if(viewPager != null) {
            // DEFINIÇÃO DOS ADAPTERS DO ViewPager E ELE VAI CONSTRUIR AS INSTÂNCIAS DAS ABAS
            // -> PARA CADA ABA DO ViewPager HAVERÁ UM Fragment
            viewPager.setAdapter(new PaginasAdapter(getSupportFragmentManager()));

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            // CONSTRUÇÃO DOS NOMES DAS ABAS DE ACORDO COM O ViewPager (parâmetro)
            // ViewPager PERMITE DESLIZAR PARA ESQUERDA E DIREITA
            tabLayout.setupWithViewPager(viewPager); // SINCRONIZA O TabLayout com o ViewPager
        }
    }

    /** 02
     *  VERIFICAR SE É SMARTPHONE OU TABLET
     */
    @Override
    public void disciplinaClicada(Disciplina disciplina) {
        if(getResources().getBoolean(R.bool.smartphone)) {
            Intent it = new Intent(this, DetalheDisciplinaActivity.class);
            it.putExtra("disciplina", disciplina);
            startActivity(it);
        } else {
            DetalheDisciplinaFragment ddf = DetalheDisciplinaFragment.newInstance(disciplina);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.detalhe, ddf)
                    .commit();
        }
    }

    /** 03
     *  CLASSE ONDE SE CRIAM OS ADAPTERS
     *  ADAPTER DE FRAGMENTS - RETORNA FRAGMENTS
     */
    private class PaginasAdapter extends FragmentPagerAdapter {
        public PaginasAdapter(FragmentManager fm) { super(fm); }

        // RETORNAR QUAL FRAGMENT SERÁ EXIBIDO
        @Override
        public Fragment getItem(int position) {
            if(position == 0){
                return new ListaDisciplinaFragment();
            }else{
                return new ListaDisciplinaFavoriteFragment();
            }
        }

        // QUANTIDADE DE ABAS
        @Override
        public int getCount() {
            return 2;
        }

        // NOMES DAS ABAS
        @Override
        public CharSequence getPageTitle(int position) {
            return position == 0 ? "Web" : "Favoritos";
        }
    }
}
