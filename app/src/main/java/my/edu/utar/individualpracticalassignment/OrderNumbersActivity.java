package my.edu.utar.individualpracticalassignment;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class OrderNumbersActivity extends BaseActivity {
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


    //Handle the 'drag and drop' behaviour
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
                drop.setBackgroundColor(Color.parseColor("#177572")); //filled

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

        //Set the status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#693c28"));
        }

        //Initialize UI Components
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

        Button btnReset = findViewById(R.id.btnReset);
        btnReset.setOnClickListener(v -> resetCurrentDragDrop());

        btnCheck.setOnClickListener(v -> checkAnswer());
        setupBackButton(R.id.btnBack);
        txtFeedback = findViewById(R.id.txtFeedback);

        generateRandomNumbers(currentLevel);

        // Background Music
        bgMusic = MediaPlayer.create(this, R.raw.bg_music);
        bgMusic.setLooping(true);
        bgMusic.setVolume(0.3f, 0.3f);
        bgMusic.start();

        //Mute and Unmute function
        btnMute = findViewById(R.id.btnMute);

        btnMute.setOnClickListener(v -> {
            if (isMuted) {
                bgMusic.start();
                btnMute.setBackgroundColor(Color.parseColor("#734012"));
                btnMute.setText("\uD83D\uDD0A");
                isMuted = false;
            } else {
                bgMusic.pause();
                btnMute.setBackgroundColor(Color.parseColor("#7A7A79"));
                btnMute.setText("\uD83D\uDD07");
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

    //Generate random numbers and populates draggable and drop zones
    private void generateRandomNumbers(int level) {
        originalNumbers.clear();
        dragContainer.removeAllViews();
        dropContainer.removeAllViews();
        dragViews.clear();
        dropViews.clear();
        txtFeedback.setText("");

        txtQuestionCount.setText("Question " + (questionCount + 1) + " of " + totalQuestions);
        Typeface chewy = ResourcesCompat.getFont(this, R.font.chewy_font);

        // Randomly choose Ascending or Descending Order
        Random rand = new Random();
        isAscending = rand.nextBoolean(); // true = ascending, false = descending
        txtOrderType.setText(isAscending ? "Ascending Order \uD83D\uDCC8" : "Descending Order \uD83D\uDCC9");

        // Determine how many boxes to use
        // Level 1 : 3 boxes, Level 2: 4 boxes Levels 3-5: 5 boxes
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

        //Create drag and drop vies
        for (int i = 0; i < boxCount; i++) {
            // Create drag view
            TextView drag = new TextView(this);
            drag.setTag("unused");
            drag.setText(String.valueOf(shuffled.get(i)));
            drag.setTextSize(20f);
            drag.setGravity(Gravity.CENTER);
            drag.setBackgroundColor(Color.parseColor("#177572"));
            drag.setTextColor(Color.WHITE);
            drag.setTypeface(chewy);

            //Use Linearlayout to make margin (spacing) between each of the buttons
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
            drop.setTextSize(20f);
            drop.setTypeface(chewy);
            drop.setGravity(Gravity.CENTER);
            drop.setBackgroundColor(Color.parseColor("#63c3a9"));
            drop.setTextColor(Color.WHITE);
            drop.setLayoutParams(params);
            drop.setOnDragListener(dragListener);

            dropContainer.addView(drop);
            dropViews.add(drop);
        }
    }

    //Reset Button
    private void resetCurrentDragDrop() {
        // Clear drop boxes and reset background
        for (TextView drop : dropViews) {
            drop.setText("");
            drop.setBackgroundColor(Color.parseColor("#63c3a9")); // Default color
        }

        // Reset draggable items (re-enable and restore background if needed)
        for (TextView drag : dragViews) {
            drag.setBackgroundColor(Color.parseColor("#177572")); // Original green
            drag.setTag("unused"); // Mark as usable again
        }

        txtFeedback.setText(""); // Clear feedback
    }


    //Check Answer
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
            txtFeedback.setTextColor(Color.parseColor("#2E7D32"));
        } else {
            txtFeedback.setText("❌ Incorrect!");
            txtFeedback.setTextColor(Color.parseColor("#D32F2F"));
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


    //Display final results dialog
    private void showResultDialog() {
        if (bgMusic != null && bgMusic.isPlaying()) {
            bgMusic.pause();
        }
        soundPool.play(soundVictory, 0.5f, 0.5f, 1, 0, 1);

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
                        txtScore.setText("Score: " + correctAnswers);
                        generateRandomNumbers(currentLevel);
                        if (!isMuted && bgMusic != null) {
                            bgMusic.start(); // Resume BGM if not muted
                        }
                    })
                    .setNegativeButton("Back", (dialog, which) -> finish())
                    .show();
        }, 2000); // 2 second delay
    }

    //Manage the background music
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

