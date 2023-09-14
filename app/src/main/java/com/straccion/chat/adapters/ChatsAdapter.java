package com.straccion.chat.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;
import com.straccion.chat.R;
import com.straccion.chat.activities.ChatActivity;
import com.straccion.chat.models.Chat;
import com.straccion.chat.providers.AuthProvider;
import com.straccion.chat.providers.UserProvider;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsAdapter extends FirestoreRecyclerAdapter<Chat, ChatsAdapter.ViewHolder> {

    Context contexto;
    UserProvider mUserProvider;
    AuthProvider mAuthProvider;






    public ChatsAdapter(FirestoreRecyclerOptions<Chat> options, Context context){
        super(options);
        this.contexto = context;
        mUserProvider = new UserProvider();
        mAuthProvider = new AuthProvider();
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Chat chat) {

        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        final String ChatId = document.getId();
        if (mAuthProvider.getUid().equals(chat.getIdUser1())){
            getUserInfo(chat.getIdUser2(), holder);
        }else {
            getUserInfo(chat.getIdUser1(), holder);
        }
        holder.viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToChatActivity(ChatId, chat.getIdUser1(), chat.getIdUser2());
            }
        });
    }

    private void goToChatActivity(String chatId, String idUser1, String idUser2) {
        Intent intent = new Intent(contexto, ChatActivity.class);
        intent.putExtra("idChat", chatId);
        intent.putExtra("idUser1", idUser1);
        intent.putExtra("idUser2", idUser2);
        contexto.startActivity(intent);
    }

    private void getUserInfo(String idUser, ViewHolder holder){
        mUserProvider.getUser(idUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    if (documentSnapshot.contains("nombreUsuario")){
                        String nombreUsuario = documentSnapshot.getString("nombreUsuario");
                        holder.mtxtnombreUsChat.setText(nombreUsuario);
                    }
                    if (documentSnapshot.contains("imageProfile")){
                        String imageProfile = documentSnapshot.getString("imageProfile");
                        if (imageProfile != null){
                            if (!imageProfile.isEmpty()){
                                Picasso.get().load(imageProfile).into(holder.mcircleImageChat);
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_chats, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View viewHolder;
        TextView mtxtnombreUsChat;
        TextView mtxtLastMessageChat;
        CircleImageView mcircleImageChat;

        public ViewHolder(View view){
            super(view);
            mtxtnombreUsChat = view.findViewById(R.id.txtnombreUsChat);
            mtxtLastMessageChat = view.findViewById(R.id.txtLastMessageChat);
            mcircleImageChat = view.findViewById(R.id.circleImageChat);
            viewHolder =view;
        }
    }
}
