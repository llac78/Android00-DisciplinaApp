package br.edu.unibratec.disciplinaapp;

import android.app.Application;

import com.squareup.otto.Bus;

/**
 * Created by Leopoldo on 29/10/2015.
 *
 *  ÍNDICE
 *  ======
 *  01) onCreate
 *  02) getBus   - ENVIAR E RECEBER MENSAGENS
 *
 *      * Classe para criar o Bus como um Singleton, para
 *        garantir apenas uma instância da classe que herda
 *        de Application
 *      * Deve ser colocada no Manifest (name) para informar
 *        que é a classe de Application
 *
 */
public class DisciplinaApplication extends Application {

    Bus mBus;

    /** 01
     */
    @Override
    public void onCreate() {
        super.onCreate();
        mBus = new Bus();
    }

    /** 02
     */
    public Bus getBus(){
        return mBus;
    }
}
