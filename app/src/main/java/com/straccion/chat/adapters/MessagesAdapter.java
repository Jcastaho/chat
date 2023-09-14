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
import com.straccion.chat.activities.ChatActivity;
import com.straccion.chat.models.Message;
import com.straccion.chat.providers.AuthProvider;
import com.straccion.chat.providers.UserProvider;
import com.straccion.chat.utils.RelativeTime;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapter extends FirestoreRecyclerAdapter<Message, MessagesAdapter.ViewHolder> {

    Context contexto;
    UserProvider mUserProvider;
    AuthProvider mAuthProvider;

    public MessagesAdapter(FirestoreRecyclerOptions<Message> options, Context context){
        super(options);
        this.contexto = context;
        mUserProvider = new UserProvider();
        mAuthProvider = new AuthProvider();
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Message message) {

        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        String messageId = document.getId();
        holder.mtxtviewMessage.setText(message.getMessage());
        String relativeTime = RelativeTime.getTimeAgo(message.getTimestamp(), contexto);
        holder.mtxtDateMessage.setText(relativeTime);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_message, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView mtxtviewMessage;
        TextView mtxtDateMessage;
        ImageView mimageViewedMessage;
        View viewHolder;

        public ViewHolder(View view){
            super(view);
            mtxtviewMessage = view.findViewById(R.id.txtviewMessage);
            mtxtDateMessage = view.findViewById(R.id.txtDateMessage);
            mimageViewedMessage = view.findViewById(R.id.imageViewedMessage);
            viewHolder =view;
        }
    }
}
