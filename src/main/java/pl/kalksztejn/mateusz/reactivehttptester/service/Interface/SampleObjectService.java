package pl.kalksztejn.mateusz.reactivehttptester.service.Interface;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.kalksztejn.mateusz.reactivehttptester.model.Element;
import pl.kalksztejn.mateusz.reactivehttptester.model.SampleObject;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.util.List;

public interface SampleObjectService {

    Flux<SampleObject> getSampleObjects(Pageable pageable);

    Mono<SampleObject> getSampleObject(long id);

    Mono<Boolean> deleteSampleObject(long id);

    Flux<Element> getSampleObjectElements(long id);

    Flux<Element> getSampleObjectElements(long id, Pageable pageable);

    Mono<SampleObject> setSampleObject(Mono<SampleObject> sampleObject);

    Mono<SampleObject> updateSampleObject(Mono<SampleObject> sampleObject);

}
