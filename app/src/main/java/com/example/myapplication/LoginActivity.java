package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnLogin, btnRegister;
    private FirebaseRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        repository = new FirebaseRepository();
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        if (repository.getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            
            if (email.isEmpty()) {
                etEmail.setError("Vui lòng nhập email");
                etEmail.requestFocus();
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.setError("Email không đúng định dạng");
                etEmail.requestFocus();
                return;
            }
            if (password.isEmpty()) {
                etPassword.setError("Vui lòng nhập mật khẩu");
                etPassword.requestFocus();
                return;
            }

            repository.login(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                } else {
                    String errorMsg = task.getException() != null ? task.getException().getMessage() : "Sai email hoặc mật khẩu";
                    if (errorMsg.contains("API key not valid")) {
                        Toast.makeText(this, "Lỗi hệ thống: API Key không hợp lệ. Vui lòng kiểm tra file google-services.json", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Đăng nhập thất bại: " + errorMsg, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });

        btnRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }
}
