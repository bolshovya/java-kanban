package controllers;

import model.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {
    HashMap<Integer, Task> taskStorage = new HashMap<>();
    HashMap <Integer, Epic> epicStorage = new HashMap<>();
    HashMap <Integer, Subtask> subtaskStorage = new HashMap<>();


    public ArrayList<Task> listOfAllTask() { // 2.1 Получение списка всех задач.
        List<Task> taskList = new ArrayList<>();
        for (Task task : taskStorage.values()) {
            taskList.add(task);
        }
        return (ArrayList<Task>) taskList;
    }

    public ArrayList<Epic> listOfAllEpic() { // 2.1 Получение списка всех эпик-задач.
        List<Epic> epicList = new ArrayList<>();
        for (Epic epic : epicStorage.values()) {
            epicList.add(epic);
        }
        return (ArrayList<Epic>) epicList;
    }

    public ArrayList<Subtask> listOfAllSubtask() { // 2.1 Получение списка всех подзадач.
        List<Subtask> subtaskList = new ArrayList<>();
        for (Subtask subtask : subtaskStorage.values()) {
            subtaskList.add(subtask);
        }
        return (ArrayList<Subtask>) subtaskList;
    }

    public void printListOfAllTasks(ArrayList<Task> list) {
        for (Task task : list) {
            System.out.println(task);
        }
    }

    public void printListOfAllEpics(ArrayList<Epic> list) {
        for (Epic epic : list) {
            System.out.println(epic);
        }
    }

    public void printListOfAllSubtasks(ArrayList<Subtask> list) {
        for (Subtask subtask : list) {
            System.out.println(subtask);
        }
    }


    public void clearTaskList() { // 2.2 Удаление всех задач.
        taskStorage.clear();
    }

    public void clearEpicList() { // 2.2 Удаление всех эпик-задач.
        epicStorage.clear();
    }

    public void clearSubtaskList() { // 2.2 Удаление всех подзадач.
        subtaskStorage.clear();
    }

    public Task getTaskById(Integer id) { // 2.3 Получение по идентификатору
        Task returnedTask = taskStorage.get(id);
        return returnedTask;
    }

    public Epic getEpicById(Integer id) { // 2.3 Получение эпик-задачи по идентификатору
        Epic returnedEpic = epicStorage.get(id);
        return returnedEpic;
    }

    public Subtask getSubtaskById(Integer id) { // 2.3 Получение по идентификатору
        Subtask returnedSubtask = subtaskStorage.get(id);
        return returnedSubtask;
    }

    public void addTask(Task newTask) { // 2.4 Создание. Сам объект должен передаваться в качестве параметра.
        taskStorage.put(newTask.getId(), newTask);
    }

    public void addEpic(Epic newEpic) { // 2.4 Создание эпик-задачи. Сам объект должен передаваться в качестве параметра.
        epicStorage.put(newEpic.getId(), newEpic);
    }

    public void addSubtask(Subtask newSubtask) { // 2.4 Создание. Сам объект должен передаваться в качестве параметра.
        subtaskStorage.put(newSubtask.getId(), newSubtask);
        Integer epicId = newSubtask.getEpicId();
        Epic epicBuffer = epicStorage.get(epicId);
        epicBuffer.setSubtaskIncludedInTheEpic(newSubtask);
        epicStorage.put(epicBuffer.getId(), epicBuffer);
    }

    public void updateTask(Integer idTask, Task newTask) { // 2.5 Обновление.
        taskStorage.put(idTask, newTask);
    }

    public void updateEpic(Integer idTask, Epic newEpic) { // 2.5 Обновление эпик-задачи.
        epicStorage.put(idTask, newEpic);
    }

    public void updateSubtask(Integer idSubtask, Subtask newSubtask) { // 2.5 Обновление подзадачи.
        subtaskStorage.put(idSubtask, newSubtask);
    }

    public void removeTask(Integer id) { // 2.6 Удаление задачи по идентификатору
        taskStorage.remove(id);
    }

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

    public void removeSubtask(Integer id) { // 2.6 Удаление подзадачи по идентификатору
        subtaskStorage.remove(id);
    }

    public void getListOfAllSubtaskEpic(Epic epic) { // 3.1 Получение списка всех подзадач определенённого эпика
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


    /* По данному (updateEpicStatus()) методу ваш комментарий: "Этот метод хорошо бы разделить на два.
    Добавление подзадачи в Epic-это одно действие, а расчёт актуального статуса Эпика-это совсем другая история. "
    *
    * Я не понимаю как разделить данный метод на два. Здесь нет добавления подзадачи в Epic.
    *
    * Логика метода следующая:
    * 1. Принимаем на вход подзадачу. Т.к. подзадача хранит в себе Эпик, к которому она относится, то получаем Id этого Эпика.
    * 2. Из хранилища всех подзадач subtaskStorage выбраем подзадачи, которые относятся в Эпику из п.1.
    *    Эти подзадачи сохраняем в лист subtaskPassedEpic.
    * 3. Если в subtaskPassedEpic подзадач нет или там null, то присваиваем статус "NEW".
    * 4. Если п.3 не сработал, т.к. лист не пустой, то мы считаем статусы подзадач и на основе выводов корректируем Эпик
    * в "хранилище" Эпиков epicStorage.
    *
    * Обновлять статус Эпика имеет смысл только тогда, когда обновляется статус Подзадачи. Поэтому я решил обновлять
    * статус подзадачи так же через TaskManager'а, чтобы при обновлении статуса Подзадачи обновлять статус Эпика.
    * Таким образом пришлось написать метод updateSubtaskStatus сюда. А раз есть такой метод для Подзадачи, сделал такой
    * же метод для обычной Задачи.
    *
    * По-моему метод updateEpicStatus() удобнее нежели вызывать данный метод (добавил на всякий случай в Epic) для каждого
    * Эпика отдельно.
     */
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

    public void updateTaskStatus(Task task) {
        if (task.getStatus() == "NEW") {
            task.setStatus("IN_PROGRESS");
        } else if (task.getStatus() == "IN_PROGRESS") {
            task.setStatus("DONE");
        } else {
            task.setStatus("DONE");
        }
    }

    /* Ну или можно написать метод который ничего на вход не принимает, а проходится по всем сохраненным Эпикам
    * в epicStorage, потом собирает подходящие Подзадачи, смотрит их статусы и обновляет статусы Эпиков.
    */
    public void updateEpicStatusMax() {
        for (Epic epic : epicStorage.values()) {
            Integer passedEpic = epic.getId();
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
    }

}
