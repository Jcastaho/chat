package com.straccion.chat.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.ToolbarWidgetWrapper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.straccion.chat.R;
import com.straccion.chat.adapters.MyPostsAdapter;
import com.straccion.chat.models.Post;
import com.straccion.chat.providers.AuthProvider;
import com.straccion.chat.providers.PostProvider;
import com.straccion.chat.providers.UserProvider;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {

    UserProvider mUserProvider;
    AuthProvider mAuthProvider;
    PostProvider mPostProvider;
    MyPostsAdapter mAdapter;


    RecyclerView mrecyclerMyPost;
    FloatingActionButton mFabChat;
    TextView mtxtPhone;
    TextView mtxtUsername;
    TextView mtxtEmail;
    ImageView mimageCover;
    TextView mtxtNumberPublicaciones;
    CircleImageView mImagePerfil;
    Toolbar mToolbar;
    TextView mtxtPostExist;
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
        mrecyclerMyPost = findViewById(R.id.recyclerMyPost);
        mtxtPostExist = findViewById(R.id.txtPostExist);
        mFabChat = findViewById(R.id.fabchat);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UserProfileActivity.this);
        mrecyclerMyPost.setLayoutManager(linearLayoutManager);



        mExtraIdUser = getIntent().getStringExtra("idUser");
        if (mAuthProvider.getUid().equals(mExtraIdUser)){
            mFabChat.setVisibility(View.GONE);
        }

        mFabChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToChatActivity();
            }
        });


        getUser();
        getPosNumber();
        checkIfExistPost();
    }

    private void goToChatActivity() {
        Intent intent = new Intent(UserProfileActivity.this, ChatActivity.class);
        intent.putExtra("idUser1", mAuthProvider.getUid());
        intent.putExtra("idUser2", mExtraIdUser);
        startActivity(intent);
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


    private void checkIfExistPost() {
        mPostProvider.getPostByUser(mExtraIdUser).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                int numberPost = value.size();
                if (numberPost > 0){
                    mtxtPostExist.setText("Publicaciones");
                    mtxtPostExist.setTextColor(Color.RED);
                }
                else {
                    mtxtPostExist.setText("No hay publicaciones");
                    mtxtPostExist.setTextColor(Color.GRAY);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Query query = mPostProvider.getPostByUser(mExtraIdUser);
        FirestoreRecyclerOptions<Post> options =
                new FirestoreRecyclerOptions.Builder<Post>().setQuery(query, Post.class).build();
        mAdapter = new MyPostsAdapter(options, UserProfileActivity.this);
        mrecyclerMyPost.setAdapter(mAdapter);
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }
}