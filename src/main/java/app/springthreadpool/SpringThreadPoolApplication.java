package app.springthreadpool;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class SpringThreadPoolApplication implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(GracefulShutdown.class);

    @Autowired
    private ApplicationContext applicationContext;

    public static void main(String[] args) {
        try {
            SpringApplication.run(SpringThreadPoolApplication.class, args);
        } catch (Exception e) {
            StackTraceElement[] stackTrace = e.getStackTrace();

            for (StackTraceElement stElem : stackTrace) {
                logger.error(stElem.toString());
            }
        }

    }

    @Override
    public void run(String[] args) throws Exception {
        ServiceRegister serviceRegister = applicationContext.getBean(ServiceRegister.class);
        GracefulShutdown gracefulShutdown = applicationContext.getBean(GracefulShutdown.class);

        TaskProducer taskProducer = applicationContext.getBean(TaskProducer.class);
        TaskConsumer taskConsumer = applicationContext.getBean(TaskConsumer.class);

        serviceRegister.registerService(taskProducer.run());
        serviceRegister.registerService(taskConsumer.run());
        gracefulShutdown.enable();
    }
}
