package controllers;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
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

        taskManager = Managers.getDefault();

        historyManager = Managers.getDefaultHistory();


        task1 = new Task("Купить продукты", "Съездить в Окей", TaskStatus.NEW,
                LocalDateTime.of(2023, 3, 26, 0, 56), Duration.ofMinutes(20));
        taskManager.addTask(task1);

        task2 = new Task("Купить мебель", "Съездить в Икею", TaskStatus.NEW,
                LocalDateTime.of(2023, 3, 26, 0, 58), Duration.ofMinutes(20));
        taskManager.addTask(task2);

        epic1 = new Epic("Выполнить проектную работу Практикума"
                , "Выполнить проектную работу согласно ТЗ");
        taskManager.addEpic(epic1);

        subtask11 = new Subtask("Успешно пройти теоретический блок спринта №3"
                , "Изучить всю теорию и выполнить успешно все задачи в тренажере", TaskStatus.NEW,
                LocalDateTime.of(2023, 3, 26, 1, 1), Duration.ofMinutes(20), epic1.getId());
        taskManager.addSubtask(subtask11);

        subtask12 = new Subtask("Выполнить задачу по Cody Style"
                , "Выполнить задачу самому и провести ревью сокурсника", TaskStatus.NEW,
                LocalDateTime.of(2023, 3, 26, 1, 2), Duration.ofMinutes(20), epic1.getId());
        taskManager.addSubtask(subtask12);

        epic2 = new Epic("Изучить курс на Ютубе JAVA", "Курс Алишева");
        taskManager.addEpic(epic2);

        subtask21 = new Subtask("Просмотреть все ролики из плей-листа", "Ютуб", TaskStatus.NEW,
                LocalDateTime.of(2023, 3, 26, 1, 3), Duration.ofMinutes(20), epic2.getId());
        taskManager.addSubtask(subtask21);

    }

    @Test
    void shouldReturnEmptyHistoryTest() {
        List<Task> history = taskManager.getHistory();
        assertEquals(true, history.isEmpty());
    }

    @Test
    void shouldReturnFullHistoryWithOutDoubleTest() {

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

        assertEquals(7, taskManager.getHistory().size());



    }

    @Test
    void shouldRemoveTaskFromHistory() {

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

        assertEquals(7, taskManager.getHistory().size());

        taskManager.removeTask(task1.getId());

        assertEquals(6, taskManager.getHistory().size());

    }


    @Test
    void add() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(subtask11);

        assertEquals(3, historyManager.getHistory().size());
    }


    @Test
    void remove() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(subtask11);

        assertEquals(3, historyManager.getHistory().size());

        historyManager.remove(task1.getId());

        assertEquals(2, historyManager.getHistory().size());
    }


    @Test
    void getHistory() {
        historyManager.add(task1);
        historyManager.add(task2);

        assertEquals(List.of(task1, task2), historyManager.getHistory());
    }
}