package br.com.chagasappandroid.vipmensagem.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

import br.com.chagasappandroid.vipmensagem.R;
import br.com.chagasappandroid.vipmensagem.adapter.MensagemAdapter;
import br.com.chagasappandroid.vipmensagem.config.ConfiguracaoFirebase;
import br.com.chagasappandroid.vipmensagem.helper.Base64Custom;
import br.com.chagasappandroid.vipmensagem.helper.Preferencias;
import br.com.chagasappandroid.vipmensagem.model.Conversa;
import br.com.chagasappandroid.vipmensagem.model.Mensagem;


public class ConversaActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private String nomeUsuarioDest;
    private EditText editMensagem;
    private ImageButton btEnvia;
    private ListView listview;
    private String idUserRemetente;
    private String nomeUsuarioRemetente;
    private String idUserDestinatario;
    private DatabaseReference firebase;
    private ArrayList<Mensagem> mensagens;
    private ArrayAdapter<Mensagem> adapter;
    private ValueEventListener valueEventListenerMensagem;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa);

        toolbar = (Toolbar)findViewById(R.id.tb_conversas);
        editMensagem = (EditText)findViewById(R.id.edit_mensagem);
        btEnvia      = (ImageButton)findViewById(R.id.bt_envia_mensagem);
        listview     = (ListView)findViewById(R.id.lv_conversas);

        Preferencias preferencias = new Preferencias(ConversaActivity.this);
        idUserRemetente = preferencias.getIdentificador();
        nomeUsuarioRemetente = preferencias.getINome();

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            nomeUsuarioDest = extras.getString("nome");
            String emailDestinatario = extras.getString("email");
            idUserDestinatario = Base64Custom.codificarBase64(emailDestinatario);
        }
        toolbar.setTitle(nomeUsuarioDest);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        mensagens = new ArrayList<>();
        adapter = new MensagemAdapter(ConversaActivity.this, mensagens);

        listview.setAdapter(adapter);

        firebase = ConfiguracaoFirebase.getFirebase().child("mensagens").child(idUserRemetente)
                                        .child(idUserDestinatario);
        valueEventListenerMensagem = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    mensagens.clear();
                for (DataSnapshot dados: dataSnapshot.getChildren()) {

                    Mensagem mensagem = dados.getValue(Mensagem.class);
                    mensagens.add(mensagem);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

            firebase.addValueEventListener(valueEventListenerMensagem);

        btEnvia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textoMensagem = editMensagem.getText().toString();

                if (textoMensagem.isEmpty()){

                    Toast.makeText(ConversaActivity.this,"Escreva uma mensagem",Toast.LENGTH_LONG).show();

                }else {

                    Mensagem mensagem = new Mensagem();
                    mensagem.setIdUsuario(idUserRemetente);
                    mensagem.setMensagem(textoMensagem);

                    //  Salva para usuario remetente
                    Boolean retornoMensagemRemetente = salvarMensagem(idUserRemetente,
                            idUserDestinatario, mensagem);

                    if (!retornoMensagemRemetente) {
                        Toast.makeText(ConversaActivity.this, "Mensagem não enviada",
                                Toast.LENGTH_LONG).show();

                    }else {

                        //  Salva para usuario destinatario
                        Boolean retornoMensagemDestinatario = salvarMensagem(idUserDestinatario,
                                idUserRemetente, mensagem);

                        if (!retornoMensagemDestinatario) {
                            Toast.makeText(ConversaActivity.this, "Mensagem não enviada",
                                    Toast.LENGTH_LONG).show();

                        }
                    }

                    //Salva conversa para rementente

                    Conversa conversa = new Conversa();
                    conversa.setIdUsuario(idUserDestinatario);
                    conversa.setNome(nomeUsuarioDest);
                    conversa.setMensagem(textoMensagem);

                    Boolean retornoConversaRemetente =  salvarConversa(idUserRemetente,
                            idUserDestinatario, conversa);

                        if (!retornoConversaRemetente){
                            Toast.makeText(ConversaActivity.this,"Conversa não salva",
                                    Toast.LENGTH_LONG).show();
                        }else {

                            //Salva conversa para destinatario
                            conversa = new Conversa();
                            conversa.setIdUsuario(idUserRemetente);
                            conversa.setNome(nomeUsuarioRemetente);
                            conversa.setMensagem(textoMensagem);
                            salvarConversa(idUserDestinatario, idUserRemetente, conversa);

                            editMensagem.setText("");
                        }
                }
            }
        });

    }

    private Boolean salvarMensagem(String idRemetente, String idDestinatario, Mensagem mensagem) {
        try {

            firebase = ConfiguracaoFirebase.getFirebase().child("mensagens");
            firebase.child(idRemetente).child(idDestinatario).push().setValue(mensagem);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private Boolean salvarConversa(String idUserRemetente, String idUserDestinatario, Conversa conversa){
        try {
            firebase = ConfiguracaoFirebase.getFirebase().child("conversas");
            firebase.child(idUserRemetente)
                    .child(idUserDestinatario)
                    .setValue(conversa);
            return true;

        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerMensagem);
    }
}
