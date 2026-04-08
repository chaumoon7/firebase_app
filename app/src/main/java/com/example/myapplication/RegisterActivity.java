package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.models.User;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private EditText etName, etEmail, etPhone, etPassword, etConfirmPassword;
    private Button btnRegister;
    private TextView tvBackToLogin;
    private FirebaseRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        repository = new FirebaseRepository();
        
        etName = findViewById(R.id.etRegName);
        etEmail = findViewById(R.id.etRegEmail);
        etPhone = findViewById(R.id.etRegPhone);
        etPassword = findViewById(R.id.etRegPassword);
        etConfirmPassword = findViewById(R.id.etRegConfirmPassword);
        btnRegister = findViewById(R.id.btnDoRegister);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);

        btnRegister.setOnClickListener(v -> validateAndRegister());
        
        tvBackToLogin.setOnClickListener(v -> finish());
    }

    private void validateAndRegister() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (name.isEmpty()) {
            etName.setError("Vui lòng nhập họ tên");
            etName.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            etEmail.setError("Email không được để trống");
            etEmail.requestFocus();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email không đúng định dạng (VD: abc@gmail.com)");
            etEmail.requestFocus();
            return;
        }
        if (phone.isEmpty()) {
            etPhone.setError("Vui lòng nhập số điện thoại");
            etPhone.requestFocus();
            return;
        } else if (phone.length() < 10 || phone.length() > 11) {
            etPhone.setError("Số điện thoại phải từ 10-11 chữ số");
            etPhone.requestFocus();
            return;
        }
        if (password.length() < 6) {
            etPassword.setError("Mật khẩu phải từ 6 ký tự trở lên");
            etPassword.requestFocus();
            return;
        }
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Mật khẩu xác nhận không khớp");
            etConfirmPassword.requestFocus();
            return;
        }

        repository.register(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = repository.getCurrentUser();
                if (firebaseUser != null) {
                    User newUser = new User(firebaseUser.getUid(), name, email, phone, "USER");
                    repository.addUser(newUser);
                    Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, MainActivity.class));
                    finishAffinity();
                }
            } else {
                String errorMsg = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                if (errorMsg.contains("email address is already in use")) {
                    Toast.makeText(this, "Email này đã được sử dụng bởi tài khoản khác!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Lỗi đăng ký: " + errorMsg, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
