package br.edu.unibratec.disciplinaapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import br.edu.unibratec.disciplinaapp.basica.Disciplina;
import br.edu.unibratec.disciplinaapp.persist.DisciplinaDAO;

/**
 * Created by Leopoldo on 09/10/2015.
 *               Updated: 31/10/2015.
 *  ÍNDICE
 *  ======
 *  01) onCreate            - INSTANCIAR A LISTA
 *  02) onAttach
 *  03) onDetach
 *  04) onActivityCreated   - CARREGAR LISTA
 *  05) carregarDisciplinas - BUSCAR O QUE ESTÁ NO DAO
 *  06) onListItemClick     - MÉTODO DE COMPORTAMENTO DO CLIQUE NO ITEM DA LIST PARA EXIBIR OS DETALHES
 *  07) favoriteListUpdated - FAZ COM QUE A CLASSE OUÇA UM EVENTO DISPARADO POR UM Bus
 */
public class ListaDisciplinaFavoriteFragment extends ListFragment
{
    List<Disciplina> mDisciplinas;
    DisciplinaAdapter mAdapter;

    /** 01
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // QUANDO GIRAR A Activity
        // Activity E A View Do Fragment (layout) SÃO DESTRUÍDAS
        // MAS APENAS A INSTÂNCIA DO Fragment PERMANECE
        setRetainInstance(true);
        mDisciplinas = new ArrayList<>();
    }

    /** 02
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bus bus = ((DisciplinaApplication) getActivity().getApplication()).getBus();
        bus.register(this); // CLASSE QUE QUER OUVIR A MENSAGEM
    }

    /** 03
     */
    @Override
    public void onDetach() {
        super.onDetach();
        Bus bus = ((DisciplinaApplication) getActivity().getApplication()).getBus();
        bus.unregister(this);
    }

    /** 04
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

       // mDisciplinas = new ArrayList<>(); ---> transferido para o onCreate

        // VERIFICAR SE A LIST ESTÁ VAZIA PARA CRIAR UMA NOVA LIST, SE NECESSÁRIO
        if(mDisciplinas.isEmpty()) {
            mAdapter = new DisciplinaAdapter(getActivity(), mDisciplinas);
            setListAdapter(mAdapter); // SETAR ADAPTER COM A LISTA VAZIA
            carregarDisciplinas();
        }
    }

    /** 05
     */
    private void carregarDisciplinas(){
        DisciplinaDAO disciplinaDAO = new DisciplinaDAO(getActivity());
        mDisciplinas.clear();
        mDisciplinas.addAll(disciplinaDAO.listar());
        mAdapter.notifyDataSetChanged();
    }

    /** 06
     *  MÉTODO DE COMPORTAMENTO DO CLIQUE NO ITEM DA LIST PARA EXIBIR OS DETALHES
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Disciplina disciplina = mDisciplinas.get(position);
        if(getActivity() instanceof AoClicarNaDisciplina){
            /** MÉTODO disciplinaClicada:
             *      - para saber se é SMARTPHONE ou TABLET
             *      - implementada na classe DisciplinaActivity
             */
            ((AoClicarNaDisciplina)getActivity()).disciplinaClicada(disciplina);
        }
    }

    /** 07
     */
    @Subscribe
    public void favoriteListUpdated(Disciplina disciplina){
        carregarDisciplinas();
    }
}
