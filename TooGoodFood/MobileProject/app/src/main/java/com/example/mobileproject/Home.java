//The Home Activity is the Most Important One. It manages all the fragments in the navigation bar
package com.example.mobileproject;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Home extends AppCompatActivity {


    private BottomNavigationView bottomNavigationView;
    private MenuFragment homeFragment = new MenuFragment();
    private ProfileFragment profileFragment = new ProfileFragment();
    private PendingOrderFragment pendingOrderFragment = new PendingOrderFragment();
    private CurrentOrderFragment currentOrderFragment = new CurrentOrderFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.user_bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,homeFragment).commit();


        //Switch to choose the right Fragment when a navigation bar's icon is clicked
        //THe R.id.name is the one used in bottom_navigation.xml  menu object
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

    //Function which allow the replacement of a fragment with another
    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.commit();
    }

}