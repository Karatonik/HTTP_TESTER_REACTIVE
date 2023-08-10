package pl.kalksztejn.mateusz.reactivehttptester.service.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kalksztejn.mateusz.reactivehttptester.service.Interface.MathService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class MathServiceImp implements MathService {

    private final Semaphore semaphore = new Semaphore(1);
    private final Scheduler customScheduler;

    @Autowired
    public MathServiceImp(Scheduler customScheduler) {
        this.customScheduler = customScheduler;
    }

    @Override
    public Mono<List<Double>> calculateSqrt() {
        Flux<Double> sqrtFlux = Flux.range(1, 100)
                .onBackpressureBuffer(100)
                .map(Math::sqrt).subscribeOn(customScheduler);

        return sqrtFlux.collectList();
    }

    @Override
    public Mono<String> calculateFactorial() {
        return Flux.range(1, 100)
                .map(BigInteger::valueOf)
                .reduce(BigInteger.ONE, BigInteger::multiply)
                .then(Mono.just("ok")).subscribeOn(customScheduler);
    }

    @Override
    public Mono<String> blockingMethod(int threadPool) {
        String filename =generateRandomFileName();
        AtomicInteger counter = new AtomicInteger(0);

        return Flux.range(0, threadPool)
                .flatMap(threadIndex -> Mono.fromCallable(() -> {
                    writeToFile("Hello from Thread " + threadIndex,filename); // Zapisz do pliku
                    return "Done";
                }))
                .doOnNext(result -> counter.incrementAndGet())
                .then(Mono.fromSupplier(() -> {
                    int completedTasks = counter.get();
                    if (completedTasks == threadPool) {
                        return "Done";
                    } else {
                        return "Error";
                    }
                }))
                .doFinally(signalType -> {
                    File file = new File(filename);
                    file.delete();
                }).subscribeOn(customScheduler);
    }

    private void writeToFile(String line, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String generateRandomFileName() {
        UUID uuid = UUID.randomUUID();
        return uuid +"txt";
    }
}
