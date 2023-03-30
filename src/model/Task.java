package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    protected String name;
    protected String description;
    protected TaskStatus status;
    protected int id;
    protected LocalDateTime startTime;
    protected Duration duration;
    protected LocalDateTime endTime;


    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;
    }


    public Task(String name, String description, int id, TaskStatus status, LocalDateTime startTime, Duration duration, LocalDateTime endTime) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = endTime;
    }


    public Task(String name, String description, TaskStatus status, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
        getEndTime();
    }




    public static Task fromString(String value) {
        String[] parts = value.split(",");
        if (parts.length > 8) {
            int id = Integer.parseInt(parts[0]);
            TaskType type = TaskType.valueOf(parts[1]);
            String name = parts[2];
            TaskStatus status = TaskStatus.valueOf(parts[3]);
            String description = parts[4];
            LocalDateTime startTime = LocalDateTime.parse(parts[5],DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
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

    public TaskType getType() {
        return TaskType.TASK;
    }

    public String saveTaskToString() {
        return id + "," + TaskType.TASK + "," + name + "," + status + "," + description + "," +
                dateTimeFormatter(startTime) + "," + duration.toMinutes() + "," + dateTimeFormatter(getEndTime());
    }

    String dateTimeFormatter(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return dateTime.format(formatter);
    }

    @Override
    public String toString() {
        return "Название: \""+name+"\". Описание: \""+description+"\". Статус: \""+status+"\". Id: "+id+".";
    }

    @Override
    public int hashCode() {
        int hash = 1;
        if (name != null) {
            hash = hash + name.hashCode();
        }
        hash = hash * 3;
        if (description != null) {
            hash = hash * description.hashCode();
        }
        return Math.abs(hash);
    }

    public int createId() {
        return Math.abs(hashCode());
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;

    }

    public TaskStatus getStatus() {
        return this.status;
    }

    public void setStatus(TaskStatus newStatus) {
        this.status = newStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && name.equals(task.name) && description.equals(task.description) && status == task.status;
    }

    public Duration getDuration() {
        return this.duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        if (duration == null || startTime == null) {
            return null;
        } else {
            return startTime.plus(duration);
        }
    }
}
