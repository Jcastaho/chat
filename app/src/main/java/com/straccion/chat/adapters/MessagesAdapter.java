package com.straccion.chat.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
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
        String relativeTime = RelativeTime.timeFormatAMPM(message.getTimestamp(), contexto);
        holder.mtxtDateMessage.setText(relativeTime);

        if (message.getIdsender().equals(mAuthProvider.getUid())){
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.setMargins(150, 0, 0, 0);
            holder.mlinearLayouthMessage.setLayoutParams(params);
            holder.mlinearLayouthMessage.setPadding(30,20,25,20);
            holder.mlinearLayouthMessage.setBackground(ResourcesCompat.getDrawable(contexto.getResources(), R.drawable.rounded_linear_layout, null));
            holder.mimageViewedMessage.setVisibility(View.VISIBLE);
            holder.mtxtviewMessage.setTextColor(Color.WHITE);
            holder.mtxtDateMessage.setTextColor(Color.LTGRAY);
        }else {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.setMargins(0, 0, 150, 0);
            holder.mlinearLayouthMessage.setLayoutParams(params);
            holder.mlinearLayouthMessage.setPadding(30,20,30,20);
            holder.mlinearLayouthMessage.setBackground(ResourcesCompat.getDrawable(contexto.getResources(), R.drawable.rounded_linear_layout_grey, null));
            holder.mimageViewedMessage.setVisibility(View.GONE);
            holder.mtxtviewMessage.setTextColor(Color.DKGRAY);
            holder.mtxtDateMessage.setTextColor(Color.LTGRAY);
        }
        if (message.isViewed()){
            holder.mimageViewedMessage.setImageResource(R.drawable.icon_check_blue_light);
        }else {
            holder.mimageViewedMessage.setImageResource(R.drawable.icon_check_grey);
        }
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
        LinearLayout mlinearLayouthMessage;
        View viewHolder;

        public ViewHolder(View view){
            super(view);
            mtxtviewMessage = view.findViewById(R.id.txtviewMessage);
            mtxtDateMessage = view.findViewById(R.id.txtDateMessage);
            mimageViewedMessage = view.findViewById(R.id.imageViewedMessage);
            mlinearLayouthMessage = view.findViewById(R.id.linearLayouthMessage);
            viewHolder =view;
        }
    }
}
