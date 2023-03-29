import controllers.InMemoryTaskManager;
import controllers.TaskManager;
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

public abstract class TaskManagerTest<T extends TaskManager> {

    T manager;

    Task task1;
    Task task2;;

    Epic epic1;

    Subtask subtask11;

    Subtask subtask12;

    Epic epic2;

    Subtask subtask21;

    abstract void setTaskManager();

    @BeforeEach
    void beforeEach() {

        setTaskManager();

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
    void listOfAllTaskTest() {
        manager.addTask(task1);
        manager.addTask(task2);

        assertEquals(List.of(task1,task2), manager.listOfAllTask());
    }

    @Test
    void listOfAllEpicTest() {
        manager.addEpic(epic1);
        manager.addEpic(epic2);

        assertEquals(List.of(epic1,epic2), manager.listOfAllEpic());
    }

    @Test
    void listOfAllSubtaskTest() {
        manager.addSubtask(subtask11);
        manager.addSubtask(subtask12);

        assertEquals(List.of(subtask11, subtask12), manager.listOfAllSubtask());
    }

    @Test
    void clearTaskListTest() {
        manager.addTask(task1);
        manager.addTask(task2);
        manager.clearTaskList();

        assertNull(manager.listOfAllTask());
    }

    @Test
    void clearEpicListTest() {
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        manager.clearEpicList();

        assertNull(manager.listOfAllEpic());
    }

    @Test
    void clearSubtaskListTest() {
        manager.addSubtask(subtask11);
        manager.addSubtask(subtask12);
        manager.clearSubtaskList();

        assertNull(manager.listOfAllSubtask());
    }

    @Test
    void getTaskByIdTest() {
        assertNull(manager.getTaskById(task1.getId()));
        manager.addTask(task1);

        assertEquals(task1, manager.getTaskById(task1.getId()));
    }

    @Test
    void getEpicByIdTest() {
        assertNull(manager.getEpicById(epic1.getId()));
        manager.addEpic(epic1);

        assertEquals(epic1, manager.getEpicById(epic1.getId()));
    }

    @Test
    void getSubtaskByIdTest() {
        assertNull(manager.getSubtaskById(subtask11.getId()));
        manager.addSubtask(subtask11);

        assertEquals(subtask11, manager.getSubtaskById(subtask11.getId()));
    }

    @Test
    void addTaskTest() {
        manager.addTask(task1);
        assertFalse(manager.listOfAllTask().isEmpty());
    }

    @Test
    void addEpicTest() {
        manager.addEpic(epic1);
        assertFalse(manager.listOfAllEpic().isEmpty());
    }

    @Test
    void addSubtaskTest() {
        manager.addSubtask(subtask11);
        assertFalse(manager.listOfAllSubtask().isEmpty());
    }

    @Test
    void updateTaskTest() {
        manager.addTask(task1);
        Task updateTask1 = new Task ("Update task1 name", "Update task1 description");
        manager.updateTask(task1.getId(), updateTask1);

        assertEquals(task1, updateTask1);
    }

    @Test
    void updateEpicTest() {
        manager.addEpic(epic1);
        Epic updateEpic1 = new Epic("Update epic1 name", "Update epic1 description");
        manager.updateEpic(epic1.getId(), updateEpic1);

        assertEquals(epic1, updateEpic1);
    }

    @Test
    void updateSubtaskTest() {
        manager.addSubtask(subtask11);
        Subtask updateSubtask11 = new Subtask("Update subtask11 name", "Update subtask11 description");
        manager.updateSubtask(subtask11.getId(), updateSubtask11);

        assertEquals(subtask11, updateSubtask11);

    }

    @Test
    void removeTaskTest() {
        manager.addTask(task1);
        assertEquals(true, manager.getTaskById(task1.getId()));

        manager.removeTask(task1.getId());
        assertEquals(false, manager.getTaskById(task1.getId()));
    }

    @Test
    void removeEpicTest() {
        manager.addEpic(epic1);
        assertEquals(true, manager.getEpicById(epic1.getId()));

        manager.removeEpic(epic1.getId());
        assertEquals(false, manager.getEpicById(epic1.getId()));
    }


    @Test
    void removeSubtaskTest() {
        manager.addSubtask(subtask11);
        assertEquals(true, manager.getSubtaskById(subtask11.getId()));

        manager.removeSubtask(subtask11.getId());
        assertEquals(false, manager.getSubtaskById(subtask11.getId()));
    }

    @Test
    void getListOfAllSubtaskEpicTest() {
        assertEquals((List.of(subtask11, subtask12)), epic1.getSubtaskList());
    }

    @Test
    void getHistoryTest() {
        assertEquals(List.of(5,4,7,3,6,2,1), manager.getHistory());
    }
}