package my.edu.utar.individualpracticalassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button compareButton = findViewById(R.id.compareNumbers);
        Button orderButton = findViewById(R.id.orderNumbers);

        compareButton.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, CompareLevelsActivity.class)));

        orderButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, OrderLevelsActivity.class); // or OrderNumbersActivity directly
            startActivity(intent);
        });

    }

}