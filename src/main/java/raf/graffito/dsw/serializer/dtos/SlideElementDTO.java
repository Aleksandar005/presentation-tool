package raf.graffito.dsw.serializer.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SlideElementDTO {
    private String type; // "image", "text", "logo"
    private String name;
    private int x;
    private int y;
    private int width;
    private int height;
    private double rotation;

    // Za tekst
    private String text;
    private String fontName;
    private int fontSize;
    private int fontStyle;
    private int colorRGB;

    // Za sliku
    private String imagePath;

    // Za logo
    private int logoColorRGB;
}
