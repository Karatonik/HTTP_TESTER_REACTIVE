package pl.kalksztejn.mateusz.reactivehttptester;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.concurrent.Executors;

@Configuration
public class ReactorConfig {

    @Value("${reactor.scheduler.typ}")
    private String schedulerType;

    @Value("${reactor.scheduler.thread}")
    private int thread;

    @Value("${reactor.scheduler.cap}")
    private int cap;

    @Bean(name = "customScheduler")
    public Scheduler customScheduler() {
        if ("parallel".equals(schedulerType)) {
            return Schedulers.newParallel("scheduler", thread);
        } else if ("elastic".equals(schedulerType)) {
            return Schedulers.newBoundedElastic(thread, cap, "scheduler");
        } else {
            throw new IllegalArgumentException("Invalid schedulerType: " + schedulerType);
        }
    }
}

