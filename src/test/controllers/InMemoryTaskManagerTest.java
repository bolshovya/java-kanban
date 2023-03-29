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

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {


    @Override
    public void setTaskManager() {
        manager = new InMemoryTaskManager();
    }

    @Test
    void getPrioritizedTasksTest() {
    }

    @Test
    void listOfAllTaskTest() {
    }

    @Test
    void listOfAllEpicTest() {
    }

    @Test
    void listOfAllSubtaskTest() {
    }

    @Test
    void clearTaskListTest() {
    }

    @Test
    void clearEpicListTest() {
    }

    @Test
    void clearSubtaskListTest() {
    }

    @Test
    void getTaskByIdTest() {
    }

    @Test
    void getEpicByIdTest() {
    }

    @Test
    void getSubtaskByIdTest() {
    }

    @Test
    void addTaskTest() {
    }

    @Test
    void addEpicTest() {
    }

    @Test
    void addSubtaskTest() {
    }

    @Test
    void getNewId() {
    }

    @Test
    void updateTaskTest() {
    }

    @Test
    void updateEpicTest() {
    }

    @Test
    void updateSubtaskTest() {
    }

    @Test
    void removeTaskTest() {
    }

    @Test
    void removeEpicTest() {
    }

    @Test
    void removeSubtaskTest() {
    }

    @Test
    void getListOfAllSubtaskEpicTest() {
    }

    @Test
    void getHistoryTest() {
    }
}