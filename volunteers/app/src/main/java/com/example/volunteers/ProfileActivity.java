package com.example.volunteers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import ModelRequest.UserProfile;
import RetrofitModels.RetroFit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import Interface.IUser;
import retrofit2.Retrofit;

public class ProfileActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String TOKEN_KEY = "token";

    private TextView nameValue;
    private TextView surnameValue;
    private TextView patronymicValue;
    private TextView groupNameValue;
    private TextView birthdayValue;
    private TextView emailValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        nameValue = findViewById(R.id.nameValue);
        surnameValue = findViewById(R.id.surnameValue);
        patronymicValue = findViewById(R.id.patronymicValue);
        groupNameValue = findViewById(R.id.groupNameValue);
        birthdayValue = findViewById(R.id.birthdayValue);
        emailValue = findViewById(R.id.emailValue);
        Button logoutButton = findViewById(R.id.logout);
        Button backBtHomeButton = findViewById(R.id.backBtHome);

        backBtHomeButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });

        logoutButton.setOnClickListener(v -> logout());

        // Проверка сохраненного токена авторизации и загрузка данных профиля
        loadProfileData();
    }

    private void loadProfileData() {
        // Проверка сохраненного токена авторизации
        String token = getAuthToken();
        if (token != null) {
            // Создание Retrofit
            Retrofit retrofit = RetroFit.getClient("http://10.0.2.2:8000/");
            IUser userService = retrofit.create(IUser.class);

            // Вызов API для получения данных профиля
            Call<UserProfile> call = userService.read_user_profile("Bearer " + token);
            call.enqueue(new Callback<UserProfile>() {
                @Override
                public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                    if (response.isSuccessful()) {
                        UserProfile userProfile = response.body();
                        if (userProfile != null) {
                            nameValue.setText(userProfile.getName());
                            surnameValue.setText(userProfile.getSurname());
                            patronymicValue.setText(userProfile.getPatronymic());
                            groupNameValue.setText(userProfile.getGroupName());
                            birthdayValue.setText(userProfile.getBirthday().toString());
                            emailValue.setText(userProfile.getEmail());
                        } else {
                            Log.e("ProfileActivity", "UserProfile is null");
                            Toast.makeText(ProfileActivity.this, "Ошибка получения данных профиля", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            Log.e("ProfileActivity", "Response body: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(ProfileActivity.this, "Ошибка получения данных профиля: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UserProfile> call, Throwable t) {
                    Log.e("ProfileActivity", "Network error: " + t.getMessage());
                    Toast.makeText(ProfileActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(ProfileActivity.this, "Токен авторизации отсутствует", Toast.LENGTH_SHORT).show();
        }
    }

    private void logout() {
        // Удаление сохраненного токена авторизации
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(TOKEN_KEY);
        editor.apply();

        // Перенаправление на экран входа
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    // Метод для сохранения токена авторизации
    private void saveAuthToken(String token) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOKEN_KEY, token);
        editor.apply();
    }

    // Метод для получения сохраненного токена авторизации
    private String getAuthToken() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(TOKEN_KEY, null);
    }
}
