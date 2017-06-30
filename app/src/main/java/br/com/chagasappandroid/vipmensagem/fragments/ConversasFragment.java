package br.com.chagasappandroid.vipmensagem.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.chagasappandroid.vipmensagem.R;
import br.com.chagasappandroid.vipmensagem.activity.ConversaActivity;
import br.com.chagasappandroid.vipmensagem.adapter.ContatoAdapter;
import br.com.chagasappandroid.vipmensagem.adapter.ConversaAdapter;
import br.com.chagasappandroid.vipmensagem.config.ConfiguracaoFirebase;
import br.com.chagasappandroid.vipmensagem.helper.Base64Custom;
import br.com.chagasappandroid.vipmensagem.helper.Preferencias;
import br.com.chagasappandroid.vipmensagem.model.Conversa;


public class ConversasFragment extends Fragment {
    private ArrayList<Conversa> arrayListConversa;
    private ArrayAdapter arrayAdapter;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListener;

    public ConversasFragment() {

    }

    @Override
    public void onStart() {
        super.onStart();
        firebase.addValueEventListener(valueEventListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListener);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        arrayListConversa= new ArrayList<>();

        View view = inflater.inflate(R.layout.fragment_conversas, container, false);

        ListView listView = (ListView)view.findViewById(R.id.lv_frag_conversas);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(),ConversaActivity.class);
                Conversa conversa = arrayListConversa.get(position);
                intent.putExtra("nome", conversa.getNome());
                String email = Base64Custom.decodificarBase64(conversa.getIdUsuario());
                intent.putExtra("email", email);

                startActivity(intent);
            }
        });

        arrayAdapter = new ConversaAdapter(getActivity(),arrayListConversa);
        listView.setAdapter(arrayAdapter);

        Preferencias preferencias = new Preferencias(getActivity());
        String idUsuario = preferencias.getIdentificador();


        firebase = ConfiguracaoFirebase.getFirebase().child("conversas").child(idUsuario);
        valueEventListener = new ValueEventListener(){

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayListConversa.clear();
                for (DataSnapshot dados : dataSnapshot.getChildren()){
                    Conversa conversa = dados.getValue(Conversa.class);
                    arrayListConversa.add(conversa);

                }

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        };

        return view;
    }

}
