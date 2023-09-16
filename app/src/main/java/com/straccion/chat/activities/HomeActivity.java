package com.straccion.chat.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.straccion.chat.R;
import com.straccion.chat.fragments.ChatsFragment;
import com.straccion.chat.fragments.FiltersFragment;
import com.straccion.chat.fragments.HomeFragment;
import com.straccion.chat.fragments.ProfileFragment;
import com.straccion.chat.providers.AuthProvider;
import com.straccion.chat.providers.TokenProvider;
import com.straccion.chat.providers.UserProvider;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;
    TokenProvider mTokenProvider;
    AuthProvider mAuthProvider;
    UserProvider mUserProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigation = findViewById(R.id.bottomNavigationView);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        mTokenProvider = new TokenProvider();
        mAuthProvider = new AuthProvider();
        mUserProvider = new UserProvider();
        openFragment(new HomeFragment());
        createToken();

    }

    @Override
    protected void onStart() {
        super.onStart();
        updateOnline(true);
    }

    private void updateOnline(boolean status) {
        mUserProvider.updateOnline(mAuthProvider.getUid(), status);
    }

    @Override
    protected void onStop() {
        super.onStop();
        updateOnline(false);
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            item -> {
                if (item.getItemId() == R.id.itemHome) {
                    openFragment(new HomeFragment());
                } else if (item.getItemId() == R.id.itemChat) {
                    openFragment(new ChatsFragment());
                } else if (item.getItemId() == R.id.ItemFiltros) {
                    openFragment(new FiltersFragment());
                } else if (item.getItemId() == R.id.itemPerfil) {
                    openFragment(new ProfileFragment());
                }
                return true;
            };

    private void createToken(){
        mTokenProvider.create(mAuthProvider.getUid());
    }



}