import controllers.*;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    HistoryManager historyManager;
    TaskManager taskManager;

    Task task1;
    Task task2;;

    Epic epic1;

    Subtask subtask11;

    Subtask subtask12;

    Epic epic2;

    Subtask subtask21;


    @BeforeEach
    void beforeEach() {

        historyManager = new InMemoryHistoryManager();

        taskManager = new InMemoryTaskManager();

        task1 = new Task("Купить продукты", "Съездить в Окей", TaskStatus.NEW,
                LocalDateTime.of(2023, 3, 26, 0, 56), Duration.ofMinutes(20));

        task2 = new Task("Купить мебель", "Съездить в Икею", TaskStatus.NEW,
                LocalDateTime.of(2023, 3, 26, 0, 58), Duration.ofMinutes(20));

        epic1 = new Epic("Выполнить проектную работу Практикума"
                , "Выполнить проектную работу согласно ТЗ");

        subtask11 = new Subtask("Успешно пройти теоретический блок спринта №3"
                , "Изучить всю теорию и выполнить успешно все задачи в тренажере", TaskStatus.NEW,
                LocalDateTime.of(2023, 3, 26, 1, 1), Duration.ofMinutes(20), epic1.getId());

        subtask12 = new Subtask("Выполнить задачу по Cody Style"
                , "Выполнить задачу самому и провести ревью сокурсника", TaskStatus.NEW,
                LocalDateTime.of(2023, 3, 26, 1, 2), Duration.ofMinutes(20), epic1.getId());

        epic2 = new Epic("Изучить курс на Ютубе JAVA", "Курс Алишева");

        subtask21 = new Subtask("Просмотреть все ролики из плей-листа", "Ютуб", TaskStatus.NEW,
                LocalDateTime.of(2023, 3, 26, 1, 3), Duration.ofMinutes(20), epic2.getId());

    }

    @Test
    void shouldReturnEmptyHistoryTest() {
        List<Task> history = historyManager.getHistory();
        assertEquals(true, history.isEmpty());
    }

    @Test
    void shouldReturnFullHistoryWithOutDoubleTest() {
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addEpic(epic1);
        taskManager.addSubtask(subtask11);
        taskManager.addSubtask(subtask12);
        taskManager.addEpic(epic2);
        taskManager.addSubtask(subtask21);

        taskManager.getTaskById(task1.getId()); // 1
        taskManager.getTaskById(task2.getId()); // 2
        taskManager.getTaskById(task1.getId()); // 1
        taskManager.getTaskById(task2.getId()); // 2
        taskManager.getSubtaskById(subtask12.getId()); // 5
        taskManager.getSubtaskById(subtask12.getId()); // 5
        taskManager.getSubtaskById(subtask11.getId()); // 4
        taskManager.getSubtaskById(subtask21.getId()); // 7
        taskManager.getEpicById(epic1.getId()); // 3
        taskManager.getEpicById(epic2.getId()); // 6
        taskManager.getTaskById(task1.getId()); // 1
        taskManager.getTaskById(task2.getId()); // 2
        taskManager.getTaskById(task1.getId()); // 1

        List<Task> history = taskManager.getHistory();


        assertEquals(subtask12, history.get(0));
        assertEquals(subtask11, history.get(1));
        assertEquals(subtask21, history.get(2));
        assertEquals(epic1, history.get(3));
        assertEquals(epic2, history.get(4));
        assertEquals(task2, history.get(5));
        assertEquals(task1, history.get(6));
    }


    @Test
    void add() {

    }

    @Test
    void linkLast() {
    }

    @Test
    void remove() {
    }

    @Test
    void removeNode() {
    }

    @Test
    void getHistory() {
    }
}