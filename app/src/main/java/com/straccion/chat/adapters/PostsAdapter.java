package com.straccion.chat.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;


import com.straccion.chat.R;
import com.straccion.chat.activities.PostDetailActivity;
import com.straccion.chat.models.Post;
import com.straccion.chat.providers.PostProvider;
import com.straccion.chat.providers.UserProvider;

public class PostsAdapter extends FirestoreRecyclerAdapter<Post,PostsAdapter.ViewHolder> {

    Context contexto;
    UserProvider mUserProvider;

    public PostsAdapter(FirestoreRecyclerOptions<Post> options, Context context){
        super(options);
        this.contexto = context;
        mUserProvider = new UserProvider();
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Post post) {

        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        String postId = document.getId();

        holder.txtViewTitle.setText(post.getTitulo().toUpperCase());
        holder.txtViewDescription.setText(post.getDescription());
        if (post.getImagen1() != null){
            if (!post.getImagen1().isEmpty()){
                Picasso.get().load(post.getImagen1()).into(holder.imageViewPost);
            }
        }
        holder.ViewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(contexto, PostDetailActivity.class);
                intent.putExtra("idUser", postId );
                contexto.startActivity(intent);
            }
        });

        getUserInfo(post.getIdUser(), holder);
    }

    private void getUserInfo(String idUser, ViewHolder holder) {
        mUserProvider.getUser(idUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    if (documentSnapshot.contains("nombreUsuario")){
                        String nombreUsuario = documentSnapshot.getString("nombreUsuario");
                        holder.mtxtUsernameCreador.setText("By: " + nombreUsuario.toUpperCase());
                    }
                }
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_post, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtViewTitle;
        TextView txtViewDescription;
        TextView mtxtUsernameCreador;
        ImageView imageViewPost;
        View ViewHolder;

        public ViewHolder(View view){
            super(view);
            txtViewTitle = view.findViewById(R.id.txtViewTituloPostCard);
            txtViewDescription = view.findViewById(R.id.txtViewDescripcionPostCard);
            imageViewPost = view.findViewById(R.id.imageViewPostCard);
            mtxtUsernameCreador = view.findViewById(R.id.txtUsernameCreador);
            ViewHolder =view;
    

        }

    }
}
