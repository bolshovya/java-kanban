import java.util.ArrayList;
import java.util.Arrays;

public class Epic extends Task {
    ArrayList <Subtask> subtaskIncludedInTheEpic= new ArrayList<>();

    public Epic(String name, String discription) {
        super(name, discription);
    }

    public void setSubtaskIncludedInTheEpic(Subtask addSubtask) {
        this.subtaskIncludedInTheEpic.add(addSubtask);
    }

    @Override
    public String toString() {
        String result;
        result = "Название: \""+name+"\". Описание: \""+description+"\". Статус: \""+status+"\". Id: "+id+". \nПодзадачи Id: ";
        int i = 0;
        int size = subtaskIncludedInTheEpic.size();
        String[] subtaskStorage = new String[size];
        for (Subtask subtask : subtaskIncludedInTheEpic) {
            subtaskStorage[i] = String.valueOf(subtask.getId());
            i++;
        }
        return result+Arrays.toString(subtaskStorage);
    }

}
