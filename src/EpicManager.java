import java.util.ArrayList;
import java.util.HashMap;

public class EpicManager {

    HashMap <Integer, Epic> epicStorage = new HashMap<>();

    HashMap <Integer, Subtask> subtaskStorage = new HashMap<>();

    public void updateTaskStatus(Task task) {
        if (task.getStatus() == "NEW") {
            task.setStatus("IN_PROGRESS");
        } else if (task.getStatus() == "IN_PROGRESS") {
            task.setStatus("DONE");
        } else {
            task.setStatus("DONE");
        }
    }

    public void updateSubtaskStatus(Subtask subtask) {
        if (subtask.getStatus() == "NEW") {
            subtask.setStatus("IN_PROGRESS");
        } else if (subtask.getStatus() == "IN_PROGRESS") {
            subtask.setStatus("DONE");
        } else {
            subtask.setStatus("DONE");
        }
        updateEpicStatus(subtask);
    }


    public void updateEpicStatus(Subtask newSubtask) {
        Integer passedEpic = newSubtask.getEpicId();
        ArrayList<Subtask> subtaskPassedEpic = new ArrayList<>();
        for (Subtask subtask : subtaskStorage.values()) {
            if (passedEpic.equals(subtask.getEpicId())) {
                subtaskPassedEpic.add(subtask);
            }
        }
        if (subtaskPassedEpic.isEmpty() || subtaskPassedEpic == null) {
            Epic epicCorrect = epicStorage.get(passedEpic);
            epicCorrect.setStatus("NEW");
            epicStorage.put(epicCorrect.getId(), epicCorrect);
            return;
        }
        int countNew = 0;
        int countDone = 0;
        for (Subtask subtask : subtaskPassedEpic) {
            if (subtask.getStatus() == "NEW") {
                countNew++;
            } else if (subtask.getStatus() == "DONE") {
                countDone++;
            }
        }
        if (countNew == subtaskPassedEpic.size()) {
            Epic epicCorrect = epicStorage.get(passedEpic);
            epicCorrect.setStatus("NEW");
            epicStorage.put(epicCorrect.getId(), epicCorrect);
        } else if (countDone == subtaskPassedEpic.size()) {
            Epic epicCorrect = epicStorage.get(passedEpic);
            epicCorrect.setStatus("DONE");
            epicStorage.put(epicCorrect.getId(), epicCorrect);
        } else {
            Epic epicCorrect = epicStorage.get(passedEpic);
            epicCorrect.setStatus("IN_PROGRESS");
            epicStorage.put(epicCorrect.getId(), epicCorrect);
        }
    }


    public void listAllEpic() { // 2.1 Получение списка всех эпик-задач.
        System.out.println("Списко эпиков:");
        for (Task task : epicStorage.values()) {
            System.out.println(task);
        }
    }

    public void clearEpicList() { // 2.2 Удаление всех эпик-задач.
        epicStorage.clear();
        for (Task task : epicStorage.values()) { // для проверки
            System.out.println(task);
        }
        System.out.println("Список эпиков пуст.");
    }

    public Epic getEpicById(Integer id) { // 2.3 Получение эпик-задачи по идентификатору
        return epicStorage.get(id);
    }

    public void addEpic(Epic newEpic) { // 2.4 Создание эпик-задачи. Сам объект должен передаваться в качестве параметра.
        epicStorage.put(newEpic.getId(), newEpic);
    }

    public void updateEpic(Integer idTask, Epic newEpic) { // 2.5 Обновление эпик-задачи.
        epicStorage.put(idTask, newEpic);
    }

    public void removeEpic(Integer id) { // 2.6 Удаление по идентификатору
        epicStorage.remove(id);
        ArrayList<Integer> keys = new ArrayList<>();
        for (Subtask subtask : subtaskStorage.values()) {
            if ((subtask.getEpicId()).equals(id)) {
                keys.add(subtask.getId());
            }
        }
        for (Integer key : keys) {
            subtaskStorage.remove(key);
        }
    }

    public void subtaskListByEpic(Epic epic) { // 3.1 Получение списка всех подзадач определенённого эпика
        ArrayList<Subtask> epicIdInSubtask = new ArrayList<>();
        for (Subtask subtask : subtaskStorage.values()) {
            if ((subtask.getSubtaskIncludedInEpic()).equals(epic)) {
                epicIdInSubtask.add(subtask);
            }
        }
        for (Subtask subtask : epicIdInSubtask) {
            System.out.println(subtask);
        }
    }

    public void listAllSubtask() { // 2.1 Получение списка всех подзадач.
        System.out.println("Список подзадач:");
        for (Subtask subtask : subtaskStorage.values()) {
            System.out.println(subtask);
        }
    }

    public void clearSubtaskList() { // 2.2 Удаление всех подзадач.
        subtaskStorage.clear();
        for (Subtask subtask : subtaskStorage.values()) { // для проверки
            System.out.println(subtask);
        }
        System.out.println("Список задач пуст.");
    }

    public void getSubtaskById(Integer id) { // 2.3 Получение по идентификатору
        System.out.println(subtaskStorage.get(id));
    }

    public void addSubtask(Subtask newSubtask) { // 2.4 Создание. Сам объект должен передаваться в качестве параметра.
        subtaskStorage.put(newSubtask.getId(), newSubtask);
        Integer epicId = newSubtask.getEpicId();
        Epic epicBuffer = epicStorage.get(epicId);
        epicBuffer.setSubtaskIncludedInTheEpic(newSubtask);
        epicStorage.put(epicBuffer.getId(), epicBuffer);
        updateEpicStatus(newSubtask);
    }

    public void updateSubtask(Integer idSubtask, Subtask newSubtask) { // 2.5 Обновление подзадачи.
        subtaskStorage.put(idSubtask, newSubtask);
    }

    public void removeSubtask(Integer id) { // 2.6 Удаление подзадачи по идентификатору
        subtaskStorage.remove(id);
    }

}
