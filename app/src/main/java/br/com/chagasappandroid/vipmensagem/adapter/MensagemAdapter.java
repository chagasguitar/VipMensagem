package br.com.chagasappandroid.vipmensagem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.chagasappandroid.vipmensagem.R;
import br.com.chagasappandroid.vipmensagem.helper.Preferencias;
import br.com.chagasappandroid.vipmensagem.model.Mensagem;


public class MensagemAdapter extends ArrayAdapter<Mensagem> {
    private Context context;
    private ArrayList<Mensagem> mensagens;

    public MensagemAdapter(Context context, ArrayList<Mensagem> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mensagens = objects;
    }


    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {

        View view = null;

            if (mensagens != null){

                Preferencias preferencias = new Preferencias(context);
                String idUsuarioRemetente = preferencias.getIdentificador();

                LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                Mensagem mensagem = mensagens.get(position);

                if (idUsuarioRemetente.equals(mensagem.getIdUsuario()) ) {
                    view = layoutInflater.inflate(R.layout.item_mensagem_direita, parent, false);
                }else{
                    view = layoutInflater.inflate(R.layout.item_mensagem_esquerda, parent, false);
                }

                TextView textView = (TextView)view.findViewById(R.id.tv_mensagem);
                textView.setText(mensagem.getMensagem());
            }
        return view;
    }
}
