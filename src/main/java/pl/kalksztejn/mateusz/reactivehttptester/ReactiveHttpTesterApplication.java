package pl.kalksztejn.mateusz.reactivehttptester;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ReactiveHttpTesterApplication {
    private static ConfigurableApplicationContext applicationContext;
    public static void main(String[] args) {
        SpringApplication.run(ReactiveHttpTesterApplication.class, args);
    }
    public static void shutdown() {
        if (applicationContext != null) {
            SpringApplication.exit(applicationContext, () -> 0);
        }
    }
    // Dodajemy hook do zamknięcia aplikacji przed zakończeniem
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(ReactiveHttpTesterApplication::shutdown));
    }

}
