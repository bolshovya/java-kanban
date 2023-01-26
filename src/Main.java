import controllers.TaskManager;
import model.*;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("Купить продукты", "Съездить в Окей");
        taskManager.addTask(task1);

        Task task2 = new Task("Купить мебель", "Съездить в Икею");
        taskManager.addTask(task2);

        Epic epic1 = new Epic("Выполнить проектную работу Практикума"
                , "Выполнить проектную работу согласно ТЗ");
        taskManager.addEpic(epic1);

        Subtask subtask11 = new Subtask("Успешно пройти теоретический блок спринта №3"
                , "Изучить всю теорию и выполнить успешно все задачи в тренажере", epic1);
        taskManager.addSubtask(subtask11);

        Subtask subtask12 = new Subtask("Выполнить задачу по Cody Style"
                , "Выполнить задачу самому, и провести ревью сокурсника", epic1);
        taskManager.addSubtask(subtask12);

        Epic epic2 = new Epic("Изучить курс на Ютубе JAVA", "Курс Алишева");
        taskManager.addEpic(epic2);

        Subtask subtask21 = new Subtask("Просмотреть все ролики из плей-листа", "Ютуб", epic2);
        taskManager.addSubtask(subtask21);

        System.out.println("Список задач:");
        taskManager.printListOfAllTasks(taskManager.listOfAllTask());
        System.out.println();
        System.out.println("Список эпиков:");
        taskManager.printListOfAllEpics(taskManager.listOfAllEpic());
        System.out.println();
        System.out.println("Список подзадач:");
        taskManager.printListOfAllSubtasks(taskManager.listOfAllSubtask());
        System.out.println();
        System.out.println("Первый апгрейд статусов!");
        subtask11.setStatus("IN_PROGRESS");
        subtask21.setStatus("IN_PROGRESS");
        taskManager.updateEpicStatusMax();
        System.out.println("Список задач:");
        taskManager.printListOfAllTasks(taskManager.listOfAllTask());
        System.out.println();
        System.out.println("Список эпиков:");
        taskManager.printListOfAllEpics(taskManager.listOfAllEpic());
        System.out.println();
        System.out.println("Список подзадач:");
        taskManager.printListOfAllSubtasks(taskManager.listOfAllSubtask());
        System.out.println();


        /*
        System.out.println("СОЗДАЮТСЯ НОВЫЕ ЗАДАЧИ");
        taskManager.listAllTask();
        System.out.println();
        epicManager.listAllEpic();
        System.out.println();
        epicManager.listAllSubtask();
        System.out.println("ПЕРВЫЙ АПДЕЙТ СТАТУСОВ");
        System.out.println();
        epicManager.updateSubtaskStatus(subtask11);
        epicManager.updateTaskStatus(task1);
        epicManager.updateSubtaskStatus(subtask12);
        taskManager.listAllTask();
        System.out.println();
        epicManager.listAllSubtask();
        System.out.println();
        epicManager.listAllEpic();
        System.out.println("ВТОРОЙ АПДЕЙТ СТАТУСОВ: ");
        System.out.println();
        epicManager.updateSubtaskStatus(subtask11);
        epicManager.updateTaskStatus(task1);
        epicManager.updateSubtaskStatus(subtask21);
        taskManager.listAllTask();
        System.out.println();
        epicManager.listAllSubtask();
        System.out.println();
        epicManager.listAllEpic();
        System.out.println("ТРЕТИЙ АПДЕЙТ СТАТУСОВ: ");
        epicManager.updateSubtaskStatus(subtask11);
        epicManager.updateSubtaskStatus(subtask12);
        epicManager.updateSubtaskStatus(subtask21);
        taskManager.listAllTask();
        System.out.println();
        epicManager.listAllSubtask();
        System.out.println();
        epicManager.listAllEpic();
        System.out.println("УДАЛЕНИЕ");
        taskManager.removeTask(1675990516);
        epicManager.removeEpic(79775152);
        taskManager.listAllTask();
        System.out.println();
        epicManager.listAllSubtask();
        System.out.println();
        epicManager.listAllEpic();
        */


    }
}
