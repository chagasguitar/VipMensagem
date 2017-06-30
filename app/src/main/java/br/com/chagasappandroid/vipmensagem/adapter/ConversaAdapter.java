package br.com.chagasappandroid.vipmensagem.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.chagasappandroid.vipmensagem.R;
import br.com.chagasappandroid.vipmensagem.model.Conversa;

public class ConversaAdapter extends ArrayAdapter{

    private Context context;
    private ArrayList<Conversa> infoConversa;

    public ConversaAdapter(Context context, ArrayList<Conversa> infoConversa) {
        super(context, 0, infoConversa);
        this.context = context;
        this.infoConversa = infoConversa;
    }

    @NonNull
    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {

        View view = null;

        if (infoConversa!= null) {

            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.layout_conversas, parent, false);

            TextView textView = (TextView) view.findViewById(R.id.text_frag_conversa);
            TextView textView1 = (TextView) view.findViewById(R.id.text2_frag_conversa);

            Conversa conversa = infoConversa.get(position);


            textView.setText(conversa.getNome());
            textView1.setText(conversa.getMensagem());
        }

        return view;
    }
}
