package br.edu.unibratec.disciplinaapp.persist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Leopoldo on 28/10/2015.
 *               Updated: 30/10/2015.
 *  ÍNDICE
 *  ======
 *  01) onCreate
 *  02) onUpgrade
 */

public class DisciplinaDbHelper extends SQLiteOpenHelper {

    // NOME E VERSÃO DO BANCO DE DADOS, RESPECTIVAMENTE
    public static final String DISC_DATABASE  = "disc_database";
    public static final int    DB_VERSION     = 1;

    // NOME DA TABELA
    public static final String TABLE_DISCIPLINA = "table_disciplina";

    // CAMPOS DA TABELA
    public static final String CAMPO_ID         = "_id";
    public static final String CAMPO_NOME       = "nome";
    public static final String CAMPO_GRADUACAO  = "graduacao";
    public static final String CAMPO_PROFESSOR  = "professor";
    public static final String CAMPO_PERIODO    = "periodo";
    public static final String CAMPO_FOTO       = "fotoFavorito";

    public DisciplinaDbHelper(Context context) {
        super(context, DISC_DATABASE, null, DB_VERSION);
    }

    /** 01
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_DISCIPLINA + "(" + CAMPO_ID        + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                              CAMPO_NOME      + " TEXT NOT NULL, " +
                                                              CAMPO_GRADUACAO + " TEXT NOT NULL, " +
                                                              CAMPO_PROFESSOR + " TEXT NOT NULL, " +
                                                              CAMPO_PERIODO   + " INTEGER NOT NULL, " +
                                                              CAMPO_FOTO      + " TEXT NOT NULL);");
    }

    /** 02
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
