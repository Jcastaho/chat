package com.straccion.chat.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.straccion.chat.models.Comments;

public class PostsComments {

    CollectionReference mCollection;

    public PostsComments(){
        mCollection = FirebaseFirestore.getInstance().collection("Comments");
    }

    public Task<Void> create(Comments comment){
        return mCollection.document().set(comment);
    }

    public Query getCommentsByPost(String idPost){ return mCollection.whereEqualTo("idPost", idPost); }

}
