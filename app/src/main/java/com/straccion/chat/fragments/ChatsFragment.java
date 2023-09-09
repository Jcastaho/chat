package com.straccion.chat.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.straccion.chat.R;
import com.straccion.chat.adapters.ChatsAdapter;
import com.straccion.chat.adapters.PostsAdapter;
import com.straccion.chat.models.Chat;
import com.straccion.chat.models.Post;
import com.straccion.chat.providers.AuthProvider;
import com.straccion.chat.providers.ChatsProvider;

import org.checkerframework.checker.units.qual.A;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatsFragment extends Fragment {

    ChatsAdapter mAdapter;
    RecyclerView mRecyclerView;
    AuthProvider mAuthProvider;
    ChatsProvider mChatsProvider;
    View mview;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChatsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatsFragment newInstance(String param1, String param2) {
        ChatsFragment fragment = new ChatsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mview = inflater.inflate(R.layout.fragment_chats, container, false);

        mRecyclerView = mview.findViewById(R.id.recyclerViewChats);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mChatsProvider = new ChatsProvider();
        mAuthProvider = new AuthProvider();
        return mview;
    }

    @Override
    public void onStart() {
        super.onStart();
        Query query = mChatsProvider.getAll(mAuthProvider.getUid());
        FirestoreRecyclerOptions<Chat> options =
                new FirestoreRecyclerOptions.Builder<Chat>().setQuery(query, Chat.class).build();
        mAdapter = new ChatsAdapter(options, getContext());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}