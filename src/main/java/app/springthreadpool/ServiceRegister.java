package app.springthreadpool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ServiceRegister {
    private final List<TaskService> serviceList = new ArrayList<>();

    public void registerService(final TaskService service) {
        serviceList.add(service);
    }

    public List<TaskService> getServiceList() {
        return serviceList;
    }
}
