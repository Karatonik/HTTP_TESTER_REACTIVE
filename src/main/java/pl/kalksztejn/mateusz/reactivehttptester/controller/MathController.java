package pl.kalksztejn.mateusz.reactivehttptester.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kalksztejn.mateusz.reactivehttptester.service.Interface.MathService;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/math")
@CrossOrigin(origins = "*", maxAge = 7200)
public class MathController {

    private final MathService mathService;

    @Autowired
    public MathController(MathService mathService) {
        this.mathService = mathService;
    }

    @GetMapping("sqrt")
    public Mono<ResponseEntity<List<Double>>> calculateSqrt() {
        return mathService.calculateSqrt()
                .map(result -> ResponseEntity.ok().body(result))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("fact")
    public Mono<ResponseEntity<String>> calculateFactorial() {
        return mathService.calculateFactorial()
                .map(result -> ResponseEntity.ok().body(result))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @GetMapping("blocking/{threadPool}")
    public Mono<ResponseEntity<String>> blockingMethod(@PathVariable int threadPool) {
        return mathService.blockingMethod(threadPool)
                .map(result -> ResponseEntity.ok().body(result))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


}
