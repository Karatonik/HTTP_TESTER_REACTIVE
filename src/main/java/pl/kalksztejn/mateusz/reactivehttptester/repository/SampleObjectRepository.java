package pl.kalksztejn.mateusz.reactivehttptester.repository;


import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import pl.kalksztejn.mateusz.reactivehttptester.model.SampleObject;
import reactor.core.publisher.Flux;

public interface SampleObjectRepository extends ReactiveCrudRepository<SampleObject, Long> {
    Flux<SampleObject> findBy(Pageable pageable);
}
