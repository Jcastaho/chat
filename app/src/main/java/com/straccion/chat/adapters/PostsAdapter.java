package com.straccion.chat.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;


import com.straccion.chat.R;
import com.straccion.chat.activities.PostDetailActivity;
import com.straccion.chat.models.Like;
import com.straccion.chat.models.Post;
import com.straccion.chat.providers.AuthProvider;
import com.straccion.chat.providers.LikesProvider;
import com.straccion.chat.providers.PostProvider;
import com.straccion.chat.providers.UserProvider;

import java.util.Date;

public class PostsAdapter extends FirestoreRecyclerAdapter<Post,PostsAdapter.ViewHolder> {

    Context contexto;
    UserProvider mUserProvider;
    LikesProvider mLikesProvider;
    AuthProvider mAuthProvider;

    public PostsAdapter(FirestoreRecyclerOptions<Post> options, Context context){
        super(options);
        this.contexto = context;
        mUserProvider = new UserProvider();
        mLikesProvider = new LikesProvider();
        mAuthProvider = new AuthProvider();
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

        holder.mimageViewLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Like like = new Like();
                like.setIdUser(mAuthProvider.getUid());
                like.setIdPost(postId);
                like.setTimestamp(new Date().getTime());
                like(like, holder);
            }
        });


        getUserInfo(post.getIdUser(), holder);
        getNumberLikesByPost(postId, holder);
        checkifExistLike(postId, mAuthProvider.getUid(), holder);
    }

    private void getNumberLikesByPost(String idPost, ViewHolder holder){
        mLikesProvider.getLikesByPost(idPost).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                int numbreLikes = value.size();
                holder.mtxtLikes.setText(String.valueOf(numbreLikes) + " Me Gusta");
            }
        });
    }
    private void like(Like like, ViewHolder holder) {
        mLikesProvider.getLikeByPostAndUser(like.getIdPost(), mAuthProvider.getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int numberDocuments = queryDocumentSnapshots.size();
                if (numberDocuments > 0){
                    String idLike = queryDocumentSnapshots.getDocuments().get(0).getId();
                    holder.mimageViewLike.setImageResource(R.drawable.icon_like_gris);
                    mLikesProvider.delete(idLike);
                }else {
                    holder.mimageViewLike.setImageResource(R.drawable.icon_like_blue);
                    mLikesProvider.create(like);
                }
            }
        });
    }

    private void checkifExistLike(String idPost, String idUser, ViewHolder holder) {
        mLikesProvider.getLikeByPostAndUser(idPost, idUser).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int numberDocuments = queryDocumentSnapshots.size();
                if (numberDocuments > 0){
                    holder.mimageViewLike.setImageResource(R.drawable.icon_like_blue);
                }else {
                    holder.mimageViewLike.setImageResource(R.drawable.icon_like_gris);
                }
            }
        });
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
        TextView mtxtLikes;
        ImageView imageViewPost;
        ImageView mimageViewLike;
        View ViewHolder;

        public ViewHolder(View view){
            super(view);
            txtViewTitle = view.findViewById(R.id.txtViewTituloPostCard);
            txtViewDescription = view.findViewById(R.id.txtViewDescripcionPostCard);
            imageViewPost = view.findViewById(R.id.imageViewPostCard);
            mtxtUsernameCreador = view.findViewById(R.id.txtUsernameCreador);
            mtxtLikes = view.findViewById(R.id.txtLikes);
            mimageViewLike = view.findViewById(R.id.imageViewLike);
            ViewHolder =view;
        }

    }
}
