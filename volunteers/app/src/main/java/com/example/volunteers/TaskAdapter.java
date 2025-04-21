package com.example.volunteers;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import Model.Task;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;
    private final OnTaskDeleteListener onTaskDeleteListener;

    public TaskAdapter(List<Task> taskList, OnTaskDeleteListener onTaskDeleteListener) {
        this.taskList = taskList;
        this.onTaskDeleteListener = onTaskDeleteListener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        if (holder.taskTextView != null) {
            holder.taskTextView.setText(task.getTask());
        } else {
            Log.e("TaskAdapter", "taskTextView is null");
        }
        if (holder.taskDescriptionTextView != null) {
            holder.taskDescriptionTextView.setText(task.getTaskDescription());
        } else {
            Log.e("TaskAdapter", "taskDescriptionTextView is null");
        }
        if (holder.deleteButton != null) {
            holder.deleteButton.setOnClickListener(v -> onTaskDeleteListener.onTaskDelete(task));
        } else {
            Log.e("TaskAdapter", "deleteButton is null");
        }
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskTextView;
        TextView taskDescriptionTextView;
        Button deleteButton;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTextView = itemView.findViewById(R.id.taskTextView);
            taskDescriptionTextView = itemView.findViewById(R.id.taskDescriptionTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    public interface OnTaskDeleteListener {
        void onTaskDelete(Task task);
    }
}
