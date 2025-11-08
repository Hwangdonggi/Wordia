package com.example.wordia;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnDoLogin;
    private TextView tvGoSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login); // 위의 login.xml 사용

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnDoLogin = findViewById(R.id.btnDoLogin);
        tvGoSignup = findViewById(R.id.tvGoSignup);

        btnDoLogin.setOnClickListener(v -> tryLogin());
        tvGoSignup.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }

    private void tryLogin() {
        String email = etEmail.getText().toString().trim();
        String pass  = etPassword.getText().toString().trim();

        if (email.isEmpty()) { toast("이메일을 입력하세요."); return; }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) { toast("유효한 이메일이 아닙니다."); return; }
        if (pass.isEmpty()) { toast("비밀번호를 입력하세요."); return; }

        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        String savedEmail = prefs.getString("userEmail", "");
        String savedPass  = prefs.getString("userPassword", "");

        if (savedEmail.isEmpty()) {
            toast("가입된 계정이 없습니다. 먼저 회원가입을 해주세요.");
            return;
        }

        if (email.equals(savedEmail) && pass.equals(savedPass)) {
            toast("로그인 성공!");
            // TODO: 로그인 성공 후 홈/학습 화면으로 이동하고 싶다면 아래 주석 해제:
            // startActivity(new Intent(this, HomeActivity.class));
            finish(); // 현재 로그인 화면 닫고 돌아가기(메인으로)
        } else {
            toast("아이디 또는 비밀번호가 틀렸습니다.");
        }
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
