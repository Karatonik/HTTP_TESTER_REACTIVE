package pl.kalksztejn.mateusz.reactivehttptester.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Objects;

@Table(name = "element")

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Element {

    @Id
    @Column("id")
    private Long elementId;

    @Column("name")
    private String name;

    @Column
    private String description;

    @Column
    private LocalDateTime dataAndTimeOfCreation;

    @Transient
    private Mono<SampleObject> sampleObject;

    @Column("sample_object")
    private Long sampleObjectId;

    public void setSampleObject(SampleObject sampleObject) {
        this.sampleObject = Mono.just(sampleObject);
    }
    public Mono<SampleObject> getSampleObject (){
        return this.sampleObject;
    }

    public Element(String name, String description, LocalDateTime dataAndTimeOfCreation, Mono<SampleObject> sampleObject) {
        this.name = name;
        this.description = description;
        this.dataAndTimeOfCreation = dataAndTimeOfCreation;
        this.sampleObject = sampleObject;
        this.sampleObjectId = Objects.requireNonNull(this.sampleObject.block()).getObjectId();
    }
}
