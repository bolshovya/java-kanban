import controllers.InMemoryTaskManager;
import model.*;

public class Main {

    public static void main(String[] args) {

        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

        Task task1 = new Task("Купить продукты", "Съездить в Окей");
        inMemoryTaskManager.addTask(task1);

        Task task2 = new Task("Купить мебель", "Съездить в Икею");
        inMemoryTaskManager.addTask(task2);

        Epic epic1 = new Epic("Выполнить проектную работу Практикума"
                , "Выполнить проектную работу согласно ТЗ");
        inMemoryTaskManager.addEpic(epic1);

        Subtask subtask11 = new Subtask("Успешно пройти теоретический блок спринта №3"
                , "Изучить всю теорию и выполнить успешно все задачи в тренажере", epic1);
        inMemoryTaskManager.addSubtask(subtask11);

        Subtask subtask12 = new Subtask("Выполнить задачу по Cody Style"
                , "Выполнить задачу самому, и провести ревью сокурсника", epic1);
        inMemoryTaskManager.addSubtask(subtask12);

        Epic epic2 = new Epic("Изучить курс на Ютубе JAVA", "Курс Алишева");
        inMemoryTaskManager.addEpic(epic2);

        Subtask subtask21 = new Subtask("Просмотреть все ролики из плей-листа", "Ютуб", epic2);
        inMemoryTaskManager.addSubtask(subtask21);


    }
}
