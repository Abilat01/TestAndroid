package com.example.geoquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static android.app.ProgressDialog.show;

public class MainActivity extends AppCompatActivity {

    private Button btnFalse;
    private Button btnTrue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btnFalse = (Button) findViewById(R.id.btnFalse);
        btnFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,
                R.string.incorrect_toast,
                        Toast.LENGTH_SHORT).show();

            }
        });

        btnTrue = (Button) findViewById(R.id.btnTrue);
        btnTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,
                        R.string.correct_toast,
                        Toast.LENGTH_SHORT).show();

            }
        });
    }
}
