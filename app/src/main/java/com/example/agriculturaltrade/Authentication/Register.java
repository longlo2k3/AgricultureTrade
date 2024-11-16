package com.example.agriculturaltrade.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agriculturaltrade.Activities.MainActivity;
import com.example.agriculturaltrade.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class Register extends AppCompatActivity {
    private EditText emailEditText, passwordEditText, confirmPasswordEditText;
    private TextView loginEditText;
    private Button registerButton;
    private FirebaseAuth mAuth;
    private  View progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Khởi tạo FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Khởi tạo view
        emailEditText = findViewById(R.id.inputEmail);
        passwordEditText = findViewById(R.id.inputPassword);
        registerButton = findViewById(R.id.buttonSignUp);
        confirmPasswordEditText = findViewById(R.id.inputConfirmPassword);
        loginEditText = findViewById(R.id.textSignIn);
        progressBar = findViewById(R.id.progressBar);

        //action
        registerUser();
        moveToLoginActivity();
    }

    private void moveToLoginActivity() {
        loginEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Log_in.class);
                startActivity(intent);
            }
        });
    }

    private void registerUser() {
        loading(false);
        // Lấy dữ liệu
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();

                // Kiểm tra trường nhập rỗng
                if (checkEmpty(email, "Please enter your email") ||
                        checkEmpty(password, "Please enter your password") ||
                        checkEmpty(confirmPassword, "Please confirm your password")) {
                    return; // Dừng lại nếu có trường nhập rỗng
                }

                // Kiểm tra mật khẩu và xác nhận mật khẩu
                if (checkPassword(password, confirmPassword)) {
                    return; // Dừng lại nếu mật khẩu không hợp lệ
                }

                // Nếu không có lỗi, tiếp tục tạo tài khoản
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    showToast("Sign up successful");
                                    loading(true);
                                    Intent intent = new Intent(Register.this, MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    loading(true);
                                    Exception exception = task.getException();
                                    if (exception instanceof FirebaseAuthException) {
                                        FirebaseAuthException firebaseEx = (FirebaseAuthException) exception;
                                        String errorMessage = firebaseEx.getMessage();
                                        showToast("Sign up failed: " + errorMessage);
                                    } else {
                                        showToast("Error: " + exception.getMessage());
                                    }
                                }
                            }
                        });
            }
        });
    }

    // Kiểm tra nếu trường nhập rỗng
    private boolean checkEmpty(String text, String message) {
        if (TextUtils.isEmpty(text)){
            showToast(message);
            return true; // Dừng lại nếu có lỗi
        }
        return false;
    }

    private void loading(Boolean isloading){
        if (isloading){
            registerButton.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }else {
            progressBar.setVisibility(View.INVISIBLE);
            loginEditText.setVisibility(View.VISIBLE);
        }
    }

    // Kiểm tra mật khẩu và xác nhận mật khẩu
    private boolean checkPassword(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)){
            showToast("Passwords do not match");
            return true; // Dừng lại nếu mật khẩu không khớp
        }
        if (password.length() < 6){
            showToast("Please enter a password of at least 6 characters");
            return true; // Dừng lại nếu mật khẩu quá ngắn
        }
        return false;
    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
