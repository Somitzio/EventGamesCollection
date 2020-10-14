package eventgamescollection.handlers.tasks;

import eventgamescollection.Main;
import eventgamescollection.abstracts.Base;
import org.bukkit.Bukkit;

public class TaskWrapper extends Base {
    private final Runnable runnable;
    private final String name;
    private int taskID;
    private boolean isRunning;

    public TaskWrapper(Runnable runnable, Main plugin) {
        Class<?> runnableClass = runnable.getClass();
        String classPath = runnableClass.getName();
        String[] classPathSplitted = classPath.split("\\.");
        String name = classPathSplitted[classPathSplitted.length - 1];
        this.runnable = runnable;
        this.name = name;
    }

    public TaskWrapper(Runnable runnable, String name) {
        this.runnable = runnable;
        this.name = name;
    }

    public void run(long delay, long repeatDelay) {
        if(isRunning()) {
            int taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(getPlugin(), getRunnable(), delay, repeatDelay);
            setTaskID(taskID);
            setRunning(true);
        }
    }
    
    public void stop() {
        if(isRunning()) {
            Bukkit.getScheduler().cancelTask(getTaskID());
            setRunning(false);
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public String getName() {
        return name;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public Runnable getRunnable() {
        return runnable;
    }
}
