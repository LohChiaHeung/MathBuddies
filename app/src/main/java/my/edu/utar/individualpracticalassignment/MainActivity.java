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


        compareButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CompareNumbersActivity.class)));

        compareButton.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, CompareLevelsActivity.class)));


    }

}