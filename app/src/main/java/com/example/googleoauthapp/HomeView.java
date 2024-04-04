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


/*        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_home);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);*/

/*        binding.navView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Xử lý sự kiện ở đây
                int id = item.getItemId();
                if (id == R.id.navigation_dashboard) {
                    // Hiển thị fragment_dashboard
                    DashboardFragment dashboardFragment = new DashboardFragment();
                    // Sử dụng FragmentManager để thay thế Fragment hiện tại trong container
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, dashboardFragment)
                            .commit();
                    return true;
                } else if (id == R.id.navigation_notifications) {
                    // Xử lý cho item khác
                }
                return false;
            }
        });*/

/*
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            navController.navigate(item.getItemId());
            return true;
        });*/

    }
}