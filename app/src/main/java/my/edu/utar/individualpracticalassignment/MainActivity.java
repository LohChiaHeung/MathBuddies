package my.edu.utar.individualpracticalassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button compareButton = findViewById(R.id.compareNumbers);
        Button orderButton = findViewById(R.id.orderNumbers);
        Button composeButton = findViewById(R.id.composeNumbers);

        compareButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CompareLevelsActivity.class); // or OrderNumbersActivity directly
            startActivity(intent);
        });

        orderButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, OrderLevelsActivity.class); // or OrderNumbersActivity directly
            startActivity(intent);
        });

        composeButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ComposeLevelsActivity.class); // or OrderNumbersActivity directly
            startActivity(intent);
        });

    }

}