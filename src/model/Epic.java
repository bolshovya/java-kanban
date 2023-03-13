package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Epic extends Task {
    private ArrayList <Subtask> subtaskInEpic = new ArrayList<>();

    public Epic(String name, String discription) {
        super(name, discription);
    }

    public Epic(String name, String description, int id, TaskStatus status) {
        super(name, description, id, status);
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
        }
    }


    public List<Subtask> getSubtaskList() {
        List<Subtask> list = subtaskInEpic;
        return list;
    }

    public void setSubtaskList(List<Subtask> list) {
        this.subtaskInEpic = (ArrayList<Subtask>) list;
    }


}

