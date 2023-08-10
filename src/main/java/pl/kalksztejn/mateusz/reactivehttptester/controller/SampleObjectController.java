package pl.kalksztejn.mateusz.reactivehttptester.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kalksztejn.mateusz.reactivehttptester.model.Element;
import pl.kalksztejn.mateusz.reactivehttptester.model.dto.ElementDto;
import pl.kalksztejn.mateusz.reactivehttptester.model.dto.SampleObjectDto;
import pl.kalksztejn.mateusz.reactivehttptester.service.Interface.ModelWrapper;
import pl.kalksztejn.mateusz.reactivehttptester.service.Interface.SampleObjectService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sample_object")
@CrossOrigin(origins = "*", maxAge = 7200)
public class SampleObjectController {

    private final SampleObjectService sampleObjectService;

    private final ModelWrapper modelWrapper;

    @Autowired
    public SampleObjectController(SampleObjectService sampleObjectService, ModelWrapper modelWrapper) {
        this.sampleObjectService = sampleObjectService;
        this.modelWrapper = modelWrapper;
    }

    @GetMapping()
    public Mono<ResponseEntity<?>> getSampleObjects(Pageable pageable) {
        return sampleObjectService.getSampleObjects(pageable)
                .map(SampleObjectDto::new)
                .collectList()
                .map(elements -> {
                    if (!elements.isEmpty()) {
                        return ResponseEntity.ok(elements);
                    } else {
                        return ResponseEntity.notFound().build();
                    }
                });
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<?>> getSampleObject(@PathVariable Long id) {
        return sampleObjectService.getSampleObject(id)
                .flatMap(sampleObject -> {
                    if (sampleObject != null) {
                        return Mono.just(ResponseEntity.ok(new SampleObjectDto(sampleObject)));
                    } else {
                        return Mono.just(ResponseEntity.notFound().build());
                    }
                });
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<?>> deleteSampleObject(@PathVariable Long id) {
        return sampleObjectService.deleteSampleObject(id)
                .flatMap(deleted -> {
                    if (deleted) {
                        return Mono.just(ResponseEntity.ok(true));
                    } else {
                        return Mono.just(ResponseEntity.notFound().build());
                    }
                });
    }

    @GetMapping("/elements/{id}")
    public Mono<ResponseEntity<?>> getSampleObjectElements(@PathVariable Long id) {
        return sampleObjectService.getSampleObjectElements(id)
                .collectList()
                .flatMap(elements -> {
                    if (!elements.isEmpty()) {
                        List<ElementDto> elementDtos = elements.stream()
                                .map(ElementDto::new)
                                .collect(Collectors.toList());
                        return Mono.just(ResponseEntity.ok(elementDtos));
                    } else {
                        return Mono.just(ResponseEntity.ok(null));
                    }
                });
    }


    @GetMapping("/elements/page/{id}")
    public Mono<ResponseEntity<?>> getSampleObjectElements(@PathVariable Long id, @RequestParam("page") int page, @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        System.out.println(pageable);
        return sampleObjectService.getSampleObjectElements(id, pageable)
                .collectList()
                .flatMap(elements -> {
                    if (!elements.isEmpty()) {
                        List<ElementDto> elementDTOs = elements.stream()
                                .map(ElementDto::new)
                                .toList();
                        return Mono.just(ResponseEntity.ok(elementDTOs));
                    } else {
                        return Mono.just(ResponseEntity.notFound().build());
                    }
                });
    }

    @PostMapping()
    public Mono<ResponseEntity<?>> setSampleObject(@RequestBody SampleObjectDto sampleObjectDto) {
        return sampleObjectService.setSampleObject(modelWrapper.SampleObjectDtoToSampleObject(Mono.just(sampleObjectDto)))
                .flatMap(sampleObject -> {
                    if (sampleObject != null) {
                        return Mono.just(ResponseEntity.ok(new SampleObjectDto(sampleObject)));
                    } else {
                        return Mono.just(ResponseEntity.notFound().build());
                    }
                });
    }

    @PutMapping()
    public Mono<ResponseEntity<?>> updateSampleObject(@RequestBody SampleObjectDto sampleObjectDto) {
        return sampleObjectService.updateSampleObject(modelWrapper.SampleObjectDtoToSampleObject(Mono.just(sampleObjectDto)))
                .flatMap(sampleObject -> {
                    if (sampleObject != null) {
                        return Mono.just(ResponseEntity.ok(new SampleObjectDto(sampleObject)));
                    } else {
                        return Mono.just(ResponseEntity.notFound().build());
                    }
                });
    }

}
