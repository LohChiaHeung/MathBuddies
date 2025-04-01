package my.edu.utar.individualpracticalassignment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;


public class OrderLevelsActivity extends BaseActivity {
    // Buttons for navigating tutorial and levels
    Button btnTutorial, btnLevel1, btnLevel2, btnLevel3, btnLevel4, btnLevel5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_levels);

        // Change status bar color in the phone
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#693c28"));
        }

        // Set up back button defined in BaseActivity
        setupBackButton(R.id.btnBack);

        btnTutorial = findViewById(R.id.btnTutorial);
        btnLevel1 = findViewById(R.id.btnLevel1);
        btnLevel2 = findViewById(R.id.btnLevel2);
        btnLevel3 = findViewById(R.id.btnLevel3);
        btnLevel4 = findViewById(R.id.btnLevel4);
        btnLevel5 = findViewById(R.id.btnLevel5);

        // Tutorial button opens the Order Number tutorial activity
        btnTutorial.setOnClickListener(v -> {
            Intent intent = new Intent(this, TutorialActivityOrderNumber.class);
            startActivity(intent);
        });

        // Level buttons trigger game start for specific levels
        btnLevel1.setOnClickListener(v -> launchLevel(1));
        btnLevel2.setOnClickListener(v -> launchLevel(2));
        btnLevel3.setOnClickListener(v -> launchLevel(3));
        btnLevel4.setOnClickListener(v -> launchLevel(4));
        btnLevel5.setOnClickListener(v -> launchLevel(5));
    }

    //Launches the OrderNumbersActivity with the selected level.
    private void launchLevel(int level) {
        Intent intent = new Intent(this, OrderNumbersActivity.class);
        intent.putExtra("level", level);
        startActivity(intent);
    }
}
