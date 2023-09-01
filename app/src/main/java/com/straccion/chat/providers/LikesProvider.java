package com.straccion.chat.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.straccion.chat.models.Like;

public class LikesProvider {

    CollectionReference mCollection;

    public LikesProvider(){
        mCollection = FirebaseFirestore.getInstance().collection("Likes");
    }

    public Task<Void> create (Like like){
        return mCollection.document().set(like);
    }
}
