package pl.kalksztejn.mateusz.reactivehttptester.service.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kalksztejn.mateusz.reactivehttptester.model.Element;
import pl.kalksztejn.mateusz.reactivehttptester.model.SampleObject;
import pl.kalksztejn.mateusz.reactivehttptester.model.dto.ElementDto;
import pl.kalksztejn.mateusz.reactivehttptester.model.dto.SampleObjectDto;
import pl.kalksztejn.mateusz.reactivehttptester.repository.ElementRepository;
import pl.kalksztejn.mateusz.reactivehttptester.repository.SampleObjectRepository;
import pl.kalksztejn.mateusz.reactivehttptester.service.Interface.ModelWrapper;
import reactor.core.publisher.Mono;


import java.util.Optional;

@Service
public class ModelWrapperImp implements ModelWrapper {

    private final SampleObjectRepository sampleObjectRepository;

    @Autowired
    public ModelWrapperImp(SampleObjectRepository sampleObjectRepository, ElementRepository elementRepository) {
        this.sampleObjectRepository = sampleObjectRepository;
    }

    @Override
    public Mono<Element> ElementDtoToElement(Mono<ElementDto> elementDto) {
        return elementDto.flatMap(dto -> {
            Element element = new Element();
            if (dto.getElementId() != null) {
                element.setElementId(dto.getElementId());
            }
            element.setName(dto.getName());
            element.setDescription(dto.getDescription());
            if (dto.getSampleObjectId() != null) {
                 sampleObjectRepository.findById(dto.getSampleObjectId())
                        .doOnNext(element::setSampleObject).subscribe();
            }
            element.setDataAndTimeOfCreation(dto.getDataAndTimeOfCreation());
            return Mono.just(element);
        });
    }

    @Override
    public Mono<SampleObject> SampleObjectDtoToSampleObject(Mono<SampleObjectDto> sampleObjectDto) {
        return sampleObjectDto.flatMap(dto -> {
            SampleObject sampleObject = new SampleObject();
            if (dto.getObjectId() != null) {
                sampleObject.setObjectId(dto.getObjectId());
                sampleObjectRepository.findById(dto.getObjectId()).doOnNext(object -> sampleObject.setElements(object.getElements())).subscribe();
            }
            sampleObject.setName(dto.getName());
            sampleObject.setDescription(dto.getDescription());
            sampleObject.setDataAndTimeOfCreation(dto.getDataAndTimeOfCreation());
            return Mono.just(sampleObject);
        });
    }
}
