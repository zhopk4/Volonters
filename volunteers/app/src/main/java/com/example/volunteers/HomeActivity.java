package com.example.volunteers;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        ImageView profileImageView = findViewById(R.id.profile);
        ImageView eventsImageView = findViewById(R.id.events);
        ImageView participantsImageView = findViewById(R.id.participants);
        ImageView listImageView = findViewById(R.id.list);

        profileImageView.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(intent);
            finish();
        });
        eventsImageView.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, EventActivity.class);
            startActivity(intent);
            finish();
        });
        participantsImageView.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ParticipantsActivity.class);
            startActivity(intent);
            finish();
        });
        listImageView.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ListActivity.class);
            startActivity(intent);
            finish();
        });

    }
}