package model;

public class Subtask extends Task {
    private Epic epicTag;

    public Subtask(String name, String disription, Epic epic) {
        super(name, disription);
        this.epicTag = epic;
    }

    public Epic getSubtaskIncludedInEpic() {
        return this.epicTag;
    }

    @Override
    public String toString() {
        return "Название: \""+name+"\". Описание: \""+description+"\". Статус: \""+status+"\". Id: "+id+". Epic Id: "+epicTag.getId()+"\"";
    }

    public Integer getEpicId() {
        return epicTag.getId();
    }

    @Override
    public void setStatus(String newStatus) {
        this.status = newStatus;
        Epic epic = this.epicTag;
        epic.updateEpicStatus();
    }

}
