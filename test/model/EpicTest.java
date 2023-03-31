package model;

import controllers.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    InMemoryTaskManager manager;

    Epic epic1;

    Subtask subtask1;
    Subtask subtask2;

    @BeforeEach
    void beforeEach() {
        manager = new InMemoryTaskManager();

        epic1 = new Epic("Выполнить проектную работу Практикума"
                , "Выполнить проектную работу согласно ТЗ");
        manager.addEpic(epic1);

        subtask1 = new Subtask("Успешно пройти теоретический блок спринта №3"
                , "Изучить всю теорию и выполнить успешно все задачи в тренажере", TaskStatus.NEW,
                LocalDateTime.of(2023, 3, 26, 1, 1), Duration.ofMinutes(20), epic1.getId());

        subtask2 = new Subtask("Выполнить задачу по Cody Style"
                , "Выполнить задачу самому и провести ревью сокурсника", TaskStatus.NEW,
                LocalDateTime.of(2023, 3, 26, 1, 2), Duration.ofMinutes(20), epic1.getId());

    }

    @Test
    void shouldReturnEmptySubtaskListTest() {
        assertEquals(0, epic1.getSubtaskList().size());
    }

    @Test
    void shouldReturnStatusNewTest() {
        manager.addSubtask(subtask2);

        assertEquals(TaskStatus.NEW, epic1.getStatus());
    }

    @Test
    void shouldReturnStatusDoneTest() {
        subtask1.setStatus(TaskStatus.DONE);
        manager.addSubtask(subtask1);

        assertEquals(TaskStatus.DONE, epic1.getStatus());

    }

    @Test
    void statusNewAndDoneTest() {
        subtask1.setStatus(TaskStatus.DONE);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        assertEquals(TaskStatus.IN_PROGRESS, epic1.getStatus());
    }

    @Test
    void shouldReturnStatusInProgressTest() {
        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        manager.addSubtask(subtask1);

        assertEquals(TaskStatus.IN_PROGRESS, epic1.getStatus());
    }

}