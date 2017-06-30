package br.com.chagasappandroid.vipmensagem.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.chagasappandroid.vipmensagem.R;
import br.com.chagasappandroid.vipmensagem.activity.ConversaActivity;
import br.com.chagasappandroid.vipmensagem.adapter.ContatoAdapter;
import br.com.chagasappandroid.vipmensagem.config.ConfiguracaoFirebase;
import br.com.chagasappandroid.vipmensagem.helper.Preferencias;
import br.com.chagasappandroid.vipmensagem.model.Contatos;


public class ContatosFragment extends Fragment {
    private ListView          listView;
    private ArrayAdapter      arrayAdapter;
    private ArrayList<Contatos> arrayContatos;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerContatos;



    public ContatosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        firebase.addValueEventListener(valueEventListenerContatos);
        Log.i("ValueEventListener","onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerContatos);
        Log.i("ValueEventListener","onStop");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        arrayContatos = new ArrayList<>();

        View view =inflater.inflate(R.layout.fragment_contatos, container, false);
        listView = (ListView)view.findViewById(R.id.lv_contatos);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ConversaActivity.class);

                Contatos contatos = arrayContatos.get(position);
                intent.putExtra("nome", contatos.getNome());
                intent.putExtra("email", contatos.getEmail());
                startActivity(intent);
            }
        });

        arrayAdapter = new ContatoAdapter(getActivity(),arrayContatos);

        listView.setAdapter(arrayAdapter);

        Preferencias preferencias = new Preferencias(getActivity());
        String identificadorUsuarioLogado = preferencias.getIdentificador();

        firebase = ConfiguracaoFirebase.getFirebase().child("contatos")
                .child(identificadorUsuarioLogado);

        valueEventListenerContatos = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayContatos.clear();
                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    Contatos contatos = dados.getValue(Contatos.class);
                    arrayContatos.add(contatos);
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
