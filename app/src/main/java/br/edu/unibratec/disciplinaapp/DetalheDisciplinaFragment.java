package br.edu.unibratec.disciplinaapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Bus;
import com.squareup.picasso.Picasso;

import br.edu.unibratec.disciplinaapp.basica.Disciplina;
import br.edu.unibratec.disciplinaapp.persist.DisciplinaDAO;

/**
 * Created by Leopoldo on 09/10/2015.
 *               Updated: 30/10/2015.
 *  ÍNDICE
 *  ======
 *  01) newInstance           - "FACTORY METHOD"
 *  02) onCreate
 *  03) onActivityCreated
 *  04) onCreateView
 *  05) onCreateOptionsMenu   - CARREGAR O ARQUIVO DE MENU
 *  06) updateItemMenu        - ATUALIZAR ÍCONE DO MENU
 *  07) onOptionsItemSelected - TRATAMENTO DA OPÇÃO DE MENU
 */

public class DetalheDisciplinaFragment extends Fragment {

    Disciplina mDisciplina;
    MenuItem mMenuItemFavorite;
    DisciplinaDAO mDisciplinaDAO;

    /** 01
     *  RETORNAR UM ELEMENTO DA PRÓPRIA CLASSE SEM PRECISAR DE UMA INSTÂNCIA
     *  FACTORY METHOD (opcional)
     */
    public static DetalheDisciplinaFragment newInstance(Disciplina disciplina){

        Bundle parametros = new Bundle();
        parametros.putSerializable("disciplina", disciplina);

        DetalheDisciplinaFragment ddf = new DetalheDisciplinaFragment();
        ddf.setArguments(parametros);
        return ddf;
    }

    /** 02
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // CARREGAR OPÇÃO DE MENU
        // FRAGMENT POR PADRÃO NÃO POSSUI OPÇÃO DE MENU
        setHasOptionsMenu(true);
    }

    /** 03
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mDisciplinaDAO = new DisciplinaDAO(getActivity());
    }

    /** 04
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // PEGAR O OBJETO
        mDisciplina = (Disciplina)getArguments().getSerializable("disciplina");

        // CARREGAR ARQUIVO DE LAYOUT
        View layoutFile = inflater.inflate(R.layout.fragment_detalhe_disciplina, container, false);

        // PREENCHER AS VIEWS
        ImageView fotoDet  = (ImageView)layoutFile.findViewById(R.id.circle);
        TextView nome      = (TextView)layoutFile.findViewById(R.id.txtNome);
        TextView graduacao = (TextView)layoutFile.findViewById(R.id.txtGraduacao);
        TextView professor = (TextView)layoutFile.findViewById(R.id.txtProfessor);
        TextView periodo   = (TextView)layoutFile.findViewById(R.id.txtPeriodo);

        Picasso.with(getContext()).load(mDisciplina.capaDisciplina).into(fotoDet);
        nome.setText(mDisciplina.nome);
        graduacao.setText(mDisciplina.graduacao);
        professor.setText(mDisciplina.professor);
        periodo.setText(String.valueOf(mDisciplina.periodo));

        // RETORNAR VIEW PREENCHIDA
        return layoutFile;
    }

    /** 05
     *  CARREGAR O ARQUIVO DE MENU
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_detalhe, menu); // PARÂMETROS: Arquivo de menu + objeto menu

        mMenuItemFavorite = menu.findItem(R.id.acao_favorito);

        updateItemMenu(mDisciplinaDAO.isFavorite(mDisciplina));
    }

    /** 06
     *  ATUALIZAR ÍCONE DO MENU
     */
    private void updateItemMenu(boolean favorite){
        mMenuItemFavorite.setIcon(favorite ? android.R.drawable.ic_delete : android.R.drawable.ic_input_add);
    }

    /** 07
     *  TRATAMENTO DA OPÇÃO DE MENU - VERIFICA SE É FAVORITO E ATUALIZA
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.acao_favorito){

            boolean favorite = mDisciplinaDAO.isFavorite(mDisciplina);
            if (favorite){
                mDisciplinaDAO.excluir(mDisciplina);
                Toast.makeText(getActivity(), R.string.msg_removed, Toast.LENGTH_SHORT).show();
            }else{
                mDisciplinaDAO.inserir(mDisciplina);
                Toast.makeText(getActivity(), R.string.msg_inserted, Toast.LENGTH_SHORT).show();

            }
            updateItemMenu(!favorite); // ACIMA
            Bus bus = ((DisciplinaApplication) getActivity().getApplication()).getBus(); // RETORNA INSTÂNCIA DE APPLICATION
            bus.post(mDisciplina); // CLASSE QUE QUER ENVIAR A MENSAGEM, NÃO PODE ESTAR VAZIO
        }
        return super.onOptionsItemSelected(item);
    }
}
