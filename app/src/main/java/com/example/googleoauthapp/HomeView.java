package com.example.googleoauthapp;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.googleoauthapp.ui.dashboard.DashboardFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.googleoauthapp.databinding.ActivityHomeBinding;
import com.google.android.material.navigation.NavigationBarView;

public class HomeView extends AppCompatActivity {

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_home);
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            navController.navigate(item.getItemId());
            return true;
        });
    }
}