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
import com.straccion.chat.providers.UserProvider;
import com.straccion.chat.utils.RelativeTime;

import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyPostsAdapter extends FirestoreRecyclerAdapter<Post, MyPostsAdapter.ViewHolder> {

    Context contexto;
    UserProvider mUserProvider;
    LikesProvider mLikesProvider;
    AuthProvider mAuthProvider;

    public MyPostsAdapter(FirestoreRecyclerOptions<Post> options, Context context){
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
        String relativeTime = RelativeTime.getTimeAgo(post.getTimestamp(), contexto);
        holder.mtxtRelativeTimeMyPost.setText(relativeTime);

        holder.mtxtTituloMyPost.setText(post.getTitulo().toUpperCase());
        if (post.getImagen1() != null){
            if (!post.getImagen1().isEmpty()){
                Picasso.get().load(post.getImagen1()).into(holder.mcircleImageMyPost);
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

    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_my_post, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView mtxtTituloMyPost;
        TextView mtxtRelativeTimeMyPost;
        CircleImageView mcircleImageMyPost;
        ImageView imageViewDeletePost;
        View ViewHolder;

        public ViewHolder(View view){
            super(view);
            mtxtTituloMyPost = view.findViewById(R.id.txtTituloMyPost);
            mtxtRelativeTimeMyPost = view.findViewById(R.id.txtRelativeTimeMyPost);
            mcircleImageMyPost = view.findViewById(R.id.circleImageMyPost);
            imageViewDeletePost = view.findViewById(R.id.imageViewDeletePost);
            ViewHolder =view;
        }

    }
}
