package com.straccion.chat.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthProvider {

    private FirebaseAuth mAuth;
    public AuthProvider(){
        mAuth = FirebaseAuth.getInstance();
    }

    public Task<AuthResult> Login(String Correo, String Contra){
        return mAuth.signInWithEmailAndPassword(Correo, Contra);
    }

    public Task<AuthResult> Registrar(String Correo, String Contra){
        return mAuth.createUserWithEmailAndPassword(Correo, Contra);
    }

    public String getUid(){
        if (mAuth.getCurrentUser() != null){
            return  mAuth.getCurrentUser().getUid();
        }else {
            return null;
        }
    }

    public FirebaseUser getUserSesion(){
        if (mAuth.getCurrentUser() != null){
            return  mAuth.getCurrentUser();
        }else {
            return null;
        }
    }
    public void Logout(){
        if (mAuth != null){
            mAuth.signOut();
        }
    }
}
