package com.example.volunteers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Model.Task;
import ModelRequest.TaskCreate;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import Interface.ITask;

public class ListActivity extends AppCompatActivity {

    private EditText taskEditText;
    private EditText taskDescriptionEditText;
    private Button addButton;
    private RecyclerView tasksRecyclerView;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;
    private Retrofit retrofit;
    private ITask taskService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        taskEditText = findViewById(R.id.taskEditText);
        taskDescriptionEditText = findViewById(R.id.taskDescriptionEditText);
        addButton = findViewById(R.id.addButton);
        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        Button backButton = findViewById(R.id.back);

        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(taskList, task -> deleteTask(task.getId()));

        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksRecyclerView.setAdapter(taskAdapter);

        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/") // для эмулятора
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        taskService = retrofit.create(ITask.class);

        addButton.setOnClickListener(v -> addTask());

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(ListActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });

        loadTasks();
    }

    private void addTask() {
        String task = taskEditText.getText().toString().trim();
        String taskDescription = taskDescriptionEditText.getText().toString().trim();

        if (task.isEmpty() || taskDescription.isEmpty()) {
            Toast.makeText(this, "Please enter both task and description", Toast.LENGTH_SHORT).show();
            return;
        }

        TaskCreate newTask = new TaskCreate(task, taskDescription);
        Call<TaskCreate> call = taskService.create_task(newTask);
        call.enqueue(new Callback<TaskCreate>() {
            @Override
            public void onResponse(Call<TaskCreate> call, Response<TaskCreate> response) {
                if (response.isSuccessful()) {
                    TaskCreate createdTask = response.body();
                    if (createdTask != null) {
                        Task taskItem = new Task(createdTask.id, createdTask.task, createdTask.task_description);
                        taskList.add(taskItem);
                        taskAdapter.notifyItemInserted(taskList.size() - 1);

                        // Очистка полей ввода
                        taskEditText.setText("");
                        taskDescriptionEditText.setText("");
                    } else {
                        Toast.makeText(ListActivity.this, "Failed to add task: response body is null", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        Log.e("ListActivity", "Response body: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(ListActivity.this, "Failed to add task", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TaskCreate> call, Throwable t) {
                Log.e("ListActivity", "Network error: " + t.getMessage());
                Toast.makeText(ListActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadTasks() {
        Call<List<TaskCreate>> call = taskService.read_tasks();
        call.enqueue(new Callback<List<TaskCreate>>() {
            @Override
            public void onResponse(Call<List<TaskCreate>> call, Response<List<TaskCreate>> response) {
                if (response.isSuccessful()) {
                    List<TaskCreate> tasks = response.body();
                    if (tasks != null) {
                        taskList.clear();
                        for (TaskCreate task : tasks) {
                            taskList.add(new Task(task.id, task.task, task.task_description));
                        }
                        taskAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(ListActivity.this, "Failed to load tasks: response body is null", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        Log.e("ListActivity", "Response body: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(ListActivity.this, "Failed to load tasks", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TaskCreate>> call, Throwable t) {
                Log.e("ListActivity", "Network error: " + t.getMessage());
                Toast.makeText(ListActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteTask(int taskId) {
        Call<TaskCreate> call = taskService.delete_task(taskId);
        call.enqueue(new Callback<TaskCreate>() {
            @Override
            public void onResponse(Call<TaskCreate> call, Response<TaskCreate> response) {
                if (response.isSuccessful()) {
                    TaskCreate deletedTask = response.body();
                    if (deletedTask != null) {
                        for (int i = 0; i < taskList.size(); i++) {
                            if (taskList.get(i).getId() == deletedTask.id) {
                                taskList.remove(i);
                                taskAdapter.notifyItemRemoved(i);
                                Log.d("ListActivity", "Task with id " + taskId + " deleted successfully");  // Логирование успешного удаления
                                break;
                            }
                        }
                    } else {
                        Toast.makeText(ListActivity.this, "Failed to delete task: response body is null", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        Log.e("ListActivity", "Response body: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(ListActivity.this, "Failed to delete task", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TaskCreate> call, Throwable t) {
                Log.e("ListActivity", "Network error: " + t.getMessage());
                Toast.makeText(ListActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
