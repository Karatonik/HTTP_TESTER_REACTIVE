package pl.kalksztejn.mateusz.reactivehttptester.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

@Table(name = "sample_object")
public class SampleObject {
    @Id
    @Column("id")
    private Long objectId;

    @Column("name")
    private String name;

    @Column("description")
    private String description;
    @Column
    private LocalDateTime dataAndTimeOfCreation;


    @Transient
    private Flux<Element> elements;


    public void setElements(Flux<Element> elements) {
        this.elements = elements;
    }

    public SampleObject(String name, String description, LocalDateTime dataAndTimeOfCreation, Flux<Element> elements) {
        this.name = name;
        this.description = description;
        this.dataAndTimeOfCreation = dataAndTimeOfCreation;
        this.elements = elements;
    }


    public SampleObject(String name, String description, LocalDateTime dataAndTimeOfCreation) {
        this.name = name;
        this.description = description;
        this.dataAndTimeOfCreation = dataAndTimeOfCreation;
        this.elements = Flux.empty();
    }
}
