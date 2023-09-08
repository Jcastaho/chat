package com.straccion.chat.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.straccion.chat.R;
import com.straccion.chat.adapters.PostsAdapter;
import com.straccion.chat.models.Post;
import com.straccion.chat.providers.AuthProvider;
import com.straccion.chat.providers.PostProvider;

public class FiltersActivity extends AppCompatActivity {

    String mExtraCategory;
    AuthProvider mAuthProvider;
    RecyclerView mRecyclerViews;
    PostProvider mPostProvider;
    PostsAdapter mPostsAdapter;
    Toolbar mToolbar;
    TextView mtxtnumeroFiltros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        mExtraCategory = getIntent().getStringExtra("categoria");
        mRecyclerViews = findViewById(R.id.recyclerViewFilters);
        mtxtnumeroFiltros = findViewById(R.id.txtnumeroFiltros);
        mToolbar = findViewById(R.id.toolbars);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerViews.setLayoutManager(new GridLayoutManager(FiltersActivity.this, 2)); //muestra de a 2 por filtro.



        Toast.makeText(this, "La categoria que selecciono es: " + mExtraCategory, Toast.LENGTH_SHORT).show();

        mAuthProvider = new AuthProvider();
        mPostProvider = new PostProvider();
    }
    @Override
    public void onStart() {
        super.onStart();
        Query query = mPostProvider.getPostByCategoryAndTimestamp(mExtraCategory);
        FirestoreRecyclerOptions<Post> options =
                new FirestoreRecyclerOptions.Builder<Post>().setQuery(query, Post.class).build();
        mPostsAdapter = new PostsAdapter(options, FiltersActivity.this, mtxtnumeroFiltros);
        mRecyclerViews.setAdapter(mPostsAdapter);
        mPostsAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPostsAdapter.stopListening();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }
}