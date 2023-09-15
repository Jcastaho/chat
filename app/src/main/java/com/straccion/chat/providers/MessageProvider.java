package com.straccion.chat.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.straccion.chat.models.Message;

import java.util.HashMap;
import java.util.Map;

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
        return mCollection.whereEqualTo("idChat", idChat).orderBy("timestamp", Query.Direction.DESCENDING);
    }

    public Query getMessageByChatandsender (String idChat, String idsender){
        return mCollection.whereEqualTo("idChat", idChat).whereEqualTo("idsender", idsender).whereEqualTo("viewed", false);
    }

    public Task<Void> updateViewes (String idDocument, boolean state){
        Map<String, Object> map = new HashMap<>();
        map.put("viewed", state);
        return mCollection.document(idDocument).update(map);
    }

}
