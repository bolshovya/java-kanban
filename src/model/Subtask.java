package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, String description, TaskStatus status, LocalDateTime startTime, Duration duration, int epicId) {
        super(name, description, status, startTime, duration);
        this.epicId = epicId;
    }

    public Subtask(String name, String description) {
        super(name, description);
    }

    public Subtask(String name, String description, TaskStatus taskStatus, int epicId) {
        super(name, description, taskStatus);
        this.epicId = epicId;
    }



    public Subtask(String name, String description, int id, TaskStatus taskStatus, LocalDateTime startTime,
                   Duration duration, LocalDateTime endTime, int epicId) {
        super(name, description, id, taskStatus, startTime, duration, endTime);
        this.epicId = epicId;
    }


    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
    }


    @Override
    public String saveTaskToString() {
        return id + "," + TaskType.SUBTASK + "," + name + "," + status + "," + description + "," +
                dateTimeFormatter(startTime) + "," + duration.toMinutes() + "," + dateTimeFormatter(getEndTime()) + "," + epicId;
    }

    @Override
    public String toString() {
        return "Название: \""+name+"\". Описание: \""+description+"\". Статус: \""+status+"\". Id: "+id+". Epic Id: "+ epicId +"\"";
    }

    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public void setStatus(TaskStatus newStatus) {
        this.status = newStatus;
    }

}
