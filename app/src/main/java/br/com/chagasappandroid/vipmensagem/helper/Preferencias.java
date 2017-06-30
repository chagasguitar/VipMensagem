package br.com.chagasappandroid.vipmensagem.helper;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;


public class Preferencias {
    private Context context;
    private SharedPreferences sharedPreferences;
    private static final String  NOME_ARQUIVO = "nome.arquivo";
    private SharedPreferences.Editor editor;
    private final String CHAVE_IDENTIFICADOR = "identificadorUsuarioLogado";
    private final String CHAVE_NOME = "nomeUsuarioLogado";

    public Preferencias(Context contextparam) {

        context = contextparam;
        sharedPreferences = context.getSharedPreferences(NOME_ARQUIVO,Context.MODE_PRIVATE);
        editor= sharedPreferences.edit();

    }

    public void salvarDados(String idUsuario, String nomeUsuario){

        editor.putString(CHAVE_IDENTIFICADOR,idUsuario);
        editor.putString(CHAVE_NOME,nomeUsuario);
        editor.commit();
    }

    public String getIdentificador(){
        return sharedPreferences.getString(CHAVE_IDENTIFICADOR, null) ;
    }

    public String getINome(){
        return sharedPreferences.getString(CHAVE_NOME, null) ;
    }

}
