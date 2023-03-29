package controllers;

import exceptions.ManagerSaveException;
import model.*;
import java.io.*;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class FileBackedTasksManager extends InMemoryTaskManager {

    final static String savePath = "src/resources/data.csv";



    public static void main(String[] args) {

        FileBackedTasksManager fileBackedTasksManager1 = new FileBackedTasksManager();

        Task task1 = new Task("Купить продукты", "Съездить в Окей", TaskStatus.NEW,
                LocalDateTime.of(2023, 3, 26, 0, 56), Duration.ofMinutes(20));
        fileBackedTasksManager1.addTask(task1);


        Task task2 = new Task("Купить мебель", "Съездить в Икею", TaskStatus.NEW,
                LocalDateTime.of(2023, 3, 26, 0, 58), Duration.ofMinutes(20));
        fileBackedTasksManager1.addTask(task2);

        Epic epic1 = new Epic("Выполнить проектную работу Практикума"
                , "Выполнить проектную работу согласно ТЗ");
        fileBackedTasksManager1.addEpic(epic1);

        Subtask subtask11 = new Subtask("Успешно пройти теоретический блок спринта №3"
                , "Изучить всю теорию и выполнить успешно все задачи в тренажере", TaskStatus.NEW,
                LocalDateTime.of(2023, 3, 26, 1, 1), Duration.ofMinutes(20), epic1.getId());
        fileBackedTasksManager1.addSubtask(subtask11);

        Subtask subtask12 = new Subtask("Выполнить задачу по Cody Style"
                , "Выполнить задачу самому и провести ревью сокурсника", TaskStatus.NEW,
                LocalDateTime.of(2023, 3, 26, 1, 2), Duration.ofMinutes(20), epic1.getId());
        fileBackedTasksManager1.addSubtask(subtask12);

        Epic epic2 = new Epic("Изучить курс на Ютубе JAVA", "Курс Алишева");
        fileBackedTasksManager1.addEpic(epic2);

        Subtask subtask21 = new Subtask("Просмотреть все ролики из плей-листа", "Ютуб", TaskStatus.NEW,
                LocalDateTime.of(2023, 3, 26, 1, 3), Duration.ofMinutes(20), epic2.getId());
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

        FileBackedTasksManager fileBackedTasksManager2 = new FileBackedTasksManager();
        fileBackedTasksManager2.loadFromFile(new File(savePath));




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
                        manager.taskStorage.put(loadTask.getId(), loadTask);
                        break;
                    case EPIC:
                        manager.epicStorage.put(loadTask.getId(), (Epic) loadTask);
                        break;
                    case SUBTASK:
                        manager.subtaskStorage.put(loadTask.getId(), (Subtask) loadTask);
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
        save();
    }

    @Override
    public void updateEpic(Integer idTask, Epic newEpic) { // 2.5 Обновление эпик-задачи.
        super.updateEpic(idTask, newEpic);
        save();
    }

    @Override
    public void updateSubtask(Integer idSubtask, Subtask newSubtask) { // 2.5 Обновление подзадачи.
        super.updateSubtask(idSubtask, newSubtask);
        save();
    }


    @Override
    public List<Task> getHistory() {
        save();
        return super.historyManager.getHistory();
    }

}
