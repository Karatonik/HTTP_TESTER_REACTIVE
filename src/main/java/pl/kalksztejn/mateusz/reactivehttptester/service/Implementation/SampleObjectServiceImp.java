package pl.kalksztejn.mateusz.reactivehttptester.service.Implementation;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kalksztejn.mateusz.reactivehttptester.model.Element;
import pl.kalksztejn.mateusz.reactivehttptester.model.SampleObject;
import pl.kalksztejn.mateusz.reactivehttptester.repository.ElementRepository;
import pl.kalksztejn.mateusz.reactivehttptester.repository.SampleObjectRepository;
import pl.kalksztejn.mateusz.reactivehttptester.service.Interface.SampleObjectService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class SampleObjectServiceImp implements SampleObjectService {

    private final SampleObjectRepository sampleObjectRepository;
    private final ElementRepository elementRepository;

    public SampleObjectServiceImp(SampleObjectRepository sampleObjectRepository, ElementRepository elementRepository) {
        this.sampleObjectRepository = sampleObjectRepository;
        this.elementRepository = elementRepository;
    }

    @Override
    public Flux<SampleObject> getSampleObjects(Pageable pageable) {
        return sampleObjectRepository.findBy(pageable);
    }

    @Override
    public Mono<SampleObject> getSampleObject(long id) {
        return sampleObjectRepository.findById(id);
    }

    @Override
    @Transactional
    public Mono<Boolean> deleteSampleObject(long id) {
        return sampleObjectRepository.findById(id)
                .flatMap(sampleObject -> {
                    Flux<Element> elements = sampleObject.getElements();
                    return elements.collectList()
                            .flatMap(elementList -> {
                                if (!elementList.isEmpty()) {
                                    return elementRepository.deleteAll(elements)
                                            .then(sampleObjectRepository.findById(id))
                                            .flatMap(existingSampleObject -> existingSampleObject.getElements()
                                                    .collectList()
                                                    .map(List::isEmpty)
                                                    .flatMap(isEmpty -> {
                                                        if (isEmpty) {
                                                            return sampleObjectRepository.delete(existingSampleObject)
                                                                    .thenReturn(true);
                                                        } else {
                                                            return Mono.just(false);
                                                        }
                                                    }));
                                } else {
                                    return sampleObjectRepository.delete(sampleObject)
                                            .thenReturn(true);
                                }
                            });
                })
                .defaultIfEmpty(false);
    }


    @Override
    public Flux<Element> getSampleObjectElements(long id) {
        return sampleObjectRepository.findById(id)
                .flatMapMany(sampleObject -> sampleObject!= null &&sampleObject.getElements()!= null  ? Flux.from(sampleObject.getElements()): Flux.empty())
                .switchIfEmpty(Flux.empty());
    }

    @Override
    public Flux<Element> getSampleObjectElements(long id, Pageable pageable) {
        return sampleObjectRepository.findById(id)
                .flatMapMany(sampleObject -> elementRepository.findElementsBySampleObjectId(sampleObject.getObjectId(), pageable))
                .switchIfEmpty(Flux.empty());
    }

    @Override
    public Mono<SampleObject> setSampleObject(Mono<SampleObject> sampleObject) {
        return sampleObject.flatMap(newSampleObject -> {
            newSampleObject.setObjectId(null);
            System.out.println(newSampleObject);
            Flux<Element> elements = newSampleObject.getElements();
            newSampleObject.setElements(Flux.empty());
            return sampleObjectRepository.save(newSampleObject)
                    .flatMap(savedSampleObject -> {
                        if (savedSampleObject.getElements() != null && elements != null) {
                            return elements.flatMap(element -> {
                                element.setSampleObject(savedSampleObject);
                                return elementRepository.save(element);
                            }).then(Mono.just(savedSampleObject));
                        }
                        return Mono.just(savedSampleObject);
                    });
        });
    }

    @Override
    @Transactional
    public Mono<SampleObject> updateSampleObject(Mono<SampleObject> sampleObject) {
        return sampleObject.flatMap(updatedSampleObject -> sampleObjectRepository.findById(updatedSampleObject.getObjectId())
                .flatMap(existingSampleObject -> {
                    existingSampleObject.setName(updatedSampleObject.getName());
                    existingSampleObject.setDescription(updatedSampleObject.getDescription());
                    existingSampleObject.setDataAndTimeOfCreation(updatedSampleObject.getDataAndTimeOfCreation());
                    return sampleObjectRepository.save(existingSampleObject);
                }));
    }
}
