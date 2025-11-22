package com.example.wordia;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private MaterialButton btnLogin, btnSignup, btnQuiz, btnLogout;
    private TextView tvRandomSentence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // XML 연결
        tvRandomSentence = findViewById(R.id.tvRandomSentence);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);
        btnQuiz = findViewById(R.id.btnQuiz);
        btnLogout = findViewById(R.id.btnLogout);

        // 로그인 여부 확인
        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

        // UI 업데이트
        updateUI(isLoggedIn);

        // 로그인 버튼
        btnLogin.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        });

        // 회원가입 버튼
        btnSignup.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, RegisterActivity.class));
        });

        // 단어 퀴즈 시작
        btnQuiz.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, QuizActivity.class));
        });

        // 로그아웃 버튼
        btnLogout.setOnClickListener(v -> {
            prefs.edit().putBoolean("isLoggedIn", false).apply();
            updateUI(false);
        });

        // 랜덤 문장 리스트
        List<String> englishSentences = Arrays.asList(
                "Keep going, you're doing great!",
                "Learning a new word boosts your mind!",
                "Small progress is still progress!"
        );

        // 랜덤 문장 적용
        tvRandomSentence.setText(
                englishSentences.get(new Random().nextInt(englishSentences.size()))
        );
    }

    // 🔥 로그인 여부에 따라 버튼 표시/숨김
    private void updateUI(boolean isLoggedIn) {
        if (isLoggedIn) {
            btnLogin.setVisibility(View.GONE);
            btnSignup.setVisibility(View.GONE);
            btnLogout.setVisibility(View.VISIBLE);
        } else {
            btnLogin.setVisibility(View.VISIBLE);
            btnSignup.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.GONE);
        }
    }

    // 화면 복귀마다 로그인 상태 갱신 (로그인 성공 직후 반영됨)
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        updateUI(prefs.getBoolean("isLoggedIn", false));
    }
}
