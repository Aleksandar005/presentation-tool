package raf.graffito.dsw.serializer;

import raf.graffito.dsw.model.implementation.Project;

import java.io.File;

public interface Serializer {
    Project loadProject(File file);
    void saveProject(Project project, File file);
}
