package app.springthreadpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class GracefulShutdown {
    private final Logger logger = LoggerFactory.getLogger(GracefulShutdown.class);

    @Autowired
    private ServiceRegister serviceRegister;

    public void enable() throws NullPointerException {
        Consumer<ServiceRegister> stopServices = (servRegister) -> {
            //Shutting down every service in list
            for (TaskService ts : servRegister.getServiceList()) {
                try {
                    ts.shutdown();
                } catch (InterruptedException e) {
                    StackTraceElement[] stackTrace = e.getStackTrace();

                    for (StackTraceElement stElem : stackTrace) {
                        logger.error(stElem.toString());
                    }
                }
            }
        };

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                stopServices.accept(serviceRegister);
            }
        });
    }
}
