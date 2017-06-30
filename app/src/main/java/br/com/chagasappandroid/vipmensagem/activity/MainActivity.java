package br.com.chagasappandroid.vipmensagem.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.chagasappandroid.vipmensagem.R;
import br.com.chagasappandroid.vipmensagem.adapter.TabAdapter;
import br.com.chagasappandroid.vipmensagem.config.ConfiguracaoFirebase;
import br.com.chagasappandroid.vipmensagem.helper.Base64Custom;
import br.com.chagasappandroid.vipmensagem.helper.Preferencias;
import br.com.chagasappandroid.vipmensagem.helper.SlidingTabLayout;
import br.com.chagasappandroid.vipmensagem.model.Contatos;
import br.com.chagasappandroid.vipmensagem.model.Usuario;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth firebaseauth;
    private String identificadorContato;
    private DatabaseReference firebase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseauth = ConfiguracaoFirebase.getFirebaseAuth();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle("Vip Mensagem");
        setSupportActionBar(toolbar);

        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.tab_layout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);


        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);
        slidingTabLayout.setDistributeEvenly(true);

        slidingTabLayout.setViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.item_sair:
                deslogarUsuario();
                return true;

            case R.id.action_configurações:
                return true;

            case R.id.item_adicionar:
                adicionarContato();
                return true;


            default: return super.onOptionsItemSelected(item);
        }
    }

    private void adicionarContato() {
        AlertDialog.Builder  alert = new AlertDialog.Builder(this);
        alert.setTitle("Novo Contato");
        alert.setMessage("E-mail do usuário");

        final EditText editText = new EditText(MainActivity.this);
        alert.setView(editText);


        alert.setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String emailContato = editText.getText().toString();

                if(emailContato.isEmpty()){
                    Toast.makeText(MainActivity.this,"Preencha o email",Toast.LENGTH_LONG).show();

                }else{

                    identificadorContato = Base64Custom.codificarBase64(emailContato);

                    firebase = ConfiguracaoFirebase.getFirebase().child("usuarios")
                                .child(identificadorContato);

                    firebase.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (dataSnapshot.getValue() != null){

                                Usuario usuarioContato = dataSnapshot.getValue(Usuario.class);

                                Preferencias preferencias = new Preferencias(MainActivity.this);
                                String identificadorUsuarioLogado = preferencias.getIdentificador();

                                firebase = ConfiguracaoFirebase.getFirebase()
                                        .child("contatos")
                                        .child(identificadorUsuarioLogado)
                                        .child(identificadorContato);

                                Contatos contatos = new Contatos();

                                contatos.setIdentificador(identificadorContato);
                                contatos.setEmail(usuarioContato.getEmail());
                                contatos.setNome(usuarioContato.getNome());

                                firebase.setValue(contatos);





                            }else{
                                Toast.makeText(MainActivity.this,"Usuário não possui cadastro",
                                        Toast.LENGTH_LONG).show();

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
            }
        });

        alert.setCancelable(false);
        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alert.create();
        alert.show();
    }

    private void deslogarUsuario() {
        firebaseauth.signOut();
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
