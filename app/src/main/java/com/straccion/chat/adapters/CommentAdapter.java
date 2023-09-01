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
import com.straccion.chat.models.Comments;
import com.straccion.chat.models.Post;
import com.straccion.chat.providers.UserProvider;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends FirestoreRecyclerAdapter<Comments, CommentAdapter.ViewHolder> {

    Context contexto;
    UserProvider mUserProvider;

    public CommentAdapter(FirestoreRecyclerOptions<Comments> options, Context context){
        super(options);
        this.contexto = context;
        mUserProvider = new UserProvider();
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Comments comments) {

        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        String commentId = document.getId();
        String idUser = document.getString("idUser");
        holder.mtxtComentarioUs.setText(comments.getComment());
        getUserInfo(idUser, holder);

    }

    private void getUserInfo(String idUser, ViewHolder holder){
        mUserProvider.getUser(idUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    if (documentSnapshot.contains("nombreUsuario")){
                        String nombreUsuario = documentSnapshot.getString("nombreUsuario");
                        holder.mtxtNomUsuarioComments.setText(nombreUsuario);
                    }
                    if (documentSnapshot.contains("imageProfile")){
                        String imageProfile = documentSnapshot.getString("imageProfile");
                        if (imageProfile != null){
                            if (!imageProfile.isEmpty()){
                                Picasso.get().load(imageProfile).into(holder.mCircleImageComment);
                            }
                        }
                    }
                }
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardviewcomment, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView mtxtNomUsuarioComments;
        TextView mtxtComentarioUs;
        CircleImageView mCircleImageComment;
        View ViewHolder;

        public ViewHolder(View view){
            super(view);
            mtxtNomUsuarioComments = view.findViewById(R.id.txtNomUsuarioComments);
            mtxtComentarioUs = view.findViewById(R.id.txtComentarioUs);
            mCircleImageComment = view.findViewById(R.id.CircleImageComment);
            ViewHolder =view;

        }

    }
}
