package com.example.volunteers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import java.io.File;
import java.io.IOException;
import Interface.IUser;
import Model.User;
import ModelRequest.UserLogin;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String TOKEN_KEY = "token";
    private static final String BASE_URL = "https://192.168.34.216:8000/"; // Замените на IP и порт вашего сервера
    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File recentsDirectory = new File(getFilesDir(), "recents");
        if (!recentsDirectory.exists()) {
            if (!recentsDirectory.mkdirs()) {
                Log.e("DirectoryCreation", "Failed to create recents directory");
            }
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE);
        }

        // Проверка сохраненного токена авторизации
        String token = getAuthToken();
        if (token != null) {
            // Если токен существует, перенаправьте пользователя на HomeActivity
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        Button enterButton = findViewById(R.id.enter);
        Button registrationButton = findViewById(R.id.registration);
        Button supportButton = findViewById(R.id.support);
        EditText loginEditText = findViewById(R.id.login);
        EditText passwordEditText = findViewById(R.id.password);

        // Переход на экран HomeActivity
        enterButton.setOnClickListener(v -> {
            String login = loginEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (login.isEmpty() || password.isEmpty()) {
                Toast.makeText(MainActivity.this, "Введите логин и пароль", Toast.LENGTH_SHORT).show();
                return;
            }
            // Создание UserLogin
            UserLogin userLogin = new UserLogin(login, password);
            // Создание RetroFit
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL) // Используйте IP и порт вашего сервера
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            IUser userService = retrofit.create(IUser.class);
            Call<User> call = userService.loginUser(userLogin);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()){
                        User user = response.body();
                        if (user == null) {
                            Log.e("Login", "User is null");
                            Toast.makeText(MainActivity.this, "Ошибка получения данных пользователя", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Log.d("Login", "User data: " + user.toString());
                        // Извлечение токена из ответа
                        String token = user.getToken();
                        // Сохранение токена авторизации
                        saveAuthToken(token);
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Log.e("Login", "Response not successful: " + response.message());
                        Log.e("Login", "Response code: " + response.code());
                        try {
                            Log.e("Login", "Response body: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(MainActivity.this, "Неверный логин или пароль", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.e("Authorization", "Network error: " + t.getMessage());
                    Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Переход на экран регистрации
        registrationButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
            startActivity(intent);
            finish();
        });
        // Открытие почтового приложения
        supportButton.setOnClickListener(v -> openGmailSupport());
    }

    // Метод для открытия почтового приложения
    private void openGmailSupport() {
        String supportEmail = "novyjformatpodderzka35@gmail.com"; // Почта поддержки
        String subject = "Сообщение об ошибке"; // Тема письма
        String body = "Здравствуйте. \n При использовании возникла ошибка."; // Текст сообщения (если требуется)

        // Создаём Intent для отправки письма
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822"); // Ограничиваем приложения только почтовыми клиентами
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{supportEmail});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);

        try {
            // Пытаемся открыть почтовое приложение через Intent
            startActivity(Intent.createChooser(emailIntent, "Выберите почтовое приложение"));
        } catch (android.content.ActivityNotFoundException ex) {
            // Выводим сообщение, если почтовое приложение отсутствует
            Toast.makeText(this, "Нет доступных приложений для отправки письма", Toast.LENGTH_SHORT).show();
        }
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
