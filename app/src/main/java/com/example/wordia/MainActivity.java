package com.example.wordia;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView tvRandomSentence;
    private MaterialButton btnLogin, btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvRandomSentence = findViewById(R.id.tvRandomSentence);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);

        // 랜덤 문장
        List<String> englishSentences = Arrays.asList(
                "Keep going, you're doing great!",
                "Learning a new word boosts your mind.",
                "Small progress is still progress.",
                "Practice makes perfect!",
                "Every day is a chance to learn.",
                "Consistency beats intensity.",
                "Challenge yourself today!",
                "A new word can change your world.",
                "Believe in your learning journey.",
                "The best way to learn is to try."
        );
        tvRandomSentence.setText(englishSentences.get(new Random().nextInt(englishSentences.size())));

        btnLogin.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, LoginActivity.class)));

        btnSignup.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, RegisterActivity.class)));
    }
}
