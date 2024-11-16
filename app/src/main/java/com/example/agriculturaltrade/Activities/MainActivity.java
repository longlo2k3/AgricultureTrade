package com.example.agriculturaltrade.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.agriculturaltrade.Authentication.Log_in;
import com.example.agriculturaltrade.Fragments.HomeFragment;
import com.example.agriculturaltrade.R;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {


    Fragment homeFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        homeFragment = new HomeFragment();
        loadFragment(homeFragment);

    }

    private void loadFragment(Fragment homeFragment){
        // load fragment
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        // replace fragment
        fragmentTransaction.replace(R.id.home_container, homeFragment);
        // commit the transaction
        fragmentTransaction.commit();

    }
}