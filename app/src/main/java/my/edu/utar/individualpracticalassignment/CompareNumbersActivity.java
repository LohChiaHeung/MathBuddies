package my.edu.utar.individualpracticalassignment;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;


public class CompareNumbersActivity extends AppCompatActivity {

    TextView txtProblem, txtFeedback, txtLevel;
    Button btnOption1, btnOption2, btnOption3;

    int questionCount = 0;
    int correctAnswers = 0;
    String correctAnswer;
    int totalQuestions = 5;
    TextView txtScore;
    Button btnBack;
    Random rand = new Random();

    int min = 1;
    int max = 10;

    boolean shuffleOptions = false; // default value

    MediaPlayer bgMusic;
    SoundPool soundPool;
    int soundCorrect, soundWrong, soundVictory;
    boolean isMuted = false;
    Button btnMute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        shuffleOptions = getIntent().getBooleanExtra("shuffle", false);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_numbers);

        txtLevel = findViewById(R.id.txtLevel);
        txtProblem = findViewById(R.id.txtProblem);
        txtFeedback = findViewById(R.id.txtFeedback);

        btnOption1 = findViewById(R.id.btnOption1);
        btnOption2 = findViewById(R.id.btnOption2);
        btnOption3 = findViewById(R.id.btnOption3);

        String levelName = getIntent().getStringExtra("levelName");
        TextView txtLevelName = findViewById(R.id.txtLevelName);
        txtLevelName.setText(levelName);

        // Get range from intent
        min = getIntent().getIntExtra("min", 1);
        max = getIntent().getIntExtra("max", 10);

        txtScore = findViewById(R.id.txtScore);
        btnBack = findViewById(R.id.btnBack);

        bgMusic = MediaPlayer.create(this, R.raw.bg_music);
        bgMusic.setVolume(0.3f, 0.3f);
        bgMusic.setLooping(true);
        bgMusic.start();

        btnMute = findViewById(R.id.btnMute);
        btnMute.setText("🔇");
        btnMute.setBackgroundColor(Color.parseColor("#EF9A9A"));

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, CompareLevelsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        btnMute.setOnClickListener(v -> {
            if (isMuted) {
                // Music was muted → now resume
                bgMusic.start();
                btnMute.setText("🔇");
                btnMute.setBackgroundColor(Color.parseColor("#EF9A9A")); // red
                isMuted = false;
            } else {
                // Music is playing → now mute it
                bgMusic.pause();
                btnMute.setText("🔇");
                btnMute.setBackgroundColor(Color.parseColor("#A5D6A7")); // green
                isMuted = true;
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setMaxStreams(5)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }

        // Load the sounds
        soundCorrect = soundPool.load(this, R.raw.correct_sound, 1);
        soundWrong = soundPool.load(this, R.raw.wrong_sound, 1);
        soundVictory = soundPool.load(this, R.raw.victory_sound, 1);

        View.OnClickListener listener = view -> {
            Button clicked = (Button) view;
            String selected = clicked.getText().toString();

            if (selected.equals(correctAnswer)) {
                correctAnswers++;
                txtFeedback.setText("✅ Correct!");
                txtFeedback.setTextColor(Color.parseColor("#2E7D32"));
                soundPool.play(soundCorrect, 1, 1, 1, 0, 1);
            } else {
                txtFeedback.setText("❌ Incorrect!");
                txtFeedback.setTextColor(Color.parseColor("#D32F2F"));
                soundPool.play(soundWrong, 1, 1, 1, 0, 1);
            }

            txtScore.setText("Score: " + correctAnswers);

            btnOption1.setEnabled(false);
            btnOption2.setEnabled(false);
            btnOption3.setEnabled(false);

            // Delay before next question
            txtFeedback.postDelayed(() -> {
                questionCount++;
                if (questionCount >= totalQuestions) {
                    showResultDialog();
                } else {
                    generateQuestion();
                    txtFeedback.setText("");
                }
            }, 1500);
        };

        btnOption1.setOnClickListener(listener);
        btnOption2.setOnClickListener(listener);
        btnOption3.setOnClickListener(listener);

        generateQuestion(); // Start first question
    }

    private void generateQuestion() {
        btnOption1.setEnabled(true);
        btnOption2.setEnabled(true);
        btnOption3.setEnabled(true);

        int num1 = rand.nextInt(max - min + 1) + min;
        int num2 = rand.nextInt(max - min + 1) + min;

        txtLevel.setText("Question " + (questionCount + 1) + " of " + totalQuestions);
        txtProblem.setText(num1 + " ___ " + num2);

        if (num1 > num2) {
            correctAnswer = ">";
        } else if (num1 < num2) {
            correctAnswer = "<";
        } else {
            correctAnswer = "=";
        }

        // Shuffle options
        String[] options = {"<", "=", ">"};
        if (shuffleOptions) {
            shuffleArray(options);
        }

        btnOption1.setText(options[0]);
        btnOption2.setText(options[1]);
        btnOption3.setText(options[2]);
    }

    private void shuffleArray(String[] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int index = rand.nextInt(i + 1);
            String temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

    private void showResultDialog() {
        //Stop the background music
        if (bgMusic != null && bgMusic.isPlaying()) {
            bgMusic.pause();
        }

        soundPool.play(soundVictory, 1, 1, 1, 0, 1);

        //Show different message for different results
        String resultMessage;
        if (correctAnswers >= 3) {
            resultMessage = "Congratulations! 🎉\nYou got " + correctAnswers + " out of " + totalQuestions + " correct!" + "\n Keep it up!";
        } else {
            resultMessage = "Good try! 😊\nYou got " + correctAnswers + " out of " + totalQuestions + " correct.\nKeep practicing!";
        }

        new Handler().postDelayed(() -> {
        new AlertDialog.Builder(this)
                .setTitle("Quiz Completed!")
                .setMessage(resultMessage)
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
                .setNegativeButton("Back to Home", (dialog, which) -> {
                    Intent intent = new Intent(this, CompareLevelsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    //Intent.FLAG_ACTIVITY_CLEAR_TOP - Clears the other activities above it.
                    //FLAG_ACTIVITY_SINGLE_TOP - Reuses the existing CompareLevelsActivity instead of creating a new one.
                    startActivity(intent);
                    finish();
                })
                .show();
        }, 2000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (bgMusic != null && bgMusic.isPlaying()) {
            bgMusic.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bgMusic != null && !bgMusic.isPlaying()) {
            bgMusic.start();
        }
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
