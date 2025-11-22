package com.example.wordia;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    private EditText etEmail, etPassword, etConfirmPassword, etNickname;
    private Button btnRegister;

    // Firebase Realtime Database users 참조
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);   // 회원가입 XML

        // 뷰 연결
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etNickname = findViewById(R.id.etNickname);   // 닉네임 입력칸
        btnRegister = findViewById(R.id.btnRegister);

        // Firebase DB 경로: /users
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        // 가입 버튼 클릭
        btnRegister.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();
            String confirm = etConfirmPassword.getText().toString().trim();
            String nickname = etNickname.getText().toString().trim();

            if (!isValid(email, pass, confirm, nickname)) return;

            // Firebase에 이메일 중복 확인 후 회원가입
            registerToFirebase(email, pass, nickname);
        });
    }

    // 입력 검증
    private boolean isValid(String email, String pass, String confirm, String nickname) {
        if (email.isEmpty()) {
            toast("이메일을 입력하세요.");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            toast("유효한 이메일이 아닙니다.");
            return false;
        }
        if (nickname.isEmpty()) {
            toast("닉네임을 입력하세요.");
            return false;
        }
        if (pass.length() < 6) {
            toast("비밀번호는 최소 6자리 이상이어야 합니다.");
            return false;
        }
        if (!pass.equals(confirm)) {
            toast("비밀번호가 일치하지 않습니다.");
            return false;
        }
        return true;
    }

    // Firebase에 회원정보 등록
    private void registerToFirebase(String email, String pass, String nickname) {
        btnRegister.setEnabled(false);

        // 1) 같은 이메일이 이미 있는지 확인
        usersRef.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // 이미 가입된 이메일
                            toast("이미 가입된 이메일입니다.");
                            btnRegister.setEnabled(true);
                        } else {
                            // 2) 새 유저 생성
                            String userId = usersRef.push().getKey();
                            if (userId == null) {
                                toast("회원가입 중 오류가 발생했습니다.");
                                btnRegister.setEnabled(true);
                                return;
                            }

                            User user = new User(email, pass, nickname);
                            usersRef.child(userId).setValue(user)
                                    .addOnCompleteListener(task -> {
                                        btnRegister.setEnabled(true);
                                        if (task.isSuccessful()) {
                                            toast("회원가입 완료!");
                                            finish();   // 로그인/메인으로 돌아가기
                                        } else {
                                            String msg = (task.getException() != null)
                                                    ? task.getException().getMessage()
                                                    : "알 수 없는 오류";
                                            toast("회원가입 실패: " + msg);
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        btnRegister.setEnabled(true);
                        toast("DB 오류: " + error.getMessage());
                    }
                });
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
