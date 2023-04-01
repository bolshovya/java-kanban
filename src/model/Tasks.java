package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Tasks {

    public static Task fromString(String value) {
        String[] parts = value.split(",");
        if (parts.length > 8) {
            int id = Integer.parseInt(parts[0]);
            TaskType type = TaskType.valueOf(parts[1]);
            String name = parts[2];
            TaskStatus status = TaskStatus.valueOf(parts[3]);
            String description = parts[4];
            LocalDateTime startTime = LocalDateTime.parse(parts[5], DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
            Duration durationTask = Duration.ofMinutes(Integer.parseInt(parts[6]));
            LocalDateTime endTime = LocalDateTime.parse(parts[7],DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
            int epicId = Integer.parseInt(parts[8]);
            return new Subtask(name, description, id, status, startTime, durationTask, endTime, epicId);
        } else {
            int id = Integer.parseInt(parts[0]);
            TaskType type = TaskType.valueOf(parts[1]);
            String name = parts[2];
            TaskStatus status = TaskStatus.valueOf(parts[3]);
            String description = parts[4];
            LocalDateTime startTime = LocalDateTime.parse(parts[5],DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
            Duration durationTask = Duration.ofMinutes(Integer.parseInt(parts[6]));
            LocalDateTime endTime = LocalDateTime.parse(parts[7],DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
            switch (type) {
                case TASK:
                    return new Task(name, description, id, status, startTime, durationTask, endTime);

                case EPIC:
                    return new Epic(name, description, id, status, startTime, durationTask, endTime);
            }
        }
        return null;
    }

}
