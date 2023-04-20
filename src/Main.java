import controllers.InMemoryTaskManager;
import controllers.TaskManager;
import model.Epic;
import model.Subtask;
import model.TaskStatus;
import server.KVServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        new KVServer().start();
    }
}
