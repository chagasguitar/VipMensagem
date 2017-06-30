package br.com.chagasappandroid.vipmensagem.adapter;


import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.chagasappandroid.vipmensagem.R;
import br.com.chagasappandroid.vipmensagem.model.Contatos;

import static br.com.chagasappandroid.vipmensagem.R.string.contatos;

public class ContatoAdapter extends ArrayAdapter{

    private ArrayList<Contatos> arrayListContatos;
    private Context context;

    public ContatoAdapter(@NonNull Context context, @NonNull ArrayList<Contatos> objects) {
        super(context,0, objects);
        this.context = context;
        this.arrayListContatos = objects;
    }

    @NonNull
    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {
        View view = null;

        if ( arrayListContatos!= null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = layoutInflater.inflate(R.layout.layout_contatos, parent,false);

            TextView nomeContato = (TextView)view.findViewById(R.id.tv_nome);
            TextView emailContato = (TextView)view.findViewById(R.id.tv_email);

            Contatos contatos = arrayListContatos.get(position);
            nomeContato.setText(contatos.getNome());
            emailContato.setText(contatos.getEmail());
        }
        return view;
    }
}
