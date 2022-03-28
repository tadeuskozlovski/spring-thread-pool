package app.springthreadpool;

public interface TaskService {
    public TaskService run(final long initialDelay, final long period);

    public TaskService run();

    public void shutdown() throws InterruptedException;
}
