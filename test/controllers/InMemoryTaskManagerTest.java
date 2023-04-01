package controllers;

import controllers.InMemoryTaskManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {


    @Override
    public void setTaskManager() {
        manager = new InMemoryTaskManager();
    }

}