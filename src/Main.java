import controllers.*;
import model.*;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        TaskManager fileBackedTasksManager1 = new InMemoryTaskManager();

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
                , "Выполнить задачу самому, и провести ревью сокурсника", epic1.getId());
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


    }
}
