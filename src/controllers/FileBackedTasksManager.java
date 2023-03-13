package controllers;

import exceptions.ManagerSaveException;
import model.*;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static model.TaskType.*;


public class FileBackedTasksManager extends InMemoryTaskManager {

    final static String savePath = "/home/bolshovya/dev/java-kanban/data.csv";



    public static void main(String[] args) {

        FileBackedTasksManager fileBackedTasksManager1 = new FileBackedTasksManager();

        Task task1 = new Task("Купить продукты", "Съездить в Окей");
        fileBackedTasksManager1.addTask(task1);

        Task task2 = new Task("Купить мебель", "Съездить в Икею");
        fileBackedTasksManager1.addTask(task2);

        Epic epic1 = new Epic("Выполнить проектную работу Практикума"
                , "Выполнить проектную работу согласно ТЗ");
        fileBackedTasksManager1.addEpic(epic1);

        Subtask subtask11 = new Subtask("Успешно пройти теоретический блок спринта №3"
                , "Изучить всю теорию и выполнить успешно все задачи в тренажере", epic1.getId());
        fileBackedTasksManager1.addSubtask(subtask11);

        Subtask subtask12 = new Subtask("Выполнить задачу по Cody Style"
                , "Выполнить задачу самому и провести ревью сокурсника", epic1.getId());
        fileBackedTasksManager1.addSubtask(subtask12);

        Epic epic2 = new Epic("Изучить курс на Ютубе JAVA", "Курс Алишева");
        fileBackedTasksManager1.addEpic(epic2);

        Subtask subtask21 = new Subtask("Просмотреть все ролики из плей-листа", "Ютуб", epic2.getId());
        fileBackedTasksManager1.addSubtask(subtask21);


        fileBackedTasksManager1.getTaskById(task1.getId()); // 1
        fileBackedTasksManager1.getTaskById(task2.getId()); // 2
        fileBackedTasksManager1.getTaskById(task1.getId()); // 1
        fileBackedTasksManager1.getTaskById(task2.getId()); // 2
        fileBackedTasksManager1.getSubtaskById(subtask12.getId()); // 5
        fileBackedTasksManager1.getSubtaskById(subtask12.getId()); // 5
        fileBackedTasksManager1.getSubtaskById(subtask11.getId()); // 4
        fileBackedTasksManager1.getSubtaskById(subtask21.getId()); // 7
        fileBackedTasksManager1.getEpicById(epic1.getId()); // 3
        fileBackedTasksManager1.getEpicById(epic2.getId()); // 6
        fileBackedTasksManager1.getTaskById(task1.getId()); // 1
        fileBackedTasksManager1.getTaskById(task2.getId()); // 2
        fileBackedTasksManager1.getTaskById(task1.getId()); // 1

        fileBackedTasksManager1.getHistory();

        List<Task> list = fileBackedTasksManager1.getHistory();

        for (Task task : list) {
            System.out.println(task.getId());
        }

        /*
        5
        4
        7
        3
        6
        2
        1
         */


        FileBackedTasksManager fileBackedTasksManager2 = new FileBackedTasksManager();
        // fileBackedTasksManager2.loadFromFile(new File(savePath));


    }


    public void save() {
        try {
            File file = new File(savePath);
            PrintWriter pw = new PrintWriter(file);
            pw.println("id,type,name,status,description,epic");
            for (Task task : taskStorage.values()) {
                pw.println(task.saveTaskToString());
            }
            for (Epic epic : epicStorage.values()) {
                pw.println(epic.saveTaskToString());
            }
            for (Subtask subtask : subtaskStorage.values()) {
                pw.println(subtask.saveTaskToString());
            }
            pw.println();
            pw.println(historyToString(historyManager));
            pw.close();
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }


    public static FileBackedTasksManager loadFromFile(File file) {
        try {
            FileBackedTasksManager manager = new FileBackedTasksManager();
            String content = Files.readString(file.toPath());
            String [] lines = content.split("\n");
            for (int i = 1; i < lines.length - 2; i++) {
                String lineTask = lines[i];
                Task loadTask = Task.fromString(lineTask);
                switch (loadTask.getType()) {
                    case TASK:
                        manager.addTask(loadTask);
                        break;
                    case EPIC:
                        manager.addEpic((Epic) loadTask);
                        break;
                    case SUBTASK:
                        manager.addSubtask((Subtask) loadTask);
                        break;
                    default:
                        break;
                }
            }
            String idString = lines[lines.length-1];
            List<Integer> idList = historyFromString(idString);
            for (Integer id : idList) {
                if (manager.taskStorage.containsKey(id)) {
                    Task loadTask = manager.taskStorage.get(id);
                    manager.historyManager.add(loadTask);
                } else if (manager.epicStorage.containsKey(id)) {
                    Epic loadEpic = manager.epicStorage.get(id);
                    manager.historyManager.add(loadEpic);
                } else if (manager.subtaskStorage.containsKey(id)) {
                    Subtask loadSubtask = manager.subtaskStorage.get(id);  // manager.getSubtaskById(id);
                    manager.historyManager.add(loadSubtask);
                }
            }
            return manager;
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }


    public static String historyToString(HistoryManager manager) {
        List<Task> tasks = manager.getHistory();
        String result = "";
        for (int i = 0; i < tasks.size(); i++) {
            result = result + (tasks.get(i).getId() + (i<tasks.size()-1 ? "," : ""));
        }
        return result;
    }


    public static List<Integer> historyFromString(String value) {
        List<Integer> history = new ArrayList<>();
        String[] ids = value.split(",");
        for (int i = 0; i < ids.length; i++) {
            history.add(Integer.parseInt(ids[i]));
        }
        return history;
    }


    @Override
    public List<Task> listOfAllTask() { // 2.1 Получение списка всех задач.
        return super.listOfAllTask();
    }

    @Override
    public List<Epic> listOfAllEpic() { // 2.1 Получение списка всех эпик-задач.
        return super.listOfAllEpic();
    }

    @Override
    public List<Subtask> listOfAllSubtask() { // 2.1 Получение списка всех подзадач.
        return super.listOfAllSubtask();
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
        return super.getTaskById(id);
    }

    @Override
    public Epic getEpicById(Integer id) { // 2.3 Получение эпик-задачи по идентификатору
        return super.getEpicById(id);
    }

    @Override
    public Subtask getSubtaskById(Integer id) { // 2.3 Получение подзадачи по идентификатору
        return super.getSubtaskById(id);
    }

    @Override
    public void addTask(Task newTask) { // 2.4 Создание. Сам объект должен передаваться в качестве параметра.
        super.addTask(newTask);
        save();
    }

    @Override
    public void addEpic(Epic newEpic) { // 2.4 Создание эпик-задачи. Сам объект должен передаваться в качестве параметра.
        super.addEpic(newEpic);
        save();
    }

    @Override
    public void addSubtask(Subtask newSubtask) { // 2.4 Создание. Сам объект должен передаваться в качестве параметра.
        super.addSubtask(newSubtask);
        save();
    }

    @Override
    public void updateTask(Integer idTask, Task newTask) { // 2.5 Обновление.
        super.updateTask(idTask, newTask);
    }

    @Override
    public void updateEpic(Integer idTask, Epic newEpic) { // 2.5 Обновление эпик-задачи.
        super.updateEpic(idTask, newEpic);
    }

    @Override
    public void updateSubtask(Integer idSubtask, Subtask newSubtask) { // 2.5 Обновление подзадачи.
        super.updateSubtask(idSubtask, newSubtask);
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
        return super.getListOfAllSubtaskEpic(epic);
    }

    @Override
    public List<Task> getHistory() {
        save();
        return super.historyManager.getHistory();
    }





    /*
    @Override
    public void clearTaskList() {
        super.clearTaskList();
        save();
    }

    @Override
    public void clearEpicList() {
        super.clearEpicList();
        save();
    }

    @Override
    public void clearSubtaskList() {
        super.clearSubtaskList();
        save();
    }


    @Override
    public void addTask(Task newTask) {
        super.addTask(newTask);
        save();
    }

    @Override
    public void addEpic(Epic newEpic) {
        super.addEpic(newEpic);
        save();
    }

    @Override
    public void addSubtask(Subtask newSubtask) {
        super.addSubtask(newSubtask);
        save();
    }

    @Override
    public Task getTaskById(Integer id) {
        save();
        return super.getTaskById(id);
    }

    @Override
    public Epic getEpicById(Integer id) {
        save();
        return super.getEpicById(id);
    }

    @Override
    public Subtask getSubtaskById(Integer id) {
        save();
        return super.getSubtaskById(id);
    }

    @Override
    public void updateTask(Integer idTask, Task newTask) {
        super.updateTask(idTask, newTask);
        save();
    }

    @Override
    public void updateEpic(Integer idTask, Epic newEpic) {
        super.updateEpic(idTask, newEpic);
        save();
    }

    @Override
    public void updateSubtask(Integer idSubtask, Subtask newSubtask) {
        super.updateSubtask(idSubtask, newSubtask);
        save();
    }

     */





}
