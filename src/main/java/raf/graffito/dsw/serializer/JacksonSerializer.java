package raf.graffito.dsw.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.message.Message;
import raf.graffito.dsw.message.MessageType;
import raf.graffito.dsw.model.implementation.Project;
import raf.graffito.dsw.serializer.dtos.ProjectDTO;

import java.io.File;
import java.io.IOException;

public class JacksonSerializer implements Serializer {
    private final ObjectMapper objectMapper;

    public JacksonSerializer() {
        objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Override
    public Project loadProject(File file){
        try {
            ProjectDTO dto = objectMapper.readValue(file, ProjectDTO.class);
            return ProjectMapper.fromDTO(dto, null);
        } catch (IOException e){
            ApplicationFramework.getInstance().getMessageGenerator().notify(
                    new Message("CANNOT LOAD PROJECT", MessageType.ERROR, this)
            );
            return null;
        }
    }

    @Override
    public void saveProject(Project project, File file) {
        try {
            ProjectDTO dto = ProjectMapper.toDTO(project);
            objectMapper.writeValue(file, dto);
        } catch (IOException e) {
            ApplicationFramework.getInstance().getMessageGenerator().notify(
                    new Message("CANNOT SAVE PROJECT", MessageType.ERROR, this)
            );
        }
    }
}
