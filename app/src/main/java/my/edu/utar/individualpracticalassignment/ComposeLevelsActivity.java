package my.edu.utar.individualpracticalassignment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

public class ComposeLevelsActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_levels);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#693c28"));
        }

        // Back Button
        setupBackButton(R.id.btnBack);

        // Tutorial Button
        Button btnTutorial = findViewById(R.id.btnTutorial);
        btnTutorial.setOnClickListener(v -> {
            Intent intent = new Intent(this, TutorialActivityComposeNumber.class);
            startActivity(intent);
        });

        // Level Buttons
        findViewById(R.id.btnLevel1).setOnClickListener(v -> startLevel(1));
        findViewById(R.id.btnLevel2).setOnClickListener(v -> startLevel(2));
        findViewById(R.id.btnLevel3).setOnClickListener(v -> startLevel(3));
        findViewById(R.id.btnLevel4).setOnClickListener(v -> startLevel(4));
        findViewById(R.id.btnLevel5).setOnClickListener(v -> startLevel(5));
    }

    private void startLevel(int level) {
        Intent intent = new Intent(this, ComposeNumbersActivity.class);
        intent.putExtra("level", level);
        startActivity(intent);
    }
}
