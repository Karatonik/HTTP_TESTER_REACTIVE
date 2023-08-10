package pl.kalksztejn.mateusz.reactivehttptester.service.Interface;

import reactor.core.publisher.Mono;

import java.util.List;

public interface MathService {

    Mono<List<Double>> calculateSqrt();

    Mono<String> calculateFactorial();

    Mono<String> blockingMethod(int threadPool);
}
