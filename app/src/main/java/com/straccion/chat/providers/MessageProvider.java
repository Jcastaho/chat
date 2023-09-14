package com.straccion.chat.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.straccion.chat.models.Message;

public class MessageProvider {
    CollectionReference mCollection;

    public MessageProvider(){
        mCollection = FirebaseFirestore.getInstance().collection("Messages");
    }
    public Task<Void> create (Message message){
        DocumentReference document = mCollection.document();
        message.setId(document.getId());
        return document.set(message);
    }

    public Query getMessageByChat (String idChat){
        return mCollection.whereEqualTo("idChat", idChat);
    }
}
