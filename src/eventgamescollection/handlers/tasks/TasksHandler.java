package eventgamescollection.handlers.tasks;

import eventgamescollection.abstracts.Base;
import eventgamescollection.exceptions.taskExceptions.TaskAlreadyExistsException;
import eventgamescollection.exceptions.taskExceptions.TaskNotFoundException;

import java.util.HashMap;

public class TasksHandler extends Base {
    private final HashMap<String, TaskWrapper> tasks;

    public TasksHandler() {
        this.tasks = new HashMap<>();
    }

    public String addRunnable(Runnable runnable) {
        TaskWrapper taskWrapper = new TaskWrapper(runnable, getPlugin());
        return addTask(taskWrapper);
    }

    public String addTask(TaskWrapper task) {
        if(task == null)
            return null;
        if(hasTask(task.getName()))
            throw new TaskAlreadyExistsException();
        String name = task.getName();
        overwriteTask(name, task);
        return name;
    }

    public void overwriteTask(String name, TaskWrapper task) {
        if(hasTask(name)) {
            getTasks().replace(name, task);
        } else {
            getTasks().put(name, task);
        }
    }

    public void runTask(String name, long delay, long repeatDelay) {
        if(hasTask(name)) {
            getTask(name).run(delay, repeatDelay);
        } else {
            throw new TaskNotFoundException();
        }
    }

    public void stopTask(String name) {
        if(hasTask(name)) {
            TaskWrapper task = getTasks().get(name);
            task.stop();
        } else {
            throw new TaskNotFoundException();
        }
    }

    public TaskWrapper getTask(String name) {
        return getTasks().get(name);
    }

    public boolean hasTask(String name) {
        return getTasks().containsKey(name);
    }

    public HashMap<String, TaskWrapper> getTasks() {
        return tasks;
    }
}
