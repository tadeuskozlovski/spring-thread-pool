package app.springthreadpool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ThreadLimit {
    private int threadLimit = 5;

    public int getLimit(){
        return threadLimit;
    }
}
