package app;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

public class User implements Serializable {
    private String name;
    private List<String> tasks;

    public User(String name) {
        this.name = name;
        this.tasks = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addTask(String task) {
        tasks.add(task);
    }

    public void removeTask(String task) {
        tasks.remove(task);
    }

    public List<String> getTasks() {
        return tasks;
    }

    public List<String> getTasksSortedByPriority() {
        return tasks.stream()
                .sorted(Comparator.comparingInt(this::getPriorityValue))
                .collect(Collectors.toList());
    }

    private int getPriorityValue(String task) {
        if (task.contains("[Alta]")) {
            return 1;
        } else if (task.contains("[Media]")) {
            return 2;
        } else if (task.contains("[Bassa]")) {
            return 3;
        }
        return 4;
    }

    @Override
    public String toString() {
        return name;
    }
}
