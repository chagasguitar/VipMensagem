package br.com.chagasappandroid.vipmensagem.activity;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import br.com.chagasappandroid.vipmensagem.R;
import br.com.chagasappandroid.vipmensagem.config.ConfiguracaoFirebase;
import br.com.chagasappandroid.vipmensagem.helper.Base64Custom;
import br.com.chagasappandroid.vipmensagem.helper.Preferencias;
import br.com.chagasappandroid.vipmensagem.model.Usuario;

public class CadastroActivity extends AppCompatActivity {
    EditText editText_nome;
    EditText editText_email;
    EditText editText_senha;
    Button button_cadastrar;
    private Usuario usuario;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        editText_nome   = (EditText)findViewById(R.id.edit_cadastro_nome);
        editText_email  = (EditText)findViewById(R.id.edit_cadastro_email);
        editText_senha  = (EditText)findViewById(R.id.edit_cadastro_senha);
        button_cadastrar= (Button)findViewById(R.id.bt_cadastrar);

        button_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    usuario = new Usuario();
                    usuario.setNome(editText_nome.getText().toString());
                    usuario.setEmail(editText_email.getText().toString());
                    usuario.setSenha(editText_senha.getText().toString());
                    cadastarUsuario();

            }
        });


    }

    private void cadastarUsuario(){
        firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
        firebaseAuth.createUserWithEmailAndPassword(usuario.getEmail()
                ,usuario.getSenha()).addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    Toast.makeText(CadastroActivity.this,"Usuário Cadastrado", Toast.LENGTH_LONG).show();

                    FirebaseUser usuarioFB = task.getResult().getUser();

                    String identificador = Base64Custom.codificarBase64(usuarioFB.getEmail());

                    usuario.setId(identificador);
                    usuario.salvar();

                    Preferencias preferencias = new Preferencias(CadastroActivity.this);
                    preferencias.salvarDados(identificador,usuario.getNome());
                    abrirLogin();

                }else{

                    String erroExcecao = "";
                    try {
                        throw task.getException();

                    } catch (FirebaseAuthUserCollisionException e ){
                        erroExcecao = "Esse e-mail já está sendo utilizado";

                    } catch (FirebaseAuthInvalidCredentialsException e){
                        erroExcecao = "Digite um email válido";

                    } catch (FirebaseAuthRecentLoginRequiredException e) {

                        erroExcecao = "Usuário inválido";

                    } catch (FirebaseAuthException e) {
                        erroExcecao = "Digite uma senha mais segura, que contenha mais caracteres";

                    } catch (Exception e){
                        erroExcecao = "Erro de cadastro";
                        e.printStackTrace();

                    }
                    Toast.makeText(CadastroActivity.this, erroExcecao ,Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private void abrirLogin() {
        Intent intent = new Intent(CadastroActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
