package com.straccion.chat.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.straccion.chat.R;
import com.straccion.chat.activities.MainActivity;
import com.straccion.chat.activities.PostActivity;
import com.straccion.chat.adapters.PostsAdapter;
import com.straccion.chat.models.Post;
import com.straccion.chat.providers.AuthProvider;
import com.straccion.chat.providers.PostProvider;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements MaterialSearchBar.OnSearchActionListener {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

    View mView;
    FloatingActionButton mFab;
    MaterialSearchBar msearchBar;
    AuthProvider mAuthProvider;
    RecyclerView mRecyclerViews;
    PostProvider mPostProvider;
    PostsAdapter mPostsAdapter;
    PostsAdapter mPostsAdapterSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        mFab = mView.findViewById(R.id.fab);
        msearchBar = mView.findViewById(R.id.searchBar);
        mRecyclerViews = mView.findViewById(R.id.recyclerViewHome);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerViews.setLayoutManager(linearLayoutManager);


        setHasOptionsMenu(true);
        mAuthProvider = new AuthProvider();
        mPostProvider = new PostProvider();

        msearchBar.setOnSearchActionListener(this);
        msearchBar.inflateMenu(R.menu.main_menu);
        msearchBar.getMenu().setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.itemLogout){
                    Logout();
                }
                return true;
            }
        });

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPost();
            }
        });
        return mView;
    }

    private void searchBytitle(String titulo){
        Query query = mPostProvider.getPostByTitle(titulo);
        FirestoreRecyclerOptions<Post> options =
                new FirestoreRecyclerOptions.Builder<Post>().setQuery(query, Post.class).build();
        mPostsAdapterSearch = new PostsAdapter(options, getContext());
        mPostsAdapterSearch.notifyDataSetChanged();
        mRecyclerViews.setAdapter(mPostsAdapterSearch);
        mPostsAdapterSearch.startListening();
    }
    private void getAllPost(){
        Query query = mPostProvider.getAll();
        FirestoreRecyclerOptions<Post> options =
                new FirestoreRecyclerOptions.Builder<Post>().setQuery(query, Post.class).build();
        mPostsAdapter = new PostsAdapter(options, getContext());
        mPostsAdapter.notifyDataSetChanged();
        mRecyclerViews.setAdapter(mPostsAdapter);
        mPostsAdapter.startListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        getAllPost();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPostsAdapter.stopListening();
        if (mPostsAdapterSearch != null){
            mPostsAdapterSearch.stopListening();
        }
    }

    private void goToPost() {
        Intent intent = new Intent(getContext(), PostActivity.class);
        startActivity(intent);
    }



    private void Logout() {
        mAuthProvider.Logout();
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {
        if (!enabled){
            getAllPost();
        }
    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
        searchBytitle(text.toString().toLowerCase());
    }

    @Override
    public void onButtonClicked(int buttonCode) {

    }
}