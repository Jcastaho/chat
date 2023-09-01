package com.straccion.chat.providers;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.straccion.chat.models.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserProvider {

    private CollectionReference mCollection;

    public UserProvider(){
        mCollection = FirebaseFirestore.getInstance().collection("Users");
    }

    public Task<DocumentSnapshot> getUser(String id){ return mCollection.document(id).get();}


    public Task<Void> create(User user){
        return mCollection.document(user.getId()).set(user);
    }


    public Task<Void> update(User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("nombreUsuario", user.getNombreUsuario());
        map.put("telefono", user.getTelefono());
        map.put("timestamp", new Date().getTime());
        map.put("imageProfile", user.getImageProfile());
        map.put("imageCover", user.getImageCover());
        return mCollection.document(user.getId()).update(map);
    }

}
