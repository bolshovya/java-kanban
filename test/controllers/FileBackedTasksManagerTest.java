package controllers;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;


import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager>{

    @Override
    void setTaskManager() {
        manager = new FileBackedTasksManager();
    }

    @BeforeEach
    void beforeEach() {

        setTaskManager();

        task1 = new Task("Купить продукты", "Съездить в Окей", TaskStatus.NEW,
                LocalDateTime.of(2023, 3, 26, 0, 56), Duration.ofMinutes(20));

        task2 = new Task("Купить мебель", "Съездить в Икею", TaskStatus.NEW,
                LocalDateTime.of(2023, 3, 26, 0, 58), Duration.ofMinutes(20));

        epic1 = new Epic("Выполнить проектную работу Практикума"
                , "Выполнить проектную работу согласно ТЗ");
        manager.addEpic(epic1);

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
    void shouldReturnEmptyTaskManagerTest() {

        FileBackedTasksManager loaderManager = new FileBackedTasksManager();
        loaderManager.loadFromFile(new File("src/resources/data.csv"));

        assertEquals(true,loaderManager.listOfAllTask().isEmpty());
    }

    @Test
    void shouldReturnEmptyHistoryListTest() {

        FileBackedTasksManager loaderManager = new FileBackedTasksManager();
        loaderManager.loadFromFile(new File("src/resources/data.csv"));

        assertEquals(0, loaderManager.getHistory().size());
    }

    @Test
    void shouldReturnEpicWithoutSubtaskTest() {
        manager.addTask(task1);
        manager.addEpic(epic1);

        FileBackedTasksManager loaderManager = new FileBackedTasksManager();
        loaderManager.loadFromFile(new File("src/resources/data.csv"));

        assertEquals(0,loaderManager.getListOfAllSubtaskEpic(epic1).size());
    }






}