package pl.kalksztejn.mateusz.reactivehttptester.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kalksztejn.mateusz.reactivehttptester.model.dto.ElementDto;
import pl.kalksztejn.mateusz.reactivehttptester.model.dto.SampleObjectDto;
import pl.kalksztejn.mateusz.reactivehttptester.service.Interface.ElementService;
import pl.kalksztejn.mateusz.reactivehttptester.service.Interface.ModelWrapper;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/element")
@CrossOrigin(origins = "*", maxAge = 7200)
public class ElementController {

    private final ElementService elementService;

    private final ModelWrapper modelWrapper;

    @Autowired
    public ElementController(ElementService elementService, ModelWrapper modelWrapper) {
        this.elementService = elementService;
        this.modelWrapper = modelWrapper;
    }

    @GetMapping
    public Mono<ResponseEntity<?>> getElements(Pageable pageable) {
        return elementService.getElements(pageable)
                .map(ElementDto::new)
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
    public Mono<ResponseEntity<ElementDto>> getElement(@PathVariable Long id) {
        return elementService.getElement(id)
                .map(element -> ResponseEntity.ok(new ElementDto(element)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<?>> deleteElement(@PathVariable Long id) {
        return elementService.deleteElement(id)
                .map(deleted -> {
                    if (deleted) {
                        return ResponseEntity.ok(true);
                    } else {
                        return ResponseEntity.notFound().build();
                    }
                });
    }

    @GetMapping("/object/{id}")
    public Mono<ResponseEntity<SampleObjectDto>> getSampleObjectByElement(@PathVariable Long id) {
        return elementService.getSampleObjectByElement(id)
                .flatMap(sampleObject -> Mono.just(ResponseEntity.ok(new SampleObjectDto(sampleObject))))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping()
    public Mono<ResponseEntity<ElementDto>> updateElement(@RequestBody ElementDto elementDto) {
        return elementService.updateElement(modelWrapper.ElementDtoToElement(Mono.just(elementDto)))
                .flatMap(updatedElement -> {
                    if (updatedElement != null) {
                        return Mono.just(ResponseEntity.ok(new ElementDto(updatedElement)));
                    } else {
                        return Mono.just(ResponseEntity.notFound().build());
                    }
                });
    }

    @PostMapping()
    public Mono<ResponseEntity<?>> setElement(@RequestBody ElementDto elementDto) {
        return elementService.setElement(modelWrapper.ElementDtoToElement(Mono.just(elementDto)))
                .flatMap(element1 -> {
                    if (element1 != null) {
                        return Mono.just(ResponseEntity.ok(new ElementDto(element1)));
                    } else {
                        return Mono.just(ResponseEntity.notFound().build());
                    }
                });
    }

}
