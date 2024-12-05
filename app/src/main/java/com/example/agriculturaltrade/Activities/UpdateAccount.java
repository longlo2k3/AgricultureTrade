package com.example.agriculturaltrade.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.agriculturaltrade.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UpdateAccount extends AppCompatActivity {
    Toolbar toolbar;

    EditText email, phoneNumber,useName;
    Spinner gender;
    Button update;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_account);

        toolbarShow();


        email = findViewById(R.id.email);
        useName = findViewById(R.id.username);
        phoneNumber = findViewById(R.id.phone);
        gender = findViewById(R.id.gender_spinner);
        update = findViewById(R.id.updateBtn);

        email.setText(mAuth.getCurrentUser().getEmail());

        // Sự kiện khi nân tích nút Update
        getDataFromFirestore();


        // Sự kiện khi nhấn vào nút Update
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });

    }

    private void toolbarShow() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void updateData() {
        // Tạo một đối tượng chứa thông tin người dùng
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("username", useName.getText().toString());
        userInfo.put("phone_number", phoneNumber.getText().toString()); // Nên dùng dấu gạch dưới thay vì khoảng trắng
        userInfo.put("gender", gender.getSelectedItem().toString());
        userInfo.put("email", email.getText().toString());

        // Lưu thông tin vào Firestore
        db.collection("Users").document(mAuth.getCurrentUser().getUid()).set(userInfo)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(UpdateAccount.this, "Thông tin người dùng được lưu thành công.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UpdateAccount.this, "Lỗi khi lưu thông tin: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void getDataFromFirestore() {
        if (mAuth.getCurrentUser() != null) {
            String userId = mAuth.getCurrentUser().getUid(); // Lấy UID người dùng

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
}