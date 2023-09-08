package com.straccion.chat.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;
import com.straccion.chat.R;
import com.straccion.chat.adapters.CommentAdapter;
import com.straccion.chat.adapters.PostsAdapter;
import com.straccion.chat.adapters.SliderAdapter;
import com.straccion.chat.models.Comments;
import com.straccion.chat.models.Post;
import com.straccion.chat.models.SliderItem;
import com.straccion.chat.providers.AuthProvider;
import com.straccion.chat.providers.LikesProvider;
import com.straccion.chat.providers.PostProvider;
import com.straccion.chat.providers.PostsComments;
import com.straccion.chat.providers.UserProvider;
import com.straccion.chat.utils.RelativeTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostDetailActivity extends AppCompatActivity {

    SliderView mSliderView;
    SliderAdapter mSliderAdapter;
    List<SliderItem> mSliderItem = new ArrayList<>();
    String mExtraPosId;
    PostProvider mPostProvider;
    UserProvider mUserProvider;
    PostsComments mCommentsProvider;
    AuthProvider mAuthProvider;
    CommentAdapter mAdapter;
    LikesProvider mLikesProvider;

    ImageView mImagenViewCategoria;
    CircleImageView mcircleImageDetail;
    Button mbtnVerPerfil;
    TextView mtxtNombreUsuarioDetail;
    TextView mtxtTelefonodetail;
    TextView mtxtNombreCategoria;
    TextView mtxtDescripcionDetail;
    TextView mtxtTituloJuego;
    Toolbar mToolbar;
    TextView mtxtViewRelativeTime;
    TextView mtxtnumLikes;

    RecyclerView mrecyclerViewComments;
    String idUser="";

    FloatingActionButton mfabComments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        mImagenViewCategoria = findViewById(R.id.ImagenViewCategoria);
        mcircleImageDetail = findViewById(R.id.circleImageDetail);
        mtxtNombreUsuarioDetail = findViewById(R.id.txtNombreUsuarioDetail);
        mtxtTelefonodetail = findViewById(R.id.txtTelefonodetail);
        mtxtNombreCategoria = findViewById(R.id.txtNombreCategoria);
        mtxtDescripcionDetail = findViewById(R.id.txtDescripcionDetail);
        mtxtTituloJuego = findViewById(R.id.txtTituloJuego);
        mbtnVerPerfil = findViewById(R.id.btnVerPerfil);
        mSliderView = findViewById(R.id.imageSlider);
        mfabComments = findViewById(R.id.fabComments);
        mtxtViewRelativeTime = findViewById(R.id.txtViewRelativeTime);

        mtxtnumLikes = findViewById(R.id.txtnumLikes);
        mrecyclerViewComments = findViewById(R.id.recyclerViewComments);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PostDetailActivity.this);
        mrecyclerViewComments.setLayoutManager(linearLayoutManager);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mfabComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogComment();
            }
        });

        mbtnVerPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToShowProfile();
            }
        });

        mAuthProvider = new AuthProvider();
        mCommentsProvider = new PostsComments();
        mPostProvider = new PostProvider();
        mUserProvider = new UserProvider();
        mLikesProvider = new LikesProvider();
        mExtraPosId = getIntent().getStringExtra("idUser");

        getPost();
        getNumberLikes();
    }

    private void getNumberLikes() {
        mLikesProvider.getLikesByPost(mExtraPosId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                int numberLikes = value.size();
                if (numberLikes == 1){
                    mtxtnumLikes.setText(numberLikes + " Me Gusta");
                }else {
                    mtxtnumLikes.setText(numberLikes + " Me Gustas");
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Query query = mCommentsProvider.getCommentsByPost(mExtraPosId);
        FirestoreRecyclerOptions<Comments> options =
                new FirestoreRecyclerOptions.Builder<Comments>().setQuery(query, Comments.class).build();
        mAdapter = new CommentAdapter(options, PostDetailActivity.this);
        mrecyclerViewComments.setAdapter(mAdapter);
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

    private void showDialogComment() {
        AlertDialog.Builder alert = new AlertDialog.Builder(PostDetailActivity.this);
        alert.setTitle("COMENTARIO");
        alert.setMessage("Ingresa tu comentario");
        EditText editText = new EditText(PostDetailActivity.this);
        editText.setHint("Texto");

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(40,0,80,40);
        editText.setLayoutParams(params);
        RelativeLayout container = new RelativeLayout(PostDetailActivity.this);
        RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        container.setLayoutParams(relativeParams);
        container.addView(editText);
        alert.setView(container);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String value = editText.getText().toString();
                if (!value.isEmpty()){
                    createCommet(value);
                }else {
                    Toast.makeText(PostDetailActivity.this, "Debe de ingresar el comentario", Toast.LENGTH_SHORT).show();
                }

            }
        });
        alert.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.show();
    }

    private void createCommet(String value) {
        Comments comments = new Comments();
        comments.setComment(value);
        comments.setIdPost(mExtraPosId);
        comments.setIdUser(mAuthProvider.getUid());
        comments.setTimestamp(new Date().getTime());
        mCommentsProvider.create(comments).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(PostDetailActivity.this, "El comentario se creo correctamente", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(PostDetailActivity.this, "No fue creado correctamente", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void goToShowProfile() {
        if (!idUser.equals("")){
            Intent intent = new Intent(PostDetailActivity.this, UserProfileActivity.class);
            intent.putExtra("idUser", idUser);
            startActivity(intent);
        }else {
            Toast.makeText(this, "Intente nuevamente", Toast.LENGTH_SHORT).show();
        }

    }

    private void instanceSlider(){
        mSliderAdapter = new SliderAdapter(PostDetailActivity.this, mSliderItem);
        mSliderView.setSliderAdapter(mSliderAdapter);
        mSliderView.setIndicatorAnimation(IndicatorAnimationType.THIN_WORM);//animacion del punto
        mSliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);//transiscion
        mSliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);//hacia que direccion va
        mSliderView.setIndicatorSelectedColor(Color.WHITE);//Color del punto
        mSliderView.setIndicatorUnselectedColor(Color.GRAY);//Color cuandono esta seleccionado
        mSliderView.setScrollTimeInSec(3);//tiempo
        mSliderView.setAutoCycle(true);//se le dice si quiere cambiar automaticamente
        mSliderView.startAutoCycle();
    }

    private void getPost(){
        mPostProvider.getPostById(mExtraPosId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    if (documentSnapshot.contains("imagen1")){
                        String imagen1 = documentSnapshot.getString("imagen1");
                        SliderItem item = new SliderItem();
                        item.setImageurl(imagen1);
                        mSliderItem.add(item);
                    }
                    if (documentSnapshot.contains("imagen2")){
                        String imagen1 = documentSnapshot.getString("imagen2");
                        SliderItem item = new SliderItem();
                        item.setImageurl(imagen1);
                        mSliderItem.add(item);
                   }
                    if (documentSnapshot.contains("titulo")){
                        String titulo = documentSnapshot.getString("titulo");
                        mtxtTituloJuego.setText(titulo.toUpperCase()); //toUpperCase sirve para todo mayusculas
                    }
                    if (documentSnapshot.contains("description")){
                        String description = documentSnapshot.getString("description");
                        mtxtDescripcionDetail.setText(description);
                    }
                    if (documentSnapshot.contains("categoria")){
                        String categoria = documentSnapshot.getString("categoria");
                        mtxtNombreCategoria.setText(categoria);
                        if (categoria.equals("PS4")){
                            mImagenViewCategoria.setImageResource(R.drawable.icon_ps4);
                        }
                        else if (categoria.equals("NINTENDO")){
                            mImagenViewCategoria.setImageResource(R.drawable.icon_nintendo);
                        }
                        else if (categoria.equals("PC")){
                            mImagenViewCategoria.setImageResource(R.drawable.icon_pc);
                        }
                        else if (categoria.equals("XBOX")){
                            mImagenViewCategoria.setImageResource(R.drawable.icon_xbox);
                        }
                    }
                    if (documentSnapshot.contains("idUser")){
                        idUser = documentSnapshot.getString("idUser");
                        getUserInfo(idUser);
                    }
                    if (documentSnapshot.contains("timestamp")){
                        long timestamp = documentSnapshot.getLong("timestamp");
                        String relativeTime = RelativeTime.getTimeAgo(timestamp, PostDetailActivity.this);
                        mtxtViewRelativeTime.setText(relativeTime);
                    }
//
                    instanceSlider();
                }
            }
        });
    }

    private void getUserInfo(String idUser) {
        mUserProvider.getUser(idUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    if (documentSnapshot.contains("nombreUsuario")){
                        String nombreUsuario = documentSnapshot.getString("nombreUsuario");
                        mtxtNombreUsuarioDetail.setText(nombreUsuario);
                    }
                    if (documentSnapshot.contains("telefono")){
                        String telefono = documentSnapshot.getString("telefono");
                        mtxtTelefonodetail.setText(telefono);
                    }
                    if (documentSnapshot.contains("imageProfile")){
                        String imageProfile = documentSnapshot.getString("imageProfile");
                        Picasso.get().load(imageProfile).into(mcircleImageDetail);
                    }
                }
            }
        });
    }
}