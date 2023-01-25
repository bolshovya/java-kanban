import java.util.HashMap;

public class TaskManager {
    public HashMap<Integer, Task> taskStorage = new HashMap<>();


    public void listAllTask() { // 2.1 Получение списка всех задач.
        System.out.println("Список задач:");
        for (Task task : taskStorage.values()) {
            System.out.println(task);
        }
    }

    public void clearTaskList() { // 2.2 Удаление всех задач.
        taskStorage.clear();
        for (Task task : taskStorage.values()) { // для проверки
            System.out.println(task);
        }
        System.out.println("Список задач пуст.");
    }

    public void getById(Integer id) { // 2.3 Получение по идентификатору
        System.out.println(taskStorage.get(id));
    }

    public void addTask(Task newTask) { // 2.4 Создание. Сам объект должен передаваться в качестве параметра.
        taskStorage.put(newTask.getId(), newTask);
    }

    public void updateTask(Integer idTask, Task newTask) { // 2.5 Обновление.
        taskStorage.put(idTask, newTask);
    }

    public void removeTask(Integer id) { // 2.6 Удаление по идентификатору
        taskStorage.remove(id);
    }

    public void listIdTask() {
        for (Integer id : taskStorage.keySet()) {
            System.out.println(id);
        }
    }

}