package raf.graffito.dsw.serializer;

import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.image.ImageLibrary;
import raf.graffito.dsw.image.ImageProxy;
import raf.graffito.dsw.message.Message;
import raf.graffito.dsw.message.MessageType;
import raf.graffito.dsw.model.graff.GraffNode;
import raf.graffito.dsw.model.implementation.Presentation;
import raf.graffito.dsw.model.implementation.Project;
import raf.graffito.dsw.model.implementation.slide.*;
import raf.graffito.dsw.serializer.dtos.PresentationDTO;
import raf.graffito.dsw.serializer.dtos.ProjectDTO;
import raf.graffito.dsw.serializer.dtos.SlideDTO;
import raf.graffito.dsw.serializer.dtos.SlideElementDTO;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

// Mapper koji konvertuje između Project modela i ProjectDTO za serijalizaciju.
public class ProjectMapper {

    public static ProjectDTO toDTO(Project project) {
        ProjectDTO dto = new ProjectDTO();

        dto.setTitle(project.getTitle());
        dto.setAuthor(project.getAuthor());

        for (GraffNode child : project.getChildren()) {
            if (child instanceof Presentation presentation) {
                dto.getPresentations().add(toPresentationDTO(presentation));
            }
            if (child instanceof Slide slide) {
                dto.getSlides().add(toSlideDTO(slide));
            }
        }

        // Sačuvaj putanje slika iz biblioteke
        ImageLibrary library = ApplicationFramework.getInstance().getImageLibrary();
        for (ImageProxy proxy : library.getImages(project)) {
            dto.getLibraryImagePaths().add(proxy.getFilePath());
        }

        return dto;
    }

    private static PresentationDTO toPresentationDTO(Presentation presentation) {
        PresentationDTO dto = new PresentationDTO();
        dto.setTitle(presentation.getTitle());
        dto.setDescription(presentation.getDescription());
        dto.setNumber(presentation.getSlideNumber());

        for(GraffNode child : presentation.getChildren()){
            if(child instanceof Slide slide){
                dto.getSlides().add(toSlideDTO(slide));
            }
        }

        return dto;
    }

    private static SlideDTO toSlideDTO(Slide slide) {
        SlideDTO dto = new SlideDTO();
        dto.setTitle(slide.getTitle());

        for(GraffNode child : slide.getChildren()){
            if(child instanceof SlideElement slideElement){
                dto.getElements().add(toElementDTO(slideElement));
            }
        }

        return dto;
    }

    private static SlideElementDTO toElementDTO(SlideElement element) {
        SlideElementDTO dto = new SlideElementDTO();

        dto.setName(element.getTitle());
        dto.setX(element.getLocation().x);
        dto.setY(element.getLocation().y);
        dto.setWidth(element.getDimension().width);
        dto.setHeight(element.getDimension().height);
        dto.setRotation(element.getRotation());

        switch (element) {
            case TextSlideElement text -> {
                dto.setType("text");
                dto.setText(text.getText());
                dto.setFontName(text.getFont().getFontName());
                dto.setFontSize(text.getFont().getSize());
                dto.setFontStyle(text.getFont().getStyle());
                dto.setColorRGB(text.getColor().getRGB());
            }
            case ImageSlideElement image -> {
                dto.setType("image");
                dto.setImagePath(image.getPath());
            }
            case LogoSlideElement logo -> {
                dto.setType("logo");
                dto.setLogoColorRGB(logo.getPrimaryColor().getRGB());
            }
            default -> {
                ApplicationFramework.getInstance().getMessageGenerator().notify(
                        new Message("ERROR LOADING THE PROJECT", MessageType.ERROR, element)
                );
            }
        }

        return dto;
    }

    // OBRNUTA KONVERZIJA

    public static Project fromDTO(ProjectDTO dto, GraffNode parent) {
        Project project = new Project(parent, dto.getTitle());
        project.setAuthor(dto.getAuthor());

        for (PresentationDTO presDTO : dto.getPresentations()) {
            Presentation presentation = fromPresentationDTO(presDTO, project);
            project.addChild(presentation);
        }

        for (SlideDTO slideDTO : dto.getSlides()) {
            Slide slide = fromSlideDTO(slideDTO, project);
            project.addChild(slide);
        }

        // Učitaj slike u biblioteku
        if (dto.getLibraryImagePaths() != null) {
            ImageLibrary library = ApplicationFramework.getInstance().getImageLibrary();
            for (String imagePath : dto.getLibraryImagePaths()) {
                if (imagePath != null && !imagePath.isEmpty()) {
                    // Proveri da li fajl postoji
                    if (new File(imagePath).exists()) {
                        ImageProxy proxy = new ImageProxy(imagePath);
                        library.addImage(project, proxy);
                    } else {
                        ApplicationFramework.getInstance().getMessageGenerator().notify(
                                new Message("LIBRARY IMAGE NOT FOUND: " + imagePath, MessageType.WARNING, project)
                        );
                    }
                }
            }
        }

        return project;
    }

    private static Presentation fromPresentationDTO(PresentationDTO dto, Project parent) {
        Presentation presentation = new Presentation(parent, dto.getTitle(), dto.getDescription());
        presentation.setSlideNumber(dto.getNumber());

        for (SlideDTO slideDTO : dto.getSlides()) {
            Slide slide = fromSlideDTO(slideDTO, presentation);
            presentation.addChild(slide);
        }

        return presentation;
    }

    private static Slide fromSlideDTO(SlideDTO dto, GraffNode parent) {
        Slide slide = new Slide(parent, dto.getTitle());

        for (SlideElementDTO elemDTO : dto.getElements()) {
            SlideElement element = fromElementDTO(elemDTO, slide);
            if (element != null) {
                slide.addChild(element);
            }
        }

        return slide;
    }

    private static SlideElement fromElementDTO(SlideElementDTO dto, Slide parent){
        Point location = new Point(dto.getX(), dto.getY());
        Dimension dimension = new Dimension(dto.getWidth(), dto.getHeight());

        SlideElement element = null;

        switch (dto.getType()) {
            case "text" -> {
                Font font = new Font(dto.getFontName(), dto.getFontStyle(), dto.getFontSize());
                Color color = new Color(dto.getColorRGB());
                element = new TextSlideElement(parent, dto.getName(), dto.getText(), location, font, color);
            }
            case "image" -> {
                try {
                    File file = new File(dto.getImagePath());
                    if (!file.exists()) {
                        ApplicationFramework.getInstance().getMessageGenerator().notify(
                                new Message("IMAGE FILE NOT FOUND: " + dto.getImagePath(), MessageType.ERROR, parent)
                        );
                        break;
                    }
                    BufferedImage image = ImageIO.read(file);
                    if (image == null) {
                        ApplicationFramework.getInstance().getMessageGenerator().notify(
                                new Message("CANNOT READ IMAGE: " + dto.getImagePath(), MessageType.ERROR, parent)
                        );
                        break;
                    }
                    element = new ImageSlideElement(parent, dto.getName(), image, location);
                    ((ImageSlideElement) element).setPath(dto.getImagePath());  // DODATO - postavi path
                } catch (Exception e) {
                    ApplicationFramework.getInstance().getMessageGenerator().notify(
                            new Message("ERROR LOADING IMAGE: " + dto.getImagePath() + " - " + e.getMessage(), MessageType.ERROR, parent)
                    );
                }
            }
            case "logo" -> {
                Color logoColor = new Color(dto.getLogoColorRGB());
                element = new LogoSlideElement(parent, dto.getName(), location, dimension, logoColor);
            }
        }

        if (element != null) {
            element.setDimension(dimension);
            element.rotate(dto.getRotation());
        }

        return element;
    }
}
