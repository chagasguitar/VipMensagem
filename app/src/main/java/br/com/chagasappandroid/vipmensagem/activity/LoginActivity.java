package br.com.chagasappandroid.vipmensagem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.chagasappandroid.vipmensagem.R;
import br.com.chagasappandroid.vipmensagem.config.ConfiguracaoFirebase;
import br.com.chagasappandroid.vipmensagem.helper.Base64Custom;
import br.com.chagasappandroid.vipmensagem.helper.Preferencias;
import br.com.chagasappandroid.vipmensagem.model.Usuario;

public class LoginActivity extends AppCompatActivity {
    private EditText editText_senha;
    private EditText editText_email;
    private Button buttonlogar;
    private FirebaseAuth autenticacao;
    private Usuario usuario;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerUsuario;
    private String identificadorUsuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        vereficarUsuarioLogado();

        editText_senha = (EditText)findViewById(R.id.edit_text_usuario);
        editText_email   = (EditText)findViewById(R.id.edit_text_email);
        buttonlogar      = (Button)findViewById(R.id.bt_logar);

        buttonlogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailDigitado = editText_email.getText().toString();
                String senhaDigitada = editText_senha.getText().toString();
                if(emailDigitado.equals("")) {

                    Toast.makeText(LoginActivity.this, "Digite seu e-mail!", Toast.LENGTH_LONG).show();

                } else if(senhaDigitada.equals("")) {

                    Toast.makeText(LoginActivity.this, "Digite sua senha!", Toast.LENGTH_LONG).show();

                }else {
                    usuario = new Usuario();
                    usuario.setEmail(editText_email.getText().toString());
                    usuario.setSenha(editText_senha.getText().toString());
                    validarCadastro();
                }
            }
        });
    }

    private void vereficarUsuarioLogado() {
        autenticacao = ConfiguracaoFirebase.getFirebaseAuth();
        if (autenticacao.getCurrentUser()!= null){
            abrirTelaPrincipal();
        }
    }

    private void validarCadastro() {

        autenticacao = ConfiguracaoFirebase.getFirebaseAuth();

        Task<AuthResult> authResultTask = autenticacao.signInWithEmailAndPassword(usuario.getEmail(),
                usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                 if (task.isSuccessful()) {

                     identificadorUsuarioLogado = Base64Custom.codificarBase64(usuario.getEmail());

                     firebase = ConfiguracaoFirebase.getFirebase()
                             .child("usuario")
                             .child(identificadorUsuarioLogado);

                     valueEventListenerUsuario = new ValueEventListener() {
                         @Override
                         public void onDataChange(DataSnapshot dataSnapshot) {
                             Usuario usuarioRecuperado = dataSnapshot.getValue(Usuario.class);

                             Preferencias preferencias = new Preferencias(LoginActivity.this);

                             preferencias.salvarDados(identificadorUsuarioLogado,
                                     usuarioRecuperado.getNome());
                         }

                         @Override
                         public void onCancelled(DatabaseError databaseError) {

                         }
                     };

                     firebase.addListenerForSingleValueEvent(valueEventListenerUsuario);



                    abrirTelaPrincipal();
                    Toast.makeText(LoginActivity.this, "Sucesso do login", Toast.LENGTH_LONG).show();
                } else {

                    String erroExececao = "";

                    try {
                        throw task.getException();

                    } catch (FirebaseAuthUserCollisionException e) {
                        erroExececao = "Usuário já está logado";

                    } catch (FirebaseAuthInvalidCredentialsException e) {

                        erroExececao = "Digite e-mail e senha válido";

                    } catch (FirebaseAuthRecentLoginRequiredException e) {

                        erroExececao = "Usuário inválido";

                    } catch (Exception e) {
                        erroExececao = "Erro de cadastro";
                        e.printStackTrace();
                    }
                    Toast.makeText(LoginActivity.this,erroExececao,Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void abrirTelaPrincipal() {

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void telaCadastro(View view) {

        Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
        startActivity(intent);
    }
}
