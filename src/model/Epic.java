package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

public class Epic extends Task {
    private ArrayList <Subtask> subtaskInEpic = new ArrayList<>();
    Duration epicDuration;

    LocalDateTime startTime;
    LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
        this.startTime = LocalDateTime.MAX;
        this.epicDuration = Duration.ZERO;
        this.endTime = startTime.plus(epicDuration);
    }

    /*
    public Epic(String name, String description, int id, TaskStatus status) {
        super(name, description, id, status);
    }

     */

    public Epic(String name, String description, int id, TaskStatus status, LocalDateTime startTime, Duration duration, LocalDateTime endTime) {
        super(name, description, id, status, startTime, duration, endTime);
    }


    public void setEpicDuration() {
        if (subtaskInEpic.isEmpty()) {
            this.epicDuration = Duration.ZERO;
        }
        Duration sumDuration = Duration.ZERO;
        for (Subtask subtask : subtaskInEpic) {
            sumDuration = sumDuration.plus(subtask.getDuration());
        }
        this.epicDuration = sumDuration;
    }

    public Duration getEpicDuration() {
        return this.epicDuration;
    }

    public void setEpicStartTime() {
        if (subtaskInEpic.isEmpty()) {
            this.startTime = LocalDateTime.of(2001,1,1,1,1);
        } else {
            LocalDateTime earlySubtaskStartTime = LocalDateTime.MAX;
            for (Subtask subtask : subtaskInEpic) {
                if (earlySubtaskStartTime.isAfter(subtask.getStartTime())) {
                    earlySubtaskStartTime = subtask.getStartTime();
                }
            }
            this.startTime = earlySubtaskStartTime;
        }
    }

    public LocalDateTime getEpicStartTime() {
        return this.startTime;
    }


    public void setEpicEndTime() {
        if (subtaskInEpic.isEmpty()) {
            this.endTime = startTime;
        } else {
            LocalDateTime lastSubtaskEndTime = LocalDateTime.MIN;
            for (Subtask subtask : subtaskInEpic) {
                if (lastSubtaskEndTime.isBefore(subtask.getEndTime())) {
                    lastSubtaskEndTime = subtask.getEndTime();
                }
            }
            this.endTime = lastSubtaskEndTime;
        }
    }

    public LocalDateTime getEpicEndTime() {
        return this.endTime;
    }


    @Override
    public String saveTaskToString() {
        return id + "," + TaskType.EPIC + "," + name + "," + status + "," + description + "," +
                dateTimeFormatter(startTime) + "," + epicDuration.toMinutes() + "," + dateTimeFormatter(endTime);
    }

    public void setSubtaskIncludedInTheEpic(Subtask addSubtask) {
        this.subtaskInEpic.add(addSubtask);
    }

    @Override
    public String toString() {
        String result;
        result = "Название: \""+name+"\". Описание: \""+description+"\". Статус: \""+status+"\". Id: "+id+". \nПодзадачи Id: ";
        int i = 0;
        int size = subtaskInEpic.size();
        String[] subtaskStorage = new String[size];
        for (Subtask subtask : subtaskInEpic) {
            subtaskStorage[i] = String.valueOf(subtask.getId());
            i++;
        }
        return result+Arrays.toString(subtaskStorage);
    }


    public void updateEpicStatus() {
        int countNew = 0;
        int countDone = 0;
        for (Subtask subtask : subtaskInEpic) {
            if (subtask.getStatus() == TaskStatus.IN_PROGRESS) {
                setStatus(TaskStatus.IN_PROGRESS);
                return;
            } else if (subtask.getStatus() == TaskStatus.NEW) {
                countNew++;
            } else if (subtask.getStatus() == TaskStatus.DONE) {
                countDone++;
            }
        }
        if (countNew == subtaskInEpic.size()) {
            setStatus(TaskStatus.NEW);
        } else if (countDone == subtaskInEpic.size()) {
            setStatus(TaskStatus.DONE);
        } else {
            setStatus(TaskStatus.IN_PROGRESS);
        }
    }


    public List<Subtask> getSubtaskList() {
        try {
            List<Subtask> list = subtaskInEpic;
            return list;
        } catch (NullPointerException e) {
            return new ArrayList<>();
        }

    }

    public void setSubtaskList(List<Subtask> list) {
        this.subtaskInEpic = (ArrayList<Subtask>) list;
    }

    public void clearSubtaskList() {
        this.subtaskInEpic.clear();
    }


}

