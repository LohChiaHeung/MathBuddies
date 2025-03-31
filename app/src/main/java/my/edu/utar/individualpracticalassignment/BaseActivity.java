package my.edu.utar.individualpracticalassignment;

import android.graphics.Color;
import android.os.Build;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    protected void setupBackButton(int buttonId) {
        Button btnBack = findViewById(buttonId);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }
    }

}
