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

    public Task(String name, String description, TaskStatus taskStatus) {
        this.name = name;
        this.description = description;
        this.status = taskStatus;
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

    public Task(String name, String description, int id, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;
        this.id = id;
        this.startTime = startTime;
        this.duration = duration;
        getEndTime();
    }


    public Task(String name, String description, TaskStatus status, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
        getEndTime();
    }

    public TaskType getType() {
        return TaskType.TASK;
    }

    public String saveTaskToString() {
        return id + "," + TaskType.TASK + "," + name + "," + status + "," + description + "," +
                dateTimeFormatter(startTime) + "," + duration.toMinutes() + "," + dateTimeFormatter(getEndTime());
    }

    protected String dateTimeFormatter(LocalDateTime dateTime) {
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
