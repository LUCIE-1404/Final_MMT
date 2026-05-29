package com.final_project_mmt.hrm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressBar);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.GONE);

            // Gửi gói tin mạng yêu cầu đăng nhập lên Firebase Server
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // Gọi hàm lấy ID Token từ Firebase
                                user.getIdToken(true).addOnSuccessListener(tokenResult -> {
                                    String firebaseToken = tokenResult.getToken();

                                    // Lưu Token vào bộ nhớ tạm thời tập trung của hệ thống
                                    AuthTokenManager.TOKEN = firebaseToken;

                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(LoginActivity.this, "Xác thực quyền truy cập thành công!", Toast.LENGTH_SHORT).show();

                                    // Chuyển luồng màn hình sang danh sách nhân sự chính
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                });
                            }
                        } else {
                            progressBar.setVisibility(View.GONE);
                            btnLogin.setVisibility(View.VISIBLE);
                            Toast.makeText(LoginActivity.this, "Truy cập bị từ chối! Sai thông tin hoặc tài khoản chưa được phân quyền.", Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }
}