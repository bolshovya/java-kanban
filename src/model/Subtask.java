package model;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, int id, TaskStatus taskStatus, int epicId) {
        super(name, description, id, taskStatus);
        this.epicId = epicId;
    }


    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
    }


    @Override
    public String saveTaskToString() {
        return id + "," + TaskType.SUBTASK + "," + name + "," + status + "," + description + "," + epicId;
    }

    /*
    public Epic getSubtaskIncludedInEpic() {
        return this.epicTag;
    }

     */

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
