package app.springthreadpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class TaskProducer implements TaskService {
    private AtomicLong taskCounter = new AtomicLong();
    private final ScheduledExecutorService pushTasksScheduledExecutionService;
    private final Logger logger = LoggerFactory.getLogger(TaskProducer.class);
    private final BlockingQueue<String> tasksQueue;
    private final int maxThreads;

    @Autowired
    TaskProducer(final BlockingQueue<String> tasksQueue, final ThreadLimit threadLimit) {
        maxThreads = threadLimit.getLimit();
        this.tasksQueue = tasksQueue;
        pushTasksScheduledExecutionService = Executors.newScheduledThreadPool(maxThreads);
    }

    public TaskService run(final long initialDelay, final long period) {
        // ScheduledExecutorService for pushing tasks into queue
        Runnable pushTask = () -> {
            logger.info("Pushing task: " + taskCounter);
            try {
                tasksQueue.put(taskCounter + "");
                taskCounter.incrementAndGet();
            } catch (InterruptedException e) {
                StackTraceElement[] stackTrace = e.getStackTrace();

                for (StackTraceElement stElem : stackTrace) {
                    logger.error(stElem.toString());
                }
            }
        };

        logger.info("Starting task pushing service with max: "+ maxThreads + " threads.");
        pushTasksScheduledExecutionService.scheduleAtFixedRate(pushTask, initialDelay, period, TimeUnit.MILLISECONDS);

        return (TaskService)this;
    }

    public TaskService run() {
        return run(0, 100);
    }

    public void shutdown() {
        if (pushTasksScheduledExecutionService.isShutdown() || pushTasksScheduledExecutionService.isTerminated()) {
            logger.info("Pushing task service is already disabled.");
            return;
        }

        logger.info("Disabling task pushing service...");
        pushTasksScheduledExecutionService.shutdown();
        logger.info("Task pushing service is disabled.");
    }
}
