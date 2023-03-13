package model;

public class Task {
    protected String name;
    protected String description;
    protected TaskStatus status;
    protected int id;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;
    }

    public Task(String name, String description, int id, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
    }

    public static Task fromString(String value) {
        String[] parts = value.split(",");
        if (parts.length > 5) {
            int id = Integer.parseInt(parts[0]);
            TaskType type = TaskType.valueOf(parts[1]);
            String name = parts[2];
            TaskStatus status = TaskStatus.valueOf(parts[3]);
            String description = parts[4];
            int epicId = Integer.parseInt(parts[5]);
            return new Subtask(name, description, id, status, epicId);
        } else {
            int id = Integer.parseInt(parts[0]);
            TaskType type = TaskType.valueOf(parts[1]);
            String name = parts[2];
            TaskStatus status = TaskStatus.valueOf(parts[3]);
            String description = parts[4];
            switch (type) {
                case TASK:
                    return new Task(name, description, id, status);

                case EPIC:
                    return new Epic(name, description, id, status);
            }
        }
        return null;
    }

    public TaskType getType() {
        return TaskType.TASK;
    }

    public String saveTaskToString() {
        return id + "," + TaskType.TASK + "," + name + "," + status + "," + description;
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
}
