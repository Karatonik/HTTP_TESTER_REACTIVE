package pl.kalksztejn.mateusz.reactivehttptester.model.dto;

import lombok.*;
import pl.kalksztejn.mateusz.reactivehttptester.model.SampleObject;

import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SampleObjectDto {
    private Long objectId;
    private String name;
    private String description;
    private LocalDateTime dataAndTimeOfCreation;

    public SampleObjectDto(String name, String description, LocalDateTime dataAndTimeOfCreation) {
        this.name = name;
        this.description = description;
        this.dataAndTimeOfCreation = dataAndTimeOfCreation;
    }
    public SampleObjectDto(SampleObject sampleObject){
        this.objectId = sampleObject.getObjectId();
        this.name = sampleObject.getName();
        this.description = sampleObject.getDescription();
        this.dataAndTimeOfCreation = sampleObject.getDataAndTimeOfCreation();
    }
}
