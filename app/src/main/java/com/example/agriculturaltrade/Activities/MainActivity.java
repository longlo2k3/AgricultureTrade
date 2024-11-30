package com.example.agriculturaltrade.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.widget.Toolbar;


import com.example.agriculturaltrade.Authentication.Log_in;
import com.example.agriculturaltrade.Fragments.HomeFragment;
import com.example.agriculturaltrade.R;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {


    Fragment homeFragment;

    Toolbar toolbar,toolbar_activity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbarShow();
        toolbarAction();

        homeFragment = new HomeFragment();
        loadFragment(homeFragment);

    }

    private void toolbarAction() {
        toolbar_activity = findViewById(R.id.home_toolbar_activity);

        // Sự kiện khi nhấn vào các nút
        findViewById(R.id.action_home).setOnClickListener(v -> {
            // Chuyển hướng đến trang chủ
            startActivity(new Intent(this, MainActivity.class));
        });

        findViewById(R.id.action_product_details).setOnClickListener(v -> {
            // Chuyển hướng đến chi tiết sản phẩm
            startActivity(new Intent(this, ShowAllActivity.class));
        });

        findViewById(R.id.action_cart).setOnClickListener(v -> {
            // Chuyển hướng đến giỏ hàng
            startActivity(new Intent(this, CartActivity.class));
        });

        findViewById(R.id.action_account).setOnClickListener(v -> {
            // Chuyển hướng đến tài khoản
            startActivity(new Intent(this, AccountActivity.class));
        });
    }

    private void toolbarShow() {
        toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_menu_24);

    }

    private void loadFragment(Fragment homeFragment){
        // load fragment
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        // replace fragment
        fragmentTransaction.replace(R.id.home_container, homeFragment);
        // commit the transaction
        fragmentTransaction.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_add_product) {
            Intent intent = new Intent(MainActivity.this, AddProductActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MainActivity.this, Log_in.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.menu_my_cart) {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
            return true;
        }
        return true;
    }
}