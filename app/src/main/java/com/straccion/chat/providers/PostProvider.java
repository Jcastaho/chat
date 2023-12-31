package com.straccion.chat.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.straccion.chat.models.Post;

public class PostProvider {

    CollectionReference mCollection;
    public PostProvider(){
        mCollection = FirebaseFirestore.getInstance().collection("Post");

    }
    public Query getPostByUser(String id){ return mCollection.whereEqualTo("idUser", id); }

    public Task<Void> save(Post post){
        return mCollection.document().set(post);
    }

    public Query getAll(){
        return mCollection.orderBy("timestamp", Query.Direction.DESCENDING);
    }

    public Task<DocumentSnapshot> getPostById(String id){
        return mCollection.document(id).get();
    }

    public Task<Void>delete (String id){
        return mCollection.document(id).delete();
    }

    public Query getPostByCategoryAndTimestamp(String category){
        return mCollection.whereEqualTo("categoria", category).orderBy("timestamp", Query.Direction.DESCENDING); }

    public Query getPostByTitle(String titulo){
        return mCollection.orderBy("titulo").startAt(titulo).endAt(titulo+'\uf8ff');
    }

}
