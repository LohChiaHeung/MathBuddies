package my.edu.utar.individualpracticalassignment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

public class CompareLevelsActivity extends BaseActivity {

    Button tutorialBtn, level1Btn, level2Btn, level3Btn, level4Btn, level5Btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_levels);
        setupBackButton(R.id.btnBack);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#693c28"));
        }

        tutorialBtn = findViewById(R.id.btnTutorial);
        level1Btn = findViewById(R.id.btnLevel1);
        level2Btn = findViewById(R.id.btnLevel2);
        level3Btn = findViewById(R.id.btnLevel3);
        level4Btn = findViewById(R.id.btnLevel4);
        level5Btn = findViewById(R.id.btnLevel5);

        tutorialBtn.setOnClickListener(v -> {
            Intent intent = new Intent(CompareLevelsActivity.this, TutorialActivity.class);
            startActivity(intent);
        });

        //Level 1 [Basic]: 1 - 10
        level1Btn.setOnClickListener(v -> startQuizWithRange(1, 10,"Level 1 – Basic",false));
        //Level 2 [Easy]: 11 - 20
        level2Btn.setOnClickListener(v -> startQuizWithRange(11, 20,"Level 2 – Easy", false));
        //Level 3 [Normal]: 21 - 30
        level3Btn.setOnClickListener(v -> startQuizWithRange(21, 30,"Level 3 – Normal", false));
        //Level 4 [Hard]: 31 - 50
        level4Btn.setOnClickListener(v -> startQuizWithRange(31, 50,"Level 4 – Hard", true));
        //Level 5 [Very Hard]: 51 - 100
        level5Btn.setOnClickListener(v -> startQuizWithRange(51, 100,"Level 5 – Very Hard", true));
    }

    private void startQuizWithRange(int min, int max, String levelName, boolean shuffle) {
        Intent intent = new Intent(this, CompareNumbersActivity.class);
        intent.putExtra("min", min);
        intent.putExtra("max", max);
        intent.putExtra("levelName", levelName);
        intent.putExtra("shuffle", shuffle);
        startActivity(intent);
    }
}