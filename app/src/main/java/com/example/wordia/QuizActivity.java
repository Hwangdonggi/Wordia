package com.example.wordia;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    private TextView tvTitle, tvScore, tvQuestion, tvExplanation;
    private RadioGroup rgOptions;
    private RadioButton opt1, opt2, opt3, opt4;
    private Button btnSubmit, btnNext, btnBackToMain;

    private static class QuizItem {
        String word;       // 정답 단어
        String sentence;   // "____"가 들어간 문장

        QuizItem(String word, String sentence) {
            this.word = word;
            this.sentence = sentence;
        }
    }

    // 단어/문장 뱅크
    private final List<QuizItem> bank = Arrays.asList(
            new QuizItem("achieve", "You can ____ your goals with consistent effort."),
            new QuizItem("benefit", "Regular exercise can ____ your health."),
            new QuizItem("consider", "Please ____ my suggestion carefully."),
            new QuizItem("improve", "Reading daily can ____ your vocabulary."),
            new QuizItem("create", "Developers ____ useful apps for people."),
            new QuizItem("reduce", "We should ____ waste to protect the environment."),
            new QuizItem("support", "Friends ____ each other in difficult times."),
            new QuizItem("choose", "You must ____ the best answer."),
            new QuizItem("explain", "Can you ____ how this works?"),
            new QuizItem("increase", "We hope to ____ the number of users this year.")
    );

    private static final int TOTAL_QUESTIONS = 10; // ✅ 10문제만 출제

    private List<QuizItem> questionPool; // 섞은 문제 목록
    private int currentIndex = 0;        // 현재 몇 번째 문제인지 (0 ~ 9)
    private int score = 0;               // 점수

    private QuizItem current;
    private final Random rnd = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz);

        tvTitle = findViewById(R.id.tvTitle);
        tvScore = findViewById(R.id.tvScore);
        tvQuestion = findViewById(R.id.tvQuestion);
        tvExplanation = findViewById(R.id.tvExplanation);

        rgOptions = findViewById(R.id.rgOptions);
        opt1 = findViewById(R.id.opt1);
        opt2 = findViewById(R.id.opt2);
        opt3 = findViewById(R.id.opt3);
        opt4 = findViewById(R.id.opt4);

        btnSubmit = findViewById(R.id.btnSubmit);
        btnNext = findViewById(R.id.btnNext);
        btnBackToMain = findViewById(R.id.btnBackToMain);

        // 문제 풀 섞기 (중복 없이 10문제)
        questionPool = new ArrayList<>(bank);
        Collections.shuffle(questionPool, rnd);

        // 혹시 bank가 10개보다 적은 경우 보호
        if (questionPool.size() > TOTAL_QUESTIONS) {
            questionPool = questionPool.subList(0, TOTAL_QUESTIONS);
        }

        // 첫 문제 로드
        loadNextQuestion();

        btnSubmit.setOnClickListener(v -> submitAnswer());

        btnNext.setOnClickListener(v -> loadNextQuestion());

        btnBackToMain.setOnClickListener(v -> {
            // 메인으로 돌아가기 (현재 퀴즈 액티비티 종료)
            finish();
        });
    }

    private void loadNextQuestion() {
        // 이미 10문제를 다 풀었으면 결과 화면
        if (currentIndex >= TOTAL_QUESTIONS || currentIndex >= questionPool.size()) {
            showFinalResult();
            return;
        }

        // 상태 초기화
        btnSubmit.setEnabled(true);
        btnNext.setEnabled(false);
        tvExplanation.setText("");
        rgOptions.setVisibility(View.VISIBLE);
        rgOptions.clearCheck();
        btnBackToMain.setVisibility(View.GONE);

        // 현재 문제 선택
        current = questionPool.get(currentIndex);
        currentIndex++;

        // 문제 텍스트 설정
        tvQuestion.setText(currentIndex + " / " + TOTAL_QUESTIONS + "  "
                + current.sentence.replace("____", "_____"));

        // 보기 생성 (정답 + 랜덤 오답 3개)
        List<String> options = new ArrayList<>();
        options.add(current.word);

        while (options.size() < 4) {
            QuizItem cand = bank.get(rnd.nextInt(bank.size()));
            if (!cand.word.equals(current.word) && !options.contains(cand.word)) {
                options.add(cand.word);
            }
        }
        Collections.shuffle(options, rnd);

        opt1.setText(options.get(0));
        opt2.setText(options.get(1));
        opt3.setText(options.get(2));
        opt4.setText(options.get(3));
    }

    private void submitAnswer() {
        int checkedId = rgOptions.getCheckedRadioButtonId();
        if (checkedId == -1) {
            Toast.makeText(this, "보기를 선택하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton picked = findViewById(checkedId);
        String chosen = picked.getText().toString();
        boolean correct = chosen.equals(current.word);

        if (correct) {
            score++;
            Toast.makeText(this, "정답!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "오답! 정답: " + current.word, Toast.LENGTH_SHORT).show();
        }

        tvScore.setText("Score: " + score);

        // 해설 문장
        tvExplanation.setText("✓ " + current.sentence.replace("____", current.word));

        // 다음 문제로 이동 가능
        btnSubmit.setEnabled(false);
        btnNext.setEnabled(true);

        // 만약 지금 문제가 마지막 문제였다면, NEXT 대신 결과 보여주기
        if (currentIndex >= TOTAL_QUESTIONS) {
            btnNext.setText("결과 보기");
        } else {
            btnNext.setText("다음 문제");
        }
    }

    private void showFinalResult() {
        // 보기/버튼 상태 조정
        rgOptions.setVisibility(View.GONE);
        btnSubmit.setEnabled(false);
        btnNext.setEnabled(false);
        btnNext.setVisibility(View.GONE);

        // 최종 결과 텍스트
        tvQuestion.setText("퀴즈 종료!");
        tvExplanation.setText("당신의 점수는 " + score + " / " + TOTAL_QUESTIONS + " 입니다.");

        // 메인으로 돌아가기 버튼 표시
        btnBackToMain.setVisibility(View.VISIBLE);
    }
}
