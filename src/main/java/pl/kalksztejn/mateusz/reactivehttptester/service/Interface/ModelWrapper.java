package pl.kalksztejn.mateusz.reactivehttptester.service.Interface;


import pl.kalksztejn.mateusz.reactivehttptester.model.Element;
import pl.kalksztejn.mateusz.reactivehttptester.model.SampleObject;
import pl.kalksztejn.mateusz.reactivehttptester.model.dto.ElementDto;
import pl.kalksztejn.mateusz.reactivehttptester.model.dto.SampleObjectDto;
import reactor.core.publisher.Mono;

public interface ModelWrapper {

    Mono<Element> ElementDtoToElement(Mono<ElementDto> elementDto);

    Mono<SampleObject> SampleObjectDtoToSampleObject(Mono<SampleObjectDto> sampleObjectDto);
}
