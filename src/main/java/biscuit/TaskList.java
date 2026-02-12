package biscuit;

import java.util.ArrayList;
import java.util.List;

public class TaskList {

    private final List<Task> tasks;

    public TaskList() {
        tasks = new ArrayList<>();
    }

    public TaskList(List<Task> loadedTasks) {
        assert loadedTasks != null : "Loaded task list should not be null";
        for (Task t : loadedTasks) {
            assert t != null : "Loaded task list should not contain null tasks";
        }
        
        tasks = new ArrayList<>(loadedTasks);
    }

    public int size() {
        return tasks.size();
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    public Task get(int index) {
        assert index >= 0 && index < tasks.size() : "get(): index out of bounds: " + index;

        return tasks.get(index);
    }

    public void add(Task task) {
        assert task != null : "Cannot add a null task";

        tasks.add(task);
    }

    public Task remove(int index) {
        return tasks.remove(index);
    }

    public List<Task> asList() {
        return List.copyOf(tasks);
    }
    
    public java.util.List<Task> find(String keyword) {
        assert keyword != null : "Find keyword should not be null";

        java.util.List<Task> matches = new java.util.ArrayList<>();
        String needle = keyword.toLowerCase();

        for (Task task : tasks) { // replace "tasks" with your internal list field name
            if (task.getDescription().toLowerCase().contains(needle)) {
                matches.add(task);
            }
        }
        return matches;
    }
}
