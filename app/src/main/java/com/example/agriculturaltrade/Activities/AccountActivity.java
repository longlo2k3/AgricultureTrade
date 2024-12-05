package com.example.agriculturaltrade.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agriculturaltrade.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AccountActivity extends AppCompatActivity {

    Toolbar toolbar, toolbar_activity;
    TextView show_my_products;

    EditText email, phoneNumber,useName;
    Spinner gender;
    Button update;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);


        //
        email = findViewById(R.id.email);
        useName = findViewById(R.id.username);
        phoneNumber = findViewById(R.id.phone);
        gender = findViewById(R.id.gender_spinner);
        update = findViewById(R.id.updateBtn);
        email.setText(mAuth.getCurrentUser().getEmail());
        show_my_products = findViewById(R.id.show_my_products);
        show_my_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, MyProductsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        toolbarShow();
        toolbarAction();


        infUserShow();
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, UpdateAccount.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void infUserShow() {
        if (mAuth.getCurrentUser() != null) {
            String userId = mAuth.getCurrentUser().getUid(); // Lấy UID người dùng
            System.out.println("UID người dùng hiện tại: " + userId);

            // Truy vấn Firestore để lấy thông tin người dùng
            db.collection("Users").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // Lấy dữ liệu từ Firestore
                            String getName = documentSnapshot.getString("username");
                            String getSdt = documentSnapshot.getString("phone_number");
                            String getEmail = documentSnapshot.getString("email");
                            String getGender = documentSnapshot.getString("gender");

                            // Hiển thị dữ liệu lên UI
                            useName.setText(getName != null ? getName : "Chưa có tên");
                            phoneNumber.setText(getSdt != null ? getSdt : "Chưa có số điện thoại");
                            email.setText(getEmail != null ?  getEmail: "Chưa có email");

                            // Xử lý giới tính
                            if ("Nam".equals(getGender)) {
                                gender.setSelection(0); // Nam
                            } else if ("Nữ".equals(getGender)){
                                gender.setSelection(1); // Nữ hoặc không xác định
                            }else {
                                gender.setSelection(2);
                            }
                        } else {
                            System.out.println("Dữ liệu người dùng không tồn tại trong Firestore.");
                        }
                    })
                    .addOnFailureListener(e -> {
                        System.out.println("Lỗi khi truy vấn Firestore: " + e.getMessage());
                    });
        } else {
            System.out.println("Người dùng chưa đăng nhập.");
        }
    }

    private void toolbarShow() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
}