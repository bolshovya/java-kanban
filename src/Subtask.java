public class Subtask extends Task {
    private Epic subtaskIncludedInEpic;

    public Subtask(String name, String disription, Epic epic) {
        super(name, disription);
        this.subtaskIncludedInEpic = epic;
    }

    public Epic getSubtaskIncludedInEpic() {
        return this.subtaskIncludedInEpic;
    }

    @Override
    public String toString() {
        return "Название: \""+name+"\". Описание: \""+description+"\". Статус: \""+status+"\". Id: "+id+". Epic Id: "+subtaskIncludedInEpic.getId()+"\"";
    }

    public Integer getEpicId() {
        return subtaskIncludedInEpic.getId();
    }

}
