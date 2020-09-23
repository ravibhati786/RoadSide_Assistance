package com.example.roadsideassistance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity  {

    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout drawerLayout;
    Fragment fragment = null;
    TextView name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
         setSupportActionBar(toolbar);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentLayout,new CustomerMapFragment());
        fragmentTransaction.commit();



        navigationView = findViewById(R.id.navigationview);
        navigationView.setItemIconTintList(null);
        drawerLayout = findViewById(R.id.drawer);


        updateNavHeader();
      //  View navview = getLayoutInflater().inflate(R.layout.nav_header,null);


        // name = navview.findViewById(R.id.customernamelogin);
        //name.setText("dngdfjhgjdfhgjhs");
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();



        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                   // case R.id.map:
                    //     drawerLayout.closeDrawer(GravityCompat.START);
                   //      break;
                    case R.id.navdocument:
                        fragment = new CustomerDocumentFragment();
                        loadfragement(fragment);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.navvehicle:
                        fragment = new CustomerVehicleFragment();
                        loadfragement(fragment);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.navservice:
                        fragment = new CustomerServiceFragment();
                        loadfragement(fragment);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.navlogout:
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(MainActivity.this,Login.class);
                        startActivity(intent);
                        finish();
                        break;
                }

                return true;
            }
        });


        }


        public void updateNavHeader(){

            navigationView = findViewById(R.id.navigationview);
            View headerView = navigationView.getHeaderView(0);
            TextView navusername = headerView.findViewById(R.id.customernamelogin);
            navusername.setText(new SharedPrefManager(this).getLoggedName());
            TextView navuseremail = headerView.findViewById(R.id.useremail);
            navuseremail.setText(new SharedPrefManager(this).getLoggedEmail());
        }


    public void loadfragement(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentLayout,fragment).addToBackStack(null).commit();
        drawerLayout.closeDrawer(GravityCompat.START);
        fragmentTransaction.addToBackStack(null);
    }



}