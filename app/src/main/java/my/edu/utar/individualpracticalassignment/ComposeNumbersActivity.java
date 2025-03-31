package my.edu.utar.individualpracticalassignment;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;




public class ComposeNumbersActivity extends BaseActivity {

    TextView txtTargetNumber, txtFeedback, txtLevel;
    TextView txtScore;
    Button btnMute, btnCheck, btnReset;
    LinearLayout blankContainer, numRow1, numRow2;

    int questionCount = 0;
    int correctAnswers = 0;
    int totalQuestions = 5;
    int currentLevel = 1;
    int min = 1;
    int max = 10;
    int targetNumber;
    int comboCount;

    MediaPlayer bgMusic;
    SoundPool soundPool;
    int soundCorrect, soundWrong, soundVictory;
    boolean isMuted = false;

    List<TextView> blankSlots = new ArrayList<>();
    List<Integer> numberPool = new ArrayList<>();
    List<Integer> selectedValues = new ArrayList<>();
    List<Button> numberButtons = new ArrayList<>();
    Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_numbers);
        setupBackButton(R.id.btnBack);

        txtLevel = findViewById(R.id.txtLevel);
        txtTargetNumber = findViewById(R.id.txtTargetNumber);
        txtFeedback = findViewById(R.id.txtFeedback);
        txtScore = findViewById(R.id.txtScore);
        btnMute = findViewById(R.id.btnMute);
        btnCheck = findViewById(R.id.btnCheck);
        btnReset = findViewById(R.id.btnReset);
        blankContainer = findViewById(R.id.blankContainer);
        numRow1 = findViewById(R.id.numRow1);
        numRow2 = findViewById(R.id.numRow2);

        currentLevel = getIntent().getIntExtra("level", 1);
        setLevelRange(currentLevel);

        bgMusic = MediaPlayer.create(this, R.raw.bg_music);
        bgMusic.setVolume(0.3f, 0.3f);
        bgMusic.setLooping(true);
        bgMusic.start();

        btnMute.setOnClickListener(v -> {
            if (isMuted) {
                bgMusic.start();
                btnMute.setText("🔇");
                btnMute.setBackgroundColor(Color.parseColor("#EF9A9A"));
            } else {
                bgMusic.pause();
                btnMute.setText("🔇");
                btnMute.setBackgroundColor(Color.parseColor("#A5D6A7"));
            }
            isMuted = !isMuted;
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder().setMaxStreams(5).setAudioAttributes(attributes).build();
        } else {
            soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }
        soundCorrect = soundPool.load(this, R.raw.correct_sound, 1);
        soundWrong = soundPool.load(this, R.raw.wrong_sound, 1);
        soundVictory = soundPool.load(this, R.raw.victory_sound, 1);

        btnCheck.setOnClickListener(v -> checkAnswer());
        btnReset.setOnClickListener(v -> resetSelections());

        generateQuestion();
    }

    private void setLevelRange(int level) {
        switch (level) {
            case 1:
            case 2:
            case 3:
                min = 1; max = 10; comboCount = 2;
                break;
            case 4:
                min = 1; max = 20; comboCount = 3;
                break;
            case 5:
                min = 1; max = 30; comboCount = 4;
                break;
        }

        String levelTitle = "Level " + level;
        switch (level) {
            case 1: levelTitle += " – Basic"; break;
            case 2: levelTitle += " – Easy"; break;
            case 3: levelTitle += " – Normal"; break;
            case 4: levelTitle += " – Hard"; break;
            case 5: levelTitle += " – Very Hard"; break;
        }
        txtLevel.setText(levelTitle);
    }

    private void generateQuestion() {
        blankSlots.clear();
        selectedValues.clear();
        blankContainer.removeAllViews();
        numRow1.removeAllViews();
        numRow2.removeAllViews();
        numberButtons.clear();

        List<Integer> combo;
        int tempSum;
        boolean valid = false;

        do {
            combo = new ArrayList<>();
            tempSum = 0;
            for (int i = 0; i < comboCount; i++) {
                int num = rand.nextInt(max - min + 1) + min;
                combo.add(num);
                tempSum += num;
            }

            switch (currentLevel) {
                case 1: valid = (tempSum >= 1 && tempSum <= 5); break;
                case 2: valid = (tempSum >= 6 && tempSum <= 10); break;
                case 3: valid = (tempSum >= 11 && tempSum <= 15); break;
                case 4: valid = (tempSum >= 16 && tempSum <= 20); break;
                case 5: valid = (tempSum >= 21 && tempSum <= 30); break;
            }

        } while (!valid);

        targetNumber = tempSum;
        txtTargetNumber.setText("Combine the Number: " + targetNumber);

        for (int i = 0; i < comboCount; i++) {
            TextView blank = new TextView(this);
            blank.setText("__");
            blank.setTextSize(24f);
            blank.setPadding(16, 8, 16, 8);
            blank.setTextColor(Color.BLACK);
            blankSlots.add(blank);
            blankContainer.addView(blank);
            if (i < comboCount - 1) {
                TextView plus = new TextView(this);
                plus.setText(" + ");
                plus.setTextSize(24f);
                blankContainer.addView(plus);
            }
        }

        numberPool.clear();
        numberPool.addAll(combo);
        while (numberPool.size() < 8) {
            int randNum = rand.nextInt(max - min + 1) + min;
            if (!numberPool.contains(randNum)) {
                numberPool.add(randNum);
            }
        }
        Collections.shuffle(numberPool);

        for (int i = 0; i < numberPool.size(); i++) {
            Button btn = new Button(this);
            int value = numberPool.get(i);
            btn.setText(String.valueOf(value));
            btn.setTextSize(18f);
            btn.setOnClickListener(v -> {
                for (TextView blank : blankSlots) {
                    if (blank.getText().toString().equals("__")) {
                        blank.setText(String.valueOf(value));
                        selectedValues.add(value);
                        btn.setEnabled(false);
                        btn.setBackgroundColor(Color.parseColor("#BDBDBD"));
                        break;
                    }
                }
            });

            numberButtons.add(btn);

            if (i < 4) {
                numRow1.addView(btn);
            } else {
                numRow2.addView(btn);
            }
        }
    }


    private void resetSelections() {
        selectedValues.clear();
        for (TextView blank : blankSlots) {
            blank.setText("__");
        }
        for (Button btn : numberButtons) {
            btn.setEnabled(true);
            btn.setBackgroundColor(Color.parseColor("#6200EE"));
        }
        txtFeedback.setText("");
    }

    private void checkAnswer() {
        if (selectedValues.size() != blankSlots.size()) {
            txtFeedback.setText("Please fill all blanks!");
            txtFeedback.setTextColor(Color.parseColor("#FFA000"));
            return;
        }

        int sum = 0;
        for (int value : selectedValues) sum += value;

        boolean correct = sum == targetNumber;
        soundPool.play(correct ? soundCorrect : soundWrong, 1, 1, 1, 0, 1);

        if (correct) {
            correctAnswers++;
            txtFeedback.setText("✅ Correct!");
            txtFeedback.setTextColor(Color.parseColor("#2E7D32"));
        } else {
            txtFeedback.setText("❌ Incorrect!");
            txtFeedback.setTextColor(Color.parseColor("#D32F2F"));
        }

        txtScore.setText("Score: " + correctAnswers);
        questionCount++;

        new Handler().postDelayed(() -> {
            if (questionCount < totalQuestions) {
                txtFeedback.setText("");
                generateQuestion();
            } else {
                showResultDialog();
            }
        }, 1500);
    }

    private void showResultDialog() {
        if (bgMusic != null && bgMusic.isPlaying()) {
            bgMusic.pause();
        }
        soundPool.play(soundVictory, 0.5f, 0.5f, 1, 0, 1);

        String message = (correctAnswers >= 3)
                ? "Well done! 🎉 You got " + correctAnswers + " out of " + totalQuestions
                : "Keep practicing! 😊 You got " + correctAnswers + " out of " + totalQuestions;

        new AlertDialog.Builder(this)
                .setTitle("Level Completed!")
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Play Again", (dialog, which) -> {
                    questionCount = 0;
                    correctAnswers = 0;
                    txtFeedback.setText("");
                    if (bgMusic != null && !isMuted) {
                        bgMusic.start();
                        bgMusic.setVolume(0.3f, 0.3f);
                    }
                    generateQuestion();
                })
                .setNegativeButton("Back", (dialog, which) -> finish())
                .show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (bgMusic != null && bgMusic.isPlaying()) bgMusic.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bgMusic != null && !isMuted) bgMusic.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bgMusic != null) {
            bgMusic.release();
            bgMusic = null;
        }
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }
}

