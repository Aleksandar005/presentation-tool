package raf.graffito.dsw.serializer.dtos;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO klasa za serijalizaciju projekta.
 * Sadrži samo podatke modela, bez view komponenti.
 */
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectDTO {
    private String title;
    private String author;
    private String path;
    private List<PresentationDTO> presentations = new ArrayList<>();
    private List<SlideDTO> slides = new ArrayList<>();
    private List<String> libraryImagePaths = new ArrayList<>();
}
