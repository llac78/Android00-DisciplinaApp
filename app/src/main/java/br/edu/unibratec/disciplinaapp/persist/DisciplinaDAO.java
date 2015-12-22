package br.edu.unibratec.disciplinaapp.persist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.edu.unibratec.disciplinaapp.basica.Disciplina;

/**
 * Created by Leopoldo on 29/10/2015.
 *               Updated: 30/10/2015.
 *  ÍNDICE
 *  ======
 *  01) inserir    - MÉTODO PARA INSERIR UM OBJETO NO BANCO DE DADOS
 *  02) excluir    - MÉTODO PARA EXCLUIR UM OBJETO NO BANCO DE DADOS
 *  03) listar     - MÉTODO PARA LISTAR OBJETOS DO BANCO DE DADOS
 *  04) isFavorite - MÉTODO PARA VERIFICAR SE O OBJETO JÁ ESTÁ NA LISTA DE FAVORITOS
 */

public class DisciplinaDAO {

    DisciplinaDbHelper mHelper;

    //CONSTRUTOR
    public DisciplinaDAO(Context context) {
        mHelper = new DisciplinaDbHelper(context);
    }

    /** 01
     */
    public void inserir(Disciplina disciplina) {
        SQLiteDatabase database = mHelper.getWritableDatabase(); // ESCREVER INFORMAÇÃO NO BANCO DE DADOS (Writable)

        ContentValues values = new ContentValues();
        values.put(DisciplinaDbHelper.CAMPO_NOME,      disciplina.nome);
        values.put(DisciplinaDbHelper.CAMPO_GRADUACAO, disciplina.graduacao);
        values.put(DisciplinaDbHelper.CAMPO_PROFESSOR, disciplina.professor);
        values.put(DisciplinaDbHelper.CAMPO_PERIODO,   disciplina.periodo);
        values.put(DisciplinaDbHelper.CAMPO_FOTO,      disciplina.capaDisciplina);

        long id = database.insert(DisciplinaDbHelper.TABLE_DISCIPLINA, null, values); // null = VALOR PADRÃO PARA COLUNAS QUE ACEITAM null, MAS QUE NÃO FORAM PASSADOS
        // QUANDO OCORRE UM INSERT, O ID DO REGISTRO INSERIDO É RETORNADO
        // VERIFICAR SE HAVERÁ ERRO (-1)
        if (id == -1) {
            throw new RuntimeException("Registro não efetuado.");
        }
        database.close();
    }

    /** 02
     */
    public void excluir(Disciplina disciplina) {
        SQLiteDatabase database = mHelper.getWritableDatabase();
        String sql = DisciplinaDbHelper.CAMPO_NOME + " = ? AND " + DisciplinaDbHelper.CAMPO_PROFESSOR + " = ?";

        database.delete(DisciplinaDbHelper.TABLE_DISCIPLINA, sql, new String[]{disciplina.nome, disciplina.professor});
        database.close();
    }

    /** 03
     */
    public List<Disciplina> listar() {
        SQLiteDatabase database = mHelper.getReadableDatabase(); // FAZER LEITURA NO BANCO DE DADOS (Readable)
        String sql = "SELECT * FROM " + DisciplinaDbHelper.TABLE_DISCIPLINA + " ORDER BY " + DisciplinaDbHelper.CAMPO_NOME + ";";

        // DATASET = CONJUNTO DE DADOS
        Cursor cursor = database.rawQuery(sql, null);

        // SE NÃO SOUBER A ORDEM DAS COLUNAS, RECOMENDA-SE PEGAR O ÍNDICE
        int indiceNome      = cursor.getColumnIndex(DisciplinaDbHelper.CAMPO_NOME);
        int indiceGraduacao = cursor.getColumnIndex(DisciplinaDbHelper.CAMPO_GRADUACAO);
        int indiceProfessor = cursor.getColumnIndex(DisciplinaDbHelper.CAMPO_PROFESSOR);
        int indicePeriodo   = cursor.getColumnIndex(DisciplinaDbHelper.CAMPO_PERIODO);
        int indiceFoto      = cursor.getColumnIndex(DisciplinaDbHelper.CAMPO_FOTO);

        List<Disciplina> disciplinas = new ArrayList<>();

        // APONTAR PARA OS REGISTROS, CASO EXISTAM
        // PREENCHER A LIST
        while (cursor.moveToNext()) {
            String nome = cursor.getString(indiceNome);
            String grad = cursor.getString(indiceGraduacao);
            String prof = cursor.getString(indiceProfessor);
            int periodo = cursor.getInt(indicePeriodo);
            String foto = cursor.getString(indiceFoto);

            Disciplina disciplina = new Disciplina(nome, grad, prof, periodo, foto);
            disciplinas.add(disciplina);
        }
        cursor.close();
        database.close();

        return disciplinas;
    }

    /** 04
     */
    public boolean isFavorite(Disciplina disciplina) {

        SQLiteDatabase database = mHelper.getReadableDatabase();
        String sql = "SELECT _id FROM " + DisciplinaDbHelper.TABLE_DISCIPLINA +
                     " WHERE "          + DisciplinaDbHelper.CAMPO_NOME + " = ? AND "
                                        + DisciplinaDbHelper.CAMPO_PROFESSOR  + " = ? ";

        Cursor cursor = database.rawQuery(sql, new String[]{ disciplina.nome, disciplina.professor});
        boolean tem = cursor.getCount() > 0;
        cursor.close();
        database.close();

        return tem;
    }
}
