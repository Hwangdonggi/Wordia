package com.example.wordia;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText etEmail, etPassword, etConfirmPassword;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 레이아웃 파일명은 아래 4)에서 제공하는 signup.xml을 사용합니다.
        setContentView(R.layout.signup);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String pass  = etPassword.getText().toString().trim();
            String confirm = etConfirmPassword.getText().toString().trim();

            if (!isValid(email, pass, confirm)) return;

            // 로컬 저장
            SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
            prefs.edit()
                    .putString("userEmail", email)
                    .putString("userPassword", pass)
                    .apply();

            Toast.makeText(this, "회원가입 완료!", Toast.LENGTH_SHORT).show();
            finish(); // 메인/로그인으로 돌아감
        });
    }

    private boolean isValid(String email, String pass, String confirm) {
        if (email.isEmpty()) {
            toast("이메일을 입력하세요."); return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            toast("유효한 이메일이 아닙니다."); return false;
        }
        if (pass.length() < 6) {
            toast("비밀번호는 최소 6자리 이상이어야 합니다."); return false;
        }
        if (!pass.equals(confirm)) {
            toast("비밀번호가 일치하지 않습니다."); return false;
        }
        return true;
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
