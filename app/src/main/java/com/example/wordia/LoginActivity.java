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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnDoLogin;
    private TextView tvGoSignup;

    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnDoLogin = findViewById(R.id.btnDoLogin);
        tvGoSignup = findViewById(R.id.tvGoSignup);

        usersRef = FirebaseDatabase.getInstance().getReference("users");

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

        btnDoLogin.setEnabled(false);

        // ✅ Firebase에서 해당 이메일을 가진 유저 검색
        usersRef.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        btnDoLogin.setEnabled(true);

                        if (!snapshot.exists()) {
                            toast("가입된 계정이 없습니다. 먼저 회원가입을 해주세요.");
                            return;
                        }

                        // email은 unique라고 가정하고 첫 번째 값만 사용
                        User found = null;
                        for (DataSnapshot child : snapshot.getChildren()) {
                            found = child.getValue(User.class);
                            break;
                        }

                        if (found == null) {
                            toast("로그인 중 오류가 발생했습니다.");
                            return;
                        }

                        if (pass.equals(found.password)) {
                            toast("로그인 성공!");

                            // ✅ 로그인 상태 저장 (메인에서 로그아웃 버튼용)
                            SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
                            prefs.edit()
                                    .putBoolean("isLoggedIn", true)
                                    .putString("loginEmail", found.email)
                                    .putString("loginNickname", found.nickname)
                                    .apply();

                            finish(); // 메인으로 돌아감
                        } else {
                            toast("아이디 또는 비밀번호가 틀렸습니다.");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        btnDoLogin.setEnabled(true);
                        toast("DB 오류: " + error.getMessage());
                    }
                });
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
