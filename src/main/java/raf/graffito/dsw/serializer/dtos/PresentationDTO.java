package raf.graffito.dsw.serializer.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PresentationDTO {
    private String title;
    private String description;
    private int number;
    private List<SlideDTO> slides = new ArrayList<>();
}
