package br.com.chagasappandroid.vipmensagem.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfiguracaoFirebase {
    private static DatabaseReference reference;
    private static FirebaseAuth firebaseAuth;


    public static DatabaseReference getFirebase(){

        if (reference==null) {
            reference = FirebaseDatabase.getInstance().getReference();
        }

        return reference;
    }

    public static FirebaseAuth getFirebaseAuth(){

        if (firebaseAuth == null){
            firebaseAuth  = FirebaseAuth.getInstance();
        }
        return firebaseAuth;
    }
}

