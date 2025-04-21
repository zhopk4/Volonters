package com.example.volunteers;

import java.io.File;

public class CheckRecentsDirectory {
    public static void main(String[] args) {
        // Путь к директории с недавними задачами
        String recentsDirectoryPath = "/path/to/recents/directory";
        File recentsDirectory = new File(recentsDirectoryPath);

        // Проверка существования директории
        if (!recentsDirectory.exists()) {
            // Создание директории, если она не существует
            if (recentsDirectory.mkdirs()) {
                System.out.println("Recents directory created successfully.");
            } else {
                System.out.println("Failed to create recents directory.");
            }
        } else {
            System.out.println("Recents directory already exists.");
        }
    }
}
