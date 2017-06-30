package br.com.chagasappandroid.vipmensagem.model;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import br.com.chagasappandroid.vipmensagem.config.ConfiguracaoFirebase;

public class Usuario {
    private String id;
    private String nome;
    private String email;
    private String senha;

    public Usuario() {
    }

    public void salvar(){

        DatabaseReference reference = ConfiguracaoFirebase.getFirebase();
        reference.child("usuarios").child(getId()).setValue(this);
    }
    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}