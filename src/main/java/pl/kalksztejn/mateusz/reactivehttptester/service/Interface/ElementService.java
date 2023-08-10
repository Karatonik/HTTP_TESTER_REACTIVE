package pl.kalksztejn.mateusz.reactivehttptester.service.Interface;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.kalksztejn.mateusz.reactivehttptester.model.Element;
import pl.kalksztejn.mateusz.reactivehttptester.model.SampleObject;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface ElementService {

    Flux<Element> getElements(Pageable pageable);
    Mono<Element> getElement(long id);

    Mono<Boolean> deleteElement(long id);

    Mono<SampleObject> getSampleObjectByElement(long elementId);

    Mono<Element> updateElement(Mono<Element> element);

    Mono<Element> setElement(Mono<Element> element);
}
