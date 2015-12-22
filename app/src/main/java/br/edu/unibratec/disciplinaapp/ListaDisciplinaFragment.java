package br.edu.unibratec.disciplinaapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import br.edu.unibratec.disciplinaapp.basica.Curso;
import br.edu.unibratec.disciplinaapp.basica.Disciplina;
import br.edu.unibratec.disciplinaapp.basica.Instituicao;

/**
 * Created by Leopoldo on 09/10/2015.
 *               Updated: 31/10/2015.
 *  ÍNDICE
 *  ======
 *  01) onCreate
 *  02) onActivityCreated
 *  03) onCreateView        - MÉTODO DE CRIAÇÃO DA LISTA COM O REFRESH
 *  04) onRefresh
 *  05) carregarDisciplinas - MÉTODO PARA CONECTAR-SE À REDE
 *  06) onListItemClick     - MÉTODO DE COMPORTAMENTO DO CLIQUE NO ITEM DA LIST PARA EXIBIR OS DETALHES
 *  07) mostrarItemLista
 *  08) DisciplinaTask      - INNER CLASS PARA ACESSAR ARQUIVO *.json
 *      8.1 - doInBackground
 *      8.2 - onPostExecute
 */
public class ListaDisciplinaFragment extends ListFragment implements SwipeRefreshLayout.OnRefreshListener
{
    List<Disciplina> mDisciplinas;
    DisciplinaAdapter mAdapter;
    SwipeRefreshLayout mSwipe;
    DisciplinaTask mDiscTask;

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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // VERIFICAR SE A LIST ESTÁ VAZIA PARA CRIAR UMA NOVA LIST, SE NECESSÁRIO
        if(mDisciplinas.isEmpty()) {
            mAdapter = new DisciplinaAdapter(getActivity(), mDisciplinas);
            setListAdapter(mAdapter); // SETAR ADAPTER COM A LISTA VAZIA
            carregarDisciplinas();
        }
    }

    /** 03
     *  MÉTODO DE CRIAÇÃO DA LISTA COM O REFRESH
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_disciplinas, null);
        mSwipe = (SwipeRefreshLayout)view.findViewById(R.id.swipe);
        mSwipe.setOnRefreshListener(this); // dispara quando o usuário fizer o Swipe
        return view;
    }

    /** 04
     */
    @Override
    public void onRefresh() {
        carregarDisciplinas(); // IMPLEMENTADO ABAIXO
    }

    /** 05
     *  MÉTODO PARA CONECTAR-SE À REDE. CASO NÃO HOUVER CONEXÃO, UMA MENSAGEM 'Toast' É EXIBIDA.
     */
    private void carregarDisciplinas(){
        ConnectivityManager connManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){

            if(mDiscTask == null) {
                mDiscTask = new DisciplinaTask();
                mDiscTask.execute();
                mSwipe.setRefreshing(true);
            } else {
                mSwipe.setRefreshing(false);
            }
        }else{
            mSwipe.setRefreshing(false); // parar o Refresh
            Toast.makeText(getActivity(),R.string.no_connection_msg, Toast.LENGTH_SHORT).show();
        }
    }

    /** 06
     *  MÉTODO DE COMPORTAMENTO DO CLIQUE NO ITEM DA LIST PARA EXIBIR OS DETALHES
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Disciplina disciplina = mDisciplinas.get(position);
        mostrarItemLista(disciplina); // IMPLEMENTADO ABAIXO
    }

    /** 07
     */
    private void mostrarItemLista(Disciplina disciplina){
        if(getActivity() instanceof AoClicarNaDisciplina){
            /** MÉTODO disciplinaClicada:
             *      - para saber se é SMARTPHONE ou TABLET
             *      - implementada na classe DisciplinaActivity
             */
            ((AoClicarNaDisciplina)getActivity()).disciplinaClicada(disciplina);
        }
    }

    /** 08
     *  INNER CLASS - ACESSAR ARQUIVO *.json
     */
    class DisciplinaTask extends AsyncTask<Void, Void, Instituicao>{

        @Override
        protected Instituicao doInBackground(Void... params) {

            OkHttpClient client = new OkHttpClient();

            client.setConnectTimeout(10, TimeUnit.SECONDS); // 10s PARA CONECTAR
            client.setReadTimeout(15, TimeUnit.SECONDS); // TEMPO DE LEITURA DO SERVER

            Request request = new Request.Builder()
                    .url("https://dl.dropboxusercontent.com/s/i77om4mqacxjgtc/unibratec.json")
                    .build();

            Response response;
            try {
                response = client.newCall(request).execute();
                String s = response.body().string();
                //s += "dfbdbdb";
                Gson gson = new Gson();
                Instituicao instituicao = gson.fromJson(s, Instituicao.class);
                return  instituicao;
            } catch (Throwable e) { // Exception e Error HERDAM DE Throwable
                e.printStackTrace();
            }
            return null;
        }

        // ATUALIZAR A INTERFACE GRÁFICA
        // ESTE MÉTODO SÓ EXECUTARÁ SE HOUVER CONEXÃO
        @Override
        protected void onPostExecute(Instituicao instituicao) {
            super.onPostExecute(instituicao);

            if(instituicao != null && instituicao.cursos != null) {
                mDisciplinas.clear(); // LIMPAR A LISTA PARA ADICIONAR OS ITENS
                for (Curso curso: instituicao.cursos) {
                    mDisciplinas.addAll(curso.disciplinas);
                }
                mAdapter.notifyDataSetChanged(); // ATUALIZAR A LISTA (DAR REFRESH)

                if(getResources().getBoolean(R.bool.tablet)){
                    mostrarItemLista(mDisciplinas.get(0));
                }
            }else{
                Toast.makeText(getActivity(), R.string.msg_erro, Toast.LENGTH_SHORT).show();
            }
            mSwipe.setRefreshing(false); // PARAR O Refresh
        }
    }
}
