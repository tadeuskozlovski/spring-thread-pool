package app.springthreadpool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class TaskQueue {
    BlockingQueue<String> tasksQueue;

    TaskQueue(){
        tasksQueue = new LinkedBlockingQueue();
    }

    @Bean
    public BlockingQueue<String> getQueue(){
        return tasksQueue;
    }
}
