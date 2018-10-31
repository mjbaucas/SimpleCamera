package com.mjbaucas.simplecamera;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button takeBtn;
    Button browseBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        takeBtn = findViewById(R.id.take_photo);
        takeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camInt = new Intent(MainActivity.this , CameraActivity.class);
                startActivity(camInt);
            }
        });

        browseBtn = findViewById(R.id.browse_photos);
        browseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
