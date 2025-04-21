package com.example.volunteers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import java.io.IOException;
import Model.NewUser;
import ModelRequest.UserRegistration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import Interface.IUser;
import RetrofitModels.RetroFit;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        EditText nameEditText = findViewById(R.id.name);
        EditText surnameEditText = findViewById(R.id.surname);
        EditText patronymicEditText = findViewById(R.id.patronymic);
        EditText groupNameEditText = findViewById(R.id.groupName);
        EditText birthdayEditText = findViewById(R.id.datebirthing);
        EditText loginEditText = findViewById(R.id.login);
        EditText emailEditText = findViewById(R.id.email);
        EditText passwordEditText = findViewById(R.id.password);
        Button registerButton = findViewById(R.id.registration);
        Button backMainButton = findViewById(R.id.backMain);

        backMainButton.setOnClickListener(v -> {
            Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        registerButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String surname = surnameEditText.getText().toString().trim();
            String patronymic = patronymicEditText.getText().toString().trim();
            String groupName = groupNameEditText.getText().toString().trim();
            String birthday = birthdayEditText.getText().toString().trim();
            String login = loginEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (name.isEmpty() || surname.isEmpty() || patronymic.isEmpty() || groupName.isEmpty()
                    || birthday.isEmpty() || login.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(RegistrationActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            // Создание Retrofit
            Retrofit retrofit = RetroFit.getClient("postgresql://postgres:951357@localhost:5432/test1"); // для эмулятора
            IUser userService = retrofit.create(IUser.class);

            // Создание объекта UserRegistration
            UserRegistration userRegistration = new UserRegistration();
            userRegistration.setName(name);
            userRegistration.setSurname(surname);
            userRegistration.setPatronymic(patronymic);
            userRegistration.setGroupName(groupName);
            userRegistration.setBirthday(birthday);
            userRegistration.setLogin(login);
            userRegistration.setEmail(email);
            userRegistration.setPassword(password);

            // Логирование данных, отправляемых на сервер
            Log.d("RegistrationActivity", "Sending data: " + new Gson().toJson(userRegistration));

            // Вызов API для регистрации
            Call<NewUser> call = userService.registerUser(userRegistration);
            call.enqueue(new Callback<NewUser>() {
                @Override
                public void onResponse(Call<NewUser> call, Response<NewUser> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(RegistrationActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        try {
                            Log.e("RegistrationActivity", "Response body: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(RegistrationActivity.this, "Registration failed: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<NewUser> call, Throwable t) {
                    Log.e("RegistrationActivity", "Network error: " + t.getMessage());
                    Toast.makeText(RegistrationActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

    }
}
