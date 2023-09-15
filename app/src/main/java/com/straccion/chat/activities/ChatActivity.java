package com.straccion.chat.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.straccion.chat.R;
import com.straccion.chat.adapters.MessagesAdapter;
import com.straccion.chat.models.Chat;
import com.straccion.chat.models.Message;
import com.straccion.chat.providers.AuthProvider;
import com.straccion.chat.providers.ChatsProvider;
import com.straccion.chat.providers.MessageProvider;
import com.straccion.chat.providers.UserProvider;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    String mExtraIdUser1;
    String mExtraIdUser2;
    String mExtraIdChat;
    View mActionBarView;

    MessageProvider mMessageprovider;
    AuthProvider mAuthProvider;
    ChatsProvider mChatsProvider;
    UserProvider mUserProvider;
    MessagesAdapter mAdapter;

    EditText mtxtEditMessaje;
    ImageView mimageViewSendMessage;
    CircleImageView mcircleImageProfile;
    TextView mtxtnombreUs;
    TextView mtxtRelativeTime;
    ImageView mimageViewBack;
    RecyclerView mrecyclerViewMessage;

    LinearLayoutManager mLinearLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mChatsProvider = new ChatsProvider();
        mMessageprovider = new MessageProvider();
        mAuthProvider = new AuthProvider();
        mUserProvider = new UserProvider();

        mrecyclerViewMessage = findViewById(R.id.recyclerViewMessage);
        mimageViewSendMessage = findViewById(R.id.imageViewSendMessage);
        mtxtEditMessaje = findViewById(R.id.txtEditMessaje);

        mLinearLayoutManager = new LinearLayoutManager(ChatActivity.this);
        mLinearLayoutManager.setReverseLayout(true);
        mLinearLayoutManager.setStackFromEnd(true);
        mrecyclerViewMessage.setLayoutManager(mLinearLayoutManager);




        mExtraIdUser1 = getIntent().getStringExtra("idUser1");
        mExtraIdUser2 = getIntent().getStringExtra("idUser2");
        mExtraIdChat = getIntent().getStringExtra("idChat");

        showCustomToolbar(R.layout.custom_chat_toolbar);

        checkIfChatExist();

        mimageViewSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter != null){
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

    private void getMessageChat(){
        Query query = mMessageprovider.getMessageByChat(mExtraIdChat);
        FirestoreRecyclerOptions<Message> options =
                new FirestoreRecyclerOptions.Builder<Message>().setQuery(query, Message.class).build();
        mAdapter = new MessagesAdapter(options, ChatActivity.this);
        mrecyclerViewMessage.setAdapter(mAdapter);
        mAdapter.startListening();

        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                updateViewed();

                int numeroMensajes = mAdapter.getItemCount();
                int lastMessagePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();

                if (lastMessagePosition == -1 || lastMessagePosition == (positionStart - 1)) {
                    // Si ya estamos en la parte inferior, desplázate hacia el nuevo elemento
                    mrecyclerViewMessage.scrollToPosition(positionStart);
                }else {
                    mrecyclerViewMessage.scrollToPosition(positionStart);
                }
            }
        });

    }


    private void sendMessage() {
        String mensaje = mtxtEditMessaje.getText().toString();
        if (!mensaje.isEmpty()){
            Message message = new Message();
            if (mAuthProvider.getUid().equals(mExtraIdUser1)){
                message.setIdsender(mExtraIdUser1);
                message.setIdReceiver(mExtraIdUser2);
            }else {
                message.setIdsender(mExtraIdUser2);
                message.setIdReceiver(mExtraIdUser1);
            }
            message.setTimestamp(new Date().getTime());
            message.setMessage(mensaje);
            message.setIdChat(mExtraIdChat);
            message.setViewed(false);

            mMessageprovider.create(message).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        mtxtEditMessaje.setText("");
                    }else {
                        Toast.makeText(ChatActivity.this, "El mensaje no se pudo crear", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    private void showCustomToolbar(int resource) {
        Toolbar toolbar = findViewById(R.id.toolbars);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mActionBarView = inflater.inflate(resource, null);
        actionBar.setCustomView(mActionBarView);
        mcircleImageProfile = mActionBarView.findViewById(R.id.circleImageProfile);
        mtxtnombreUs = mActionBarView.findViewById(R.id.txtnombreUs);
        mtxtRelativeTime = mActionBarView.findViewById(R.id.txtRelativeTime);
        mimageViewBack = mActionBarView.findViewById(R.id.imageViewBack);
        mimageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getUserInfo();

    }
    private void getUserInfo() {
        String idUserInfo = "";
        if (mAuthProvider.getUid().equals(mExtraIdUser1)){
            idUserInfo = mExtraIdUser2;
        }else {
            idUserInfo = mExtraIdUser1;
        }
        mUserProvider.getUser(idUserInfo).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    if (documentSnapshot.contains("nombreUsuario")){
                        String nombreUsuario = documentSnapshot.getString("nombreUsuario");
                        mtxtnombreUs.setText(nombreUsuario);
                    }
                }
                if (documentSnapshot.exists()){
                    if (documentSnapshot.contains("imageProfile")){
                        String imageProfile = documentSnapshot.getString("imageProfile");
                        if (imageProfile != null){
                            if (!imageProfile.equals("")){
                                Picasso.get().load(imageProfile).into(mcircleImageProfile);
                            }
                        }
                    }
                }
            }
        });

    }




    private void checkIfChatExist(){
        mChatsProvider.getChatByUs1andUs2(mExtraIdUser1, mExtraIdUser2).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int size = queryDocumentSnapshots.size();
                if (size == 0){
                    createChat();
                }else {
                    mExtraIdChat = queryDocumentSnapshots.getDocuments().get(0).getId();
                    getMessageChat();
                    updateViewed();
                }
            }
        });
    }

    private void updateViewed() {
        String idSender = "";
        if (mAuthProvider.getUid().equals(mExtraIdUser1)){
            idSender = mExtraIdUser2;
        }else {
            idSender = mExtraIdUser1;
        }

        mMessageprovider.getMessageByChatandsender(mExtraIdChat, idSender).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot document : queryDocumentSnapshots.getDocuments()){
                    mMessageprovider.updateViewes(document.getId(), true);
                }
            }
        });
    }


    private void createChat() {
        Chat chat = new Chat();
        chat.setIdUser1(mExtraIdUser1);
        chat.setIdUser2(mExtraIdUser2);
        chat.setWriting(false);
        chat.setTimestamp(new Date().getTime());
        chat.setId(mExtraIdUser1 + mExtraIdUser2);
        ArrayList<String> ids = new ArrayList<>();
        ids.add(mExtraIdUser1);
        ids.add(mExtraIdUser2);
        chat.setIds(ids);
        mChatsProvider.create(chat);
        mExtraIdChat = chat.getId();
        getMessageChat();
    }
}