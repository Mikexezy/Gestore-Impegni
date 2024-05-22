package app;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

//Classe User
public class User implements Serializable {
    private String name;
    private List<String> tasks;

    //Costruttore classe User
    public User(String name) {
        this.name = name;
        this.tasks = new ArrayList<>();
    }

    //Metodo per estrarre il nome dell'User
    public String getName() {
        return name;
    }

    //Metodo per aggiungere una Task
    public void addTask(String task) {
        tasks.add(task);
    }

    //Metodo per rimuovere una Task
    public void removeTask(String task) {
        tasks.remove(task);
    }
    
    //Metodo per estrarre la lista delle Task
    public List<String> getTasks() {
        return tasks;
    }

    //Metodo per estrarre la lista delle Task per priorità
    public List<String> getTasksSortedByPriority() {
        return tasks.stream()
                .sorted(Comparator.comparingInt(this::getPriorityValue))
                .collect(Collectors.toList());
    }

    //Metodo per estrarre il valore della priorità della Task
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
