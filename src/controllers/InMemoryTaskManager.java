package controllers;

import model.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    protected static int idCount = 1;
    protected Map<Integer, Task> taskStorage = new HashMap<>();
    protected Map<Integer, Epic> epicStorage = new HashMap<>();
    protected Map<Integer, Subtask> subtaskStorage = new HashMap<>();
    HistoryManager historyManager = Managers.getDefaultHistory();


    @Override
    public List<Task> listOfAllTask() { // 2.1 Получение списка всех задач.
        List<Task> taskList = new ArrayList<>();
        for (Task task : taskStorage.values()) {
            taskList.add(task);
        }
        return (List<Task>) taskList;
    }

    @Override
    public List<Epic> listOfAllEpic() { // 2.1 Получение списка всех эпик-задач.
        List<Epic> epicList = new ArrayList<>();
        for (Epic epic : epicStorage.values()) {
            epicList.add(epic);
        }
        return (List<Epic>) epicList;
    }

    @Override
    public List<Subtask> listOfAllSubtask() { // 2.1 Получение списка всех подзадач.
        List<Subtask> subtaskList = new ArrayList<>();
        for (Subtask subtask : subtaskStorage.values()) {
            subtaskList.add(subtask);
        }
        return (List<Subtask>) subtaskList;
    }

    @Override
    public void clearTaskList() { // 2.2 Удаление всех задач.
        taskStorage.clear();
    }

    @Override
    public void clearEpicList() { // 2.2 Удаление всех эпик-задач.
        epicStorage.clear();
        subtaskStorage.clear();
    }

    @Override
    public void clearSubtaskList() { // 2.2 Удаление всех подзадач.
        subtaskStorage.clear();
    }

    @Override
    public Task getTaskById(Integer id) { // 2.3 Получение по идентификатору
        Task returnedTask = taskStorage.get(id);
        historyManager.add(returnedTask);
        return returnedTask;
    }

    @Override
    public Epic getEpicById(Integer id) { // 2.3 Получение эпик-задачи по идентификатору
        Epic returnedEpic = epicStorage.get(id);
        historyManager.add(returnedEpic);
        return returnedEpic;
    }

    @Override
    public Subtask getSubtaskById(Integer id) { // 2.3 Получение подзадачи по идентификатору
        Subtask returnedSubtask = subtaskStorage.get(id);
        historyManager.add(returnedSubtask);
        return returnedSubtask;
    }

    @Override
    public void addTask(Task newTask) { // 2.4 Создание. Сам объект должен передаваться в качестве параметра.
        newTask.setId(idCount);
        taskStorage.put(idCount, newTask);
        idCount++;
    }

    @Override
    public void addEpic(Epic newEpic) { // 2.4 Создание эпик-задачи. Сам объект должен передаваться в качестве параметра.
        newEpic.setId(idCount);
        epicStorage.put(idCount, newEpic);
        idCount++;
    }

    @Override
    public void addSubtask(Subtask newSubtask) { // 2.4 Создание. Сам объект должен передаваться в качестве параметра.
        newSubtask.setId(idCount);
        subtaskStorage.put(idCount, newSubtask);
        idCount++;
        Integer epicId = newSubtask.getEpicId();
        Epic epicBuffer = epicStorage.get(epicId);
        epicBuffer.setSubtaskIncludedInTheEpic(newSubtask);
        epicBuffer.updateEpicStatus();
        epicStorage.put(epicBuffer.getId(), epicBuffer);
    }

    @Override
    public void updateTask(Integer idTask, Task newTask) { // 2.5 Обновление.
        taskStorage.put(idTask, newTask);
    }

    @Override
    public void updateEpic(Integer idTask, Epic newEpic) { // 2.5 Обновление эпик-задачи.
        epicStorage.put(idTask, newEpic);
    }

    @Override
    public void updateSubtask(Integer idSubtask, Subtask newSubtask) { // 2.5 Обновление подзадачи.
        subtaskStorage.put(idSubtask, newSubtask);
        Integer epicId = newSubtask.getEpicId();
        Epic epicBuffer = epicStorage.get(epicId);
        epicBuffer.updateEpicStatus();
        epicStorage.put(epicId, epicBuffer);
    }

    @Override
    public void removeTask(Integer id) { // 2.6 Удаление задачи по идентификатору
        taskStorage.remove(id);
    }

    @Override
    public void removeEpic(Integer id) { // 2.6 Удаление эпика по идентификатору
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

    @Override
    public void removeSubtask(Integer id) { // 2.6 Удаление подзадачи по идентификатору
        Subtask subtask = subtaskStorage.get(id);
        Integer epicId = subtask.getEpicId();
        Epic epicBuffer = epicStorage.get(epicId);
        subtaskStorage.remove(id);
        epicBuffer.updateEpicStatus();
    }

    @Override
    public List<Subtask> getListOfAllSubtaskEpic(Epic epic) { // 3.1 Получение списка всех подзадач определенённого эпика
        return epic.getSubtaskList();
    }


    public List<Task> getHistory() {
        return historyManager.getHistory();
    }




}
