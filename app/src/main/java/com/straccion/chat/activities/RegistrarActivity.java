package com.straccion.chat.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.straccion.chat.R;
import com.straccion.chat.models.User;
import com.straccion.chat.providers.AuthProvider;
import com.straccion.chat.providers.UserProvider;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class RegistrarActivity extends AppCompatActivity {

    CircleImageView mcircleImageViewVolver;
    TextInputEditText mTextNombreUsuario;
    TextInputEditText mTextEmail;
    TextInputEditText mTextContraseña;
    TextInputEditText mTextConfirmcontra;
    Button mbtnRegistrarse;
    AuthProvider mAuthProvider;
    UserProvider mUsersProvider;
    TextInputEditText mTextInputPhone;
    AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        mcircleImageViewVolver = findViewById(R.id.circleVolver);
        mTextNombreUsuario = findViewById(R.id.txtNombreUsuario);
        mTextEmail = findViewById(R.id.txtCorreo);
        mTextContraseña = findViewById(R.id.txtContraseña);
        mTextConfirmcontra = findViewById(R.id.txtConfirmContra);
        mbtnRegistrarse = findViewById(R.id.btnRegistrarse);
        mTextInputPhone = findViewById(R.id.TextInputPhone);
        mDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento")
                .setCancelable(false).build();

        mAuthProvider = new AuthProvider();
        mUsersProvider = new UserProvider();

        mbtnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Registrar();
            }
        });

        mcircleImageViewVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void Registrar(){
        String Nombre = mTextNombreUsuario.getText().toString();
        String Correo = mTextEmail.getText().toString();
        String Contra = mTextContraseña.getText().toString();
        String ConfirmContra = mTextConfirmcontra.getText().toString();
        String Telefono = mTextInputPhone.getText().toString();

        if (!Nombre.isEmpty() && !Correo.isEmpty() && !Contra.isEmpty() && !ConfirmContra.isEmpty() && !Telefono.isEmpty()){
            if (isEmailValid(Correo)){
                if (Contra.equals(ConfirmContra)){
                    if (Contra.length() >= 6){
                        CreatreUser(Nombre,Correo, Contra, Telefono);
                    }else {
                        Toast.makeText(this, "Las Contraseñas deben de tener al menos 6 caracteres", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(this, "Las Contraseñas no coinciden", Toast.LENGTH_LONG).show();
                }
            }else {
                Toast.makeText(this, "El correo no es valido", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(this, "Ingresa todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    //Metodo para validar que si sea un correo valido ingresado
    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void CreatreUser(String NombreUsuario, String Correo, String Contraseña, String Telefono){
        mDialog.show();
        mAuthProvider.Registrar(Correo, Contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    String id = mAuthProvider.getUid();

                    User user = new User();
                    user.setId(id);
                    user.setCorreo(Correo);
                    user.setNombreUsuario(NombreUsuario);
                    user.setTelefono(Telefono);
                    user.setTimestamp(new Date().getTime());

                    mUsersProvider.create(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mDialog.dismiss();
                            if (task.isSuccessful()){
                                //los intent sirven para pasar a otra pantalla
                                Intent intent = new Intent(RegistrarActivity.this, HomeActivity.class);
                                startActivity(intent);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);//limpia pantallas de historial
                                Toast.makeText(RegistrarActivity.this, "El usuario se almaceno en la base de datos", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(RegistrarActivity.this, "El usuario NO se almaceno en la base de datos", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    mDialog.dismiss();
                    Toast.makeText(RegistrarActivity.this, "No se pudo registrar el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}