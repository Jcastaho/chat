package com.straccion.chat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.straccion.chat.R;
import com.straccion.chat.providers.AuthProvider;
import com.straccion.chat.providers.PostProvider;
import com.straccion.chat.providers.UserProvider;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {

    UserProvider mUserProvider;
    AuthProvider mAuthProvider;
    PostProvider mPostProvider;

    TextView mtxtPhone;
    TextView mtxtUsername;
    TextView mtxtEmail;
    ImageView mimageCover;
    TextView mtxtNumberPublicaciones;
    CircleImageView mImagePerfil;
    CircleImageView mcircleBack;
    String mExtraIdUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mPostProvider = new PostProvider();
        mUserProvider = new UserProvider();
        mAuthProvider = new AuthProvider();

        mtxtPhone = findViewById(R.id.txtPhone);
        mtxtUsername = findViewById(R.id.txtUsername);
        mtxtEmail = findViewById(R.id.txtEmail);
        mimageCover = findViewById(R.id.imageCover);
        mImagePerfil = findViewById(R.id.ImagePerfil);
        mtxtNumberPublicaciones = findViewById(R.id.txtNumberPublicaciones);
        mcircleBack = findViewById(R.id.circleBack);
        mcircleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mExtraIdUser = getIntent().getStringExtra("idUser");
        getUser();
        getPosNumber();
    }

    private void getUser(){
        mUserProvider.getUser(mExtraIdUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    if (documentSnapshot.contains("correo")){
                        String Correo = documentSnapshot.getString("correo");
                        mtxtEmail.setText(Correo);
                    }
                    if (documentSnapshot.contains("telefono")){
                        String Tel = documentSnapshot.getString("telefono");
                        mtxtPhone.setText(Tel);
                    }
                    if (documentSnapshot.contains("nombreUsuario")){
                        String nombreUsuario = documentSnapshot.getString("nombreUsuario");
                        mtxtUsername.setText(nombreUsuario);
                    }
                    if (documentSnapshot.contains("imageProfile")){
                        String imageProfile = documentSnapshot.getString("imageProfile");
                        if (imageProfile != null){
                            if (!imageProfile.isEmpty()){
                                Picasso.get().load(imageProfile).into(mImagePerfil);
                            }
                        }
                    }
                    if (documentSnapshot.contains("imageCover")){
                        String imageCover = documentSnapshot.getString("imageCover");
                        if (imageCover != null){
                            if (!imageCover.isEmpty()){
                                Picasso.get().load(imageCover).into(mimageCover);
                            }
                        }
                    }
                }
            }
        });
    }

    private void getPosNumber(){
        mPostProvider.getPostByUser(mExtraIdUser).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int Numero = queryDocumentSnapshots.size();//cantidad de elementos de la consulta, documentos que existen segun ese id del usuario
                mtxtNumberPublicaciones.setText(String.valueOf(Numero));
            }
        });
    }
}