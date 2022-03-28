package app.springthreadpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class TaskConsumer implements TaskService {
    private final ScheduledExecutorService pullTasksScheduledExecutionService;
    private final Logger logger = LoggerFactory.getLogger(TaskConsumer.class);
    private final BlockingQueue<String> tasksQueue;
    private final int maxThreads;

    @Autowired
    TaskConsumer(final BlockingQueue<String> tasksQueue, final ThreadLimit threadLimit) {
        maxThreads = threadLimit.getLimit();
        this.tasksQueue = tasksQueue;
        pullTasksScheduledExecutionService = Executors.newScheduledThreadPool(maxThreads);
    }

    public TaskService run(final long initialDelay, final long period) {
        // ScheduledExecutorService for pulling tasks from queue
        Runnable pullTask = () -> {
            String headTask = tasksQueue.poll();
            if (headTask != null)
                logger.info("Getting task: " + headTask);
        };

        logger.info("Starting task pulling service with max: "+ maxThreads + " threads.");
        pullTasksScheduledExecutionService.scheduleAtFixedRate(pullTask, initialDelay, period, TimeUnit.MILLISECONDS);

        return (TaskService) this;
    }

    public TaskService run() {
        return run(0, 150);
    }

    public void shutdown() throws InterruptedException {
        if (pullTasksScheduledExecutionService.isShutdown() || pullTasksScheduledExecutionService.isTerminated()) {
            logger.info("Pulling task service is already disabled.");
            return;
        }

        logger.info("Executing tasks in a queue...");
        while (!tasksQueue.isEmpty()) {
            Thread.sleep(1000);
        }
        logger.info("All tasks are completed. Disabling task pulling service...");

        pullTasksScheduledExecutionService.shutdown();
        logger.info("Task pulling service is disabled.");
    }
}
