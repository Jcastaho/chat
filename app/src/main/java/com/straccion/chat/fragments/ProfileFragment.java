package com.straccion.chat.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.straccion.chat.R;
import com.straccion.chat.activities.EditarPerfilActivity;
import com.straccion.chat.adapters.MyPostsAdapter;
import com.straccion.chat.adapters.PostsAdapter;
import com.straccion.chat.models.Post;
import com.straccion.chat.providers.AuthProvider;
import com.straccion.chat.providers.PostProvider;
import com.straccion.chat.providers.UserProvider;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    View mView;
    UserProvider mUserProvider;
    AuthProvider mAuthProvider;
    PostProvider mPostProvider;
    RecyclerView mrecyclerMyPost;
    MyPostsAdapter mAdapter;



    LinearLayout mLinearLayoutEditProfile;
    TextView mtxtPhone;
    TextView mtxtUsername;
    TextView mtxtEmail;
    ImageView mimageCover;
    TextView mtxtNumberPublicaciones;
    CircleImageView mImagePerfil;





    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;




    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_profile, container, false);
        mLinearLayoutEditProfile = mView.findViewById(R.id.linearLayoutEditarPerfil);
        mtxtPhone = mView.findViewById(R.id.txtPhone);
        mtxtUsername = mView.findViewById(R.id.txtUsername);
        mtxtEmail = mView.findViewById(R.id.txtEmail);
        mimageCover = mView.findViewById(R.id.imageCover);
        mImagePerfil = mView.findViewById(R.id.ImagePerfil);
        mrecyclerMyPost = mView.findViewById(R.id.recyclerMyPost);
        mtxtNumberPublicaciones = mView.findViewById(R.id.txtNumberPublicaciones);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mrecyclerMyPost.setLayoutManager(linearLayoutManager);

        mLinearLayoutEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToEditProfile();
            }
        });

        mPostProvider = new PostProvider();
        mUserProvider = new UserProvider();
        mAuthProvider = new AuthProvider();
        getUser();
        getPosNumber();
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Query query = mPostProvider.getPostByUser(mAuthProvider.getUid());
        FirestoreRecyclerOptions<Post> options =
                new FirestoreRecyclerOptions.Builder<Post>().setQuery(query, Post.class).build();
        mAdapter = new MyPostsAdapter(options, getContext());
        mrecyclerMyPost.setAdapter(mAdapter);
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private void goToEditProfile() {
        Intent intent = new Intent(getContext(), EditarPerfilActivity.class);
        startActivity(intent);
    }

    private void getUser(){
        mUserProvider.getUser(mAuthProvider.getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    if (documentSnapshot.contains("correo")){
                        String Correo = documentSnapshot.getString("correo");
                        mtxtEmail.setText(Correo);
                    }
                    if (documentSnapshot.contains("telefono")){
                        String Tel = documentSnapshot.getString("telefono");
                        mtxtPhone.setText(Tel);
                    }
                    if (documentSnapshot.contains("nombreUsuario")){
                        String nombreUsuario = documentSnapshot.getString("nombreUsuario");
                        mtxtUsername.setText(nombreUsuario);
                    }
                    if (documentSnapshot.contains("imageProfile")){
                        String imageProfile = documentSnapshot.getString("imageProfile");
                        if (imageProfile != null){
                            if (!imageProfile.isEmpty()){
                                Picasso.get().load(imageProfile).into(mImagePerfil);
                            }
                        }
                    }
                    if (documentSnapshot.contains("imageCover")){
                        String imageCover = documentSnapshot.getString("imageCover");
                        if (imageCover != null){
                            if (!imageCover.isEmpty()){
                                Picasso.get().load(imageCover).into(mimageCover);
                            }
                        }
                    }
                }
            }
        });
    }

    private void getPosNumber(){
        mPostProvider.getPostByUser(mAuthProvider.getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int Numero = queryDocumentSnapshots.size();//cantidad de elementos de la consulta, documentos que existen segun ese id del usuario
                mtxtNumberPublicaciones.setText(String.valueOf(Numero));
            }
        });
    }
}