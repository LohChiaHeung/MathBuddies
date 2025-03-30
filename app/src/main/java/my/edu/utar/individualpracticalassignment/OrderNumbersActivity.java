package my.edu.utar.individualpracticalassignment;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class OrderNumbersActivity extends BaseActivity {
    TextView txtNum1, txtNum2, txtNum3, txtNum4;
    TextView drop1, drop2, drop3, drop4;
    Button btnCheck;
    List<Integer> originalNumbers = new ArrayList<>();
    List<TextView> dropTargets = new ArrayList<>();
    boolean isAscending = true; // You can toggle this based on level

    int questionCount = 0;
    int correctAnswers = 0;
    int totalQuestions = 5;
    int currentLevel = 1;
    TextView txtScore;

    MediaPlayer bgMusic;
    SoundPool soundPool;
    int soundCorrect, soundWrong, soundVictory;
    boolean isMuted = false;
    Button btnMute;

    LinearLayout dragContainer, dropContainer;
    List<TextView> dragViews = new ArrayList<>();
    List<TextView> dropViews = new ArrayList<>();

    TextView txtFeedback;
    TextView txtLevel, txtOrderType, txtQuestionCount;
    View.OnDragListener dragListener = (v, event) -> {
        switch (event.getAction()) {
            case DragEvent.ACTION_DROP:
                TextView drop = (TextView) v;
                ClipData.Item item = event.getClipData().getItemAt(0);
                String draggedValue = item.getText().toString();

                // Prevent overwriting an already-filled drop box
                if (!drop.getText().toString().trim().isEmpty()) {
                    Toast.makeText(this, "Already filled!", Toast.LENGTH_SHORT).show();
                    return true;
                }

                drop.setText(draggedValue);
                drop.setBackgroundColor(Color.parseColor("#90CAF9")); // light blue = filled

                // Find and gray out the original drag source
                for (TextView dragView : dragViews) {
                    if (dragView.getText().toString().equals(draggedValue) && dragView.getTag().equals("unused")) {
                        dragView.setBackgroundColor(Color.parseColor("#BDBDBD")); // gray = used
                        dragView.setTag("used");
                        break;
                    }
                }

                return true;
        }
        return true;
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_numbers);


        txtLevel = findViewById(R.id.txtLevel);
        txtOrderType = findViewById(R.id.txtOrderType);
        txtQuestionCount = findViewById(R.id.txtQuestionCount);

        currentLevel = getIntent().getIntExtra("level", 1);
        // Initialize views
        dragContainer = findViewById(R.id.dragContainer);
        dropContainer = findViewById(R.id.dropContainer);
        btnCheck = findViewById(R.id.btnCheck);
        txtScore = findViewById(R.id.txtScore);

        dropTargets = Arrays.asList(drop1, drop2, drop3, drop4);


        btnCheck.setOnClickListener(v -> checkAnswer());
        setupBackButton(R.id.btnBack);
        txtFeedback = findViewById(R.id.txtFeedback);

        generateRandomNumbers(currentLevel);
        // Background Music
        bgMusic = MediaPlayer.create(this, R.raw.bg_music);
        bgMusic.setLooping(true);
        bgMusic.setVolume(0.3f, 0.3f);
        bgMusic.start();

        btnMute = findViewById(R.id.btnMute);
        btnMute.setText("🔇");
        btnMute.setBackgroundColor(Color.parseColor("#EF9A9A"));

        btnMute.setOnClickListener(v -> {
            if (isMuted) {
                bgMusic.start();
                btnMute.setText("🔇");
                btnMute.setBackgroundColor(Color.parseColor("#EF9A9A")); // Red
                isMuted = false;
            } else {
                bgMusic.pause();
                btnMute.setText("🔇");
                btnMute.setBackgroundColor(Color.parseColor("#A5D6A7")); // Green
                isMuted = true;
            }
        });

        // SoundPool for effects
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

        // Load sound effects
        soundCorrect = soundPool.load(this, R.raw.correct_sound, 1);
        soundWrong = soundPool.load(this, R.raw.wrong_sound, 1);
        soundVictory = soundPool.load(this, R.raw.victory_sound, 1);

        // Set level name based on currentLevel
        String levelTitle = "";
        switch (currentLevel) {
            case 1: levelTitle = "Level 1 – Basic"; break;
            case 2: levelTitle = "Level 2 – Easy"; break;
            case 3: levelTitle = "Level 3 – Normal"; break;
            case 4: levelTitle = "Level 4 – Hard"; break;
            case 5: levelTitle = "Level 5 – Very Hard"; break;
        }
        txtLevel.setText(levelTitle);



    }

    private void generateRandomNumbers(int level) {
        originalNumbers.clear();
        dragContainer.removeAllViews();
        dropContainer.removeAllViews();
        dragViews.clear();
        dropViews.clear();
        txtFeedback.setText("");

        txtQuestionCount.setText("Question " + (questionCount + 1) + " of " + totalQuestions);


        // Randomly choose Ascending or Descending
        Random rand = new Random();
        isAscending = rand.nextBoolean(); // true = ascending, false = descending
        txtOrderType.setText(isAscending ? "Ascending Order" : "Descending Order");

        // Determine how many boxes to use
        int boxCount = 4;
        switch (level) {
            case 1: boxCount = 3; break;
            case 2: boxCount = 4; break;
            default: boxCount = 5;
        }

        // Determine range
        int min = 1, max = 10;
        switch (level) {
            case 1: min = 1; max = 10; break;
            case 2: min = 11; max = 20; break;
            case 3: min = 21; max = 30; break;
            case 4: min = 31; max = 50; break;
            case 5: min = 51; max = 100; break;
        }

        // Generate unique random numbers
        while (originalNumbers.size() < boxCount) {
            int num = rand.nextInt(max - min + 1) + min;
            if (!originalNumbers.contains(num)) {
                originalNumbers.add(num);
            }
        }

        // Shuffle for draggable views
        List<Integer> shuffled = new ArrayList<>(originalNumbers);
        Collections.shuffle(shuffled);

        for (int i = 0; i < boxCount; i++) {
            // Create drag view
            TextView drag = new TextView(this);
            drag.setTag("unused");
            drag.setText(String.valueOf(shuffled.get(i)));
            drag.setTextSize(18f);
            drag.setGravity(Gravity.CENTER);
            drag.setBackgroundColor(Color.parseColor("#4CAF50"));
            drag.setTextColor(Color.WHITE);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, 130, 1f);
            params.setMargins(8, 8, 8, 8);
            drag.setLayoutParams(params);

            drag.setOnLongClickListener(v -> {
                ClipData data = ClipData.newPlainText("value", ((TextView) v).getText());
                v.startDragAndDrop(data, new View.DragShadowBuilder(v), v, 0);
                return true;
            });

            dragContainer.addView(drag);
            dragViews.add(drag);

            // Create drop view
            TextView drop = new TextView(this);
            drop.setText("");
            drop.setTextSize(18f);
            drop.setGravity(Gravity.CENTER);
            drop.setBackgroundColor(Color.parseColor("#DDDDDD"));
            drop.setTextColor(Color.BLACK);
            drop.setLayoutParams(params);
            drop.setOnDragListener(dragListener);

            dropContainer.addView(drop);
            dropViews.add(drop);
        }
    }

    private void checkAnswer() {
        List<Integer> dropped = new ArrayList<>();
        for (TextView drop : dropViews) {
            String value = drop.getText().toString().trim();
            if (value.isEmpty()) {
                Toast.makeText(this, "Please fill all boxes", Toast.LENGTH_SHORT).show();
                return;
            }
            dropped.add(Integer.parseInt(value));
        }

        List<Integer> correctOrder = new ArrayList<>(originalNumbers);
        if (isAscending) {
            Collections.sort(correctOrder);
        } else {
            Collections.sort(correctOrder, Collections.reverseOrder());
        }


        // Play sound
        boolean correct = dropped.equals(correctOrder);
        soundPool.play(correct ? soundCorrect : soundWrong, 1, 1, 1, 0, 1);

        if (correct) {
            correctAnswers++;
            txtFeedback.setText("✅ Correct!");
            txtFeedback.setTextColor(Color.parseColor("#2E7D32")); // Dark green
        } else {
            txtFeedback.setText("❌ Incorrect!");
            txtFeedback.setTextColor(Color.parseColor("#D32F2F")); // Red
        }

        questionCount++;
        txtScore.setText("Score: " + correctAnswers);

        new Handler().postDelayed(() -> {
            if (questionCount < totalQuestions) {
                generateRandomNumbers(currentLevel);
            } else {
                showResultDialog();
            }
        }, 1500);
    }


    private void showResultDialog() {
        if (bgMusic != null && bgMusic.isPlaying()) {
            bgMusic.pause();
        }
        soundPool.play(soundVictory, 1, 1, 1, 0, 1);

        String resultMessage;
        if (correctAnswers >= 3) {
            resultMessage = "Congratulations! 🎉\nYou got " + correctAnswers + " out of " + totalQuestions + " correct!" + "\n\nKeep it up!";
        } else {
            resultMessage = "Good try! 😊\nYou got " + correctAnswers + " out of " + totalQuestions + " correct.\nKeep practicing!";
        }

        new Handler().postDelayed(() -> {
            new AlertDialog.Builder(this)
                    .setTitle("Level Completed!")
                    .setMessage(resultMessage)
                    .setCancelable(false)
                    .setPositiveButton("Play Again", (dialog, which) -> {
                        questionCount = 0;
                        correctAnswers = 0;
                        txtFeedback.setText("");
                        generateRandomNumbers(currentLevel);
                        if (!isMuted && bgMusic != null) {
                            bgMusic.start(); // Resume BGM if not muted
                        }
                    })
                    .setNegativeButton("Back", (dialog, which) -> finish())
                    .show();
        }, 2000); // 2 second delay
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
        if (bgMusic != null && !isMuted) {
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

