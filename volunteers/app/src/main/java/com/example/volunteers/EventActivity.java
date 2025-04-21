package com.example.volunteers;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Button backHomeButton = findViewById(R.id.backEventHome);
        backHomeButton.setOnClickListener(v -> {
            Intent intent = new Intent(EventActivity.this, HomeActivity.class);
            if(intent != null){
                startActivity(intent);
                finish();}
            else{
                Toast.makeText(this, "Error",Toast.LENGTH_SHORT);
            }
        });
    }
}