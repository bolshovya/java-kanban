package model;

public class Task {
    protected String name;
    protected String description;
    protected TaskStatus status;
    protected int id;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.id = createId();
        this.status = TaskStatus.NEW;
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

    public TaskStatus getStatus() {
        return this.status;
    }

    public void setStatus(TaskStatus newStatus) {
        this.status = newStatus;
    }
}
