package com.example.agriculturaltrade.Authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agriculturaltrade.Activities.MainActivity;
import com.example.agriculturaltrade.Activities.OnBoardingActivity;
import com.example.agriculturaltrade.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Log_in extends AppCompatActivity {
    private Button loginButton;
    private EditText emailEditText, passwordEditText;
    private TextView registerEditText, textNotificationEmail, textNotificationPwd,textForgotPassword;
    View progressBar;
    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Kiểm tra nếu người dùng đã đăng nhập
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Nếu đã đăng nhập, chuyển đến MainActivity
            Intent intent = new Intent(Log_in.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        // Khởi tạo FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Khởi tạo view
        emailEditText = findViewById(R.id.inputEmail);
        passwordEditText = findViewById(R.id.inputPassword);
        loginButton = findViewById(R.id.buttonSignIn);
        registerEditText = findViewById(R.id.textCreateNewAccount);
        textNotificationEmail = findViewById(R.id.textNotificationEmail);
        textNotificationPwd = findViewById(R.id.textNotificationPwd);
        progressBar = findViewById(R.id.progressBar);
        textForgotPassword = findViewById(R.id.textForgotPassword);

        //action
        LoginUser();
        moveToRegisterActivity();
        textForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Log_in.this, ForgetPassword.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void LoginUser() {
        loading(false);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Kiểm tra nếu email hoặc password trống
                if (checkEmpty(email, "Please enter your email") || checkEmpty(password, "Please enter your password")) {
                    return; // Dừng lại nếu có trường nhập rỗng
                }
                // Kiểm tra email có hợp lệ hay không
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    showToast("Please enter a valid email");
                    return; // Dừng lại nếu email không hợp lệ
                }

                // Đăng nhập vào Firebase
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Log_in.this, task -> {
                            if (task.isSuccessful()) {
                                loading(true);
                                // Đăng nhập thành công, chuyển đến MainActivity
                                Intent intent = new Intent(Log_in.this, OnBoardingActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                loading(true);
                                // Lấy chi tiết lỗi từ Firebase và hiển thị thông báo phù hợp
                                String errorMessage = task.getException() != null ? task.getException().getMessage() : "Login failed due to unknown error";
                                showToast("Login failed: " + errorMessage);
                            }
                        });
            }
        });
    }

    private void loading(Boolean isloading){
        if (isloading){
            loginButton.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }else {
            progressBar.setVisibility(View.INVISIBLE);
            registerEditText.setVisibility(View.VISIBLE);
        }
    }

    public void moveToRegisterActivity() {
        registerEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Log_in.this, Register.class);
                startActivity(intent);
            }
        });
    }

    public boolean checkEmpty(String text, String mess) {
        if (TextUtils.isEmpty(text)) {
            showToast(mess);
            return true; // Dừng lại nếu có lỗi
        }
        return false;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
