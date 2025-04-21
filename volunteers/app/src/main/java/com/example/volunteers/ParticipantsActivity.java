package com.example.volunteers;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ParticipantsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participants);

        Button backParticipantsButton = findViewById(R.id.backParticipants);

        backParticipantsButton.setOnClickListener(v -> {
            Intent intent = new Intent(ParticipantsActivity.this, HomeActivity.class);
            if(intent != null){
                startActivity(intent);
                finish();}
            else{
                Toast.makeText(this, "Error",Toast.LENGTH_SHORT);
            }
        });
    }
}