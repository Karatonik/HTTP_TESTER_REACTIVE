package pl.kalksztejn.mateusz.reactivehttptester.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class HelloWorldController {

    @GetMapping("/hello")
    public Mono<String> hello() {
        return Mono.just("Hello, World!");
    }
    @GetMapping("/1")
    public Mono<String> one() {
        return Mono.fromSupplier(() -> {
            StringBuilder test = new StringBuilder();
            for (long i = 0; i < 9999999; i++) {
                test.append('1');
            }
            return test.toString();
        });
    }
}
