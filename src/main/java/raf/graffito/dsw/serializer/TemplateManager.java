package raf.graffito.dsw.serializer;

import lombok.Getter;
import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.message.Message;
import raf.graffito.dsw.message.MessageType;
import raf.graffito.dsw.model.graff.GraffNode;
import raf.graffito.dsw.model.implementation.Project;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Klasa za upravljanje šablonima projekata.
 * Šabloni se čuvaju u ~/.graffito/templates/ folderu.
 */
@Getter
public class TemplateManager {
    private static final String GRAFFITO_FOLDER = ".graffito";
    private static final String TEMPLATES_FOLDER = "templates";

    private final File templatesDirectory;

    public TemplateManager() {
        this.templatesDirectory = initTemplatesDirectory();
    }

    /**
     * Inicijalizuje templates folder u user home direktorijumu.
     * Ako folder ne postoji, kreira ga.
     */
    private File initTemplatesDirectory() {
        String userHome = System.getProperty("user.home");
        Path templatesPath = Paths.get(userHome, GRAFFITO_FOLDER, TEMPLATES_FOLDER);

        File templatesDir = templatesPath.toFile();

        if (!templatesDir.exists()) {
            boolean created = templatesDir.mkdirs();
            if (created) {
                ApplicationFramework.getInstance().getMessageGenerator().notify(
                        new Message("TEMPLATES FOLDER CREATED: " + templatesDir.getAbsolutePath(),
                                MessageType.INFO, this)
                );
            } else {
                ApplicationFramework.getInstance().getMessageGenerator().notify(
                        new Message("FAILED TO CREATE TEMPLATES FOLDER", MessageType.ERROR, this)
                );
            }
        }

        return templatesDir;
    }

    /**
     * Učitava šablon iz fajla i primenjuje ga na postojeći projekat.
     * Zadržava originalni title i author projekta.
     * vraca se true, ako je sve uspesno
     */
    public boolean loadTemplateIntoProject(File templateFile, Project targetProject) {
        if (templateFile == null || targetProject == null) {
            return false;
        }

        // Proveri da li je fajl iz templates foldera
        if (!isInTemplatesFolder(templateFile)) {
            ApplicationFramework.getInstance().getMessageGenerator().notify(
                    new Message("CANNOT LOAD TEMPLATE FROM OUTSIDE TEMPLATE FOLDER", MessageType.ERROR, this)
            );
            return false;
        }

        // Sačuvaj originalni title i author
        String originalTitle = targetProject.getTitle();
        String originalAuthor = targetProject.getAuthor();
        GraffNode originalParent = targetProject.getParent();

        // Učitaj šablon
        Project templateProject = ApplicationFramework.getInstance().getSerializer().loadProject(templateFile);

        if (templateProject == null) {
            ApplicationFramework.getInstance().getMessageGenerator().notify(
                    new Message("FAILED TO LOAD TEMPLATE", MessageType.ERROR, this)
            );
            return false;
        }

        // Obriši postojeću decu i resetuj number
        targetProject.getChildren().clear();
        targetProject.setNumber(0);  // DODATO: resetuj number

        // Kopiraj decu iz šablona koristeći addChild() da se ažurira number
        for (GraffNode child : templateProject.getChildren()) {
            child.setParent(targetProject);
            targetProject.addChild(child);
        }

        // Vrati originalni title, author i parent
        targetProject.setTitle(originalTitle);
        targetProject.setAuthor(originalAuthor);
        targetProject.setParent(originalParent);
        targetProject.setChanged(true);

        ApplicationFramework.getInstance().getMessageGenerator().notify(
                new Message("TEMPLATE LOADED INTO PROJECT: " + originalTitle, MessageType.INFO, this)
        );

        return true;
    }

    // Proverava da li je fajl unutar templates foldera.
    private boolean isInTemplatesFolder(File file) {
        try {
            String filePath = file.getCanonicalPath();
            String templatesDirPath = templatesDirectory.getCanonicalPath();
            return filePath.startsWith(templatesDirPath);
        } catch (Exception e) {
            return false;
        }
    }

    // Proverava da li postoje šabloni u templates folderu.
    public boolean hasTemplates() {
        File[] templates = templatesDirectory.listFiles((dir, name) ->
                name.toLowerCase().endsWith(".json"));
        return templates != null && templates.length > 0;
    }

    // Vraća listu svih šablona.
    public File[] getTemplates() {
        return templatesDirectory.listFiles((dir, name) ->
                name.toLowerCase().endsWith(".json"));
    }
}
