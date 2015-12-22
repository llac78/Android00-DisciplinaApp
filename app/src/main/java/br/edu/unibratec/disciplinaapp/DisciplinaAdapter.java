package br.edu.unibratec.disciplinaapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.edu.unibratec.disciplinaapp.basica.Disciplina;

/**
 * Created by Leopoldo on 22/10/2015.
 *
 *  ÍNIDICE
 *  =======
 *  01) getView
 *  02) ViewHolder - INNER CLASS (Adapter EFICIENTE)
 */
public class DisciplinaAdapter extends ArrayAdapter<Disciplina> {
    public DisciplinaAdapter(Context context, List<Disciplina> disciplinas) {
        // 0 (zero) SERIA O ARQUIVO DE LAYOUT PARA CADA ITEM DA LISTA
        // CASO O ADAPTER FOSSE PERSONALIZADO
        // 0 (zero) IGNORA O ARQUIVO DE LAYOUT
        super(context, 0, disciplinas);
    }

    /** 01
     *  4 PASSOS DO "MANTRA SAGRADO"
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Disciplina disciplina = getItem(position);
        ViewHolder holder;

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_disciplina, null);
            holder = new ViewHolder();
            holder.imgDisciplina = (ImageView)convertView.findViewById(R.id.imgDisciplina);
            holder.txtCursoItem  = (TextView) convertView.findViewById(R.id.txtCursoItem);
            holder.txtNomeItem   = (TextView) convertView.findViewById(R.id.txtNomeItem);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag(); // PEGAR A INSTÂNCIA DO HOLDER, CASO convertView NÃO FOR NULO
        }

        // PICASSO: biblioteca para carregar as imagens cujos nomes estão contidos no arquivo *.json
        Picasso.with(getContext()).load(disciplina.capaDisciplina).into(holder.imgDisciplina);
        holder.txtCursoItem.setText(disciplina.graduacao);
        holder.txtNomeItem.setText(disciplina.nome);

        return convertView;
    }

    /** 02
     *  INNER CLASS - ADAPTER EFICIENTE
     */
    class ViewHolder{
        ImageView imgDisciplina;
        TextView txtNomeItem;
        TextView txtCursoItem;
    }
}
