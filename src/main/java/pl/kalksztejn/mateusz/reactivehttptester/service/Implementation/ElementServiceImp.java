package pl.kalksztejn.mateusz.reactivehttptester.service.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kalksztejn.mateusz.reactivehttptester.model.Element;
import pl.kalksztejn.mateusz.reactivehttptester.model.SampleObject;
import pl.kalksztejn.mateusz.reactivehttptester.repository.ElementRepository;
import pl.kalksztejn.mateusz.reactivehttptester.service.Interface.ElementService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ElementServiceImp implements ElementService {

    private final ElementRepository elementRepository;

    @Autowired
    public ElementServiceImp(ElementRepository elementRepository) {
        this.elementRepository = elementRepository;
    }

    @Override
    public Flux<Element> getElements(Pageable pageable) {
        return elementRepository.findBy(pageable);
    }

    @Override
    public Mono<Element> getElement(long id) {
        return elementRepository.findById(id);
    }

    @Override
    @Transactional
    public Mono<Boolean> deleteElement(long id) {
        return elementRepository.findById(id)
                .flatMap(element -> elementRepository.delete(element).thenReturn(true))
                .defaultIfEmpty(false);
    }

    @Override
    public Mono<SampleObject> getSampleObjectByElement(long elementId) {
        return elementRepository.findById(elementId)
                .cast(Element.class)
                .flatMap(Element::getSampleObject);
    }

    @Override
    @Transactional
    public Mono<Element> updateElement(Mono<Element> element) {
        return element.flatMap(e -> elementRepository.findById(e.getElementId())
                .flatMap(existingElement -> {
                    existingElement.setName(e.getName());
                    existingElement.setDescription(e.getDescription());
                    return elementRepository.save(existingElement);
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Element not found")))
                .onErrorResume(throwable -> Mono.empty()));
    }

    @Override
    public Mono<Element> setElement(Mono<Element> element) {
        return element.flatMap(e -> {
            e.setElementId(null);
            return elementRepository.save(e);
        });
    }
}
