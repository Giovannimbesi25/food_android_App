package com.example.mobileproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.example.mobileproject.databinding.ActivityHomeBinding;
import com.example.mobileproject.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;

public class Home extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    PendingOrderFragment pendingOrderFragment = new PendingOrderFragment();
    CurrentOrderFragment currentOrderFragment = new CurrentOrderFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.user_bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,homeFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.user_menu:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,homeFragment).commit();
                        break;
                    case R.id.user_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,profileFragment).commit();
                        break;
                    case R.id.user_current_order:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,currentOrderFragment).commit();
                        break;
                    case R.id.user_pending_order:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,pendingOrderFragment).commit();
                        break;
                }
                return true;
            }
        });

    }

    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.commit();
    }

}