package raf.graffito.dsw.core;
import raf.graffito.dsw.bridge.WindowModeManager;
import raf.graffito.dsw.clipboard.ClipboardManager;
import raf.graffito.dsw.command.CommandManager;
import raf.graffito.dsw.decorator.ColorPalette;
import raf.graffito.dsw.gui.swing.MainFrame;
import raf.graffito.dsw.image.ImageLibrary;
import raf.graffito.dsw.logger.ConsoleLogger;
import raf.graffito.dsw.logger.FileLogger;
import raf.graffito.dsw.logger.Logger;
import raf.graffito.dsw.logger.LoggerFactory;
import raf.graffito.dsw.message.MessageGenerator;
import raf.graffito.dsw.model.repository.GraffRepository;
import raf.graffito.dsw.model.repository.GraffRepositoryImpl;
import lombok.Getter;
import raf.graffito.dsw.serializer.JacksonSerializer;
import raf.graffito.dsw.serializer.Serializer;
import raf.graffito.dsw.serializer.TemplateManager;
import raf.graffito.dsw.strategy.SpaceChecker;

@Getter
public class ApplicationFramework {
    // Buduća polja za model celog projekta

    private static ApplicationFramework instance;
    private MessageGenerator messageGenerator;
    private LoggerFactory loggerFactory;
    private GraffRepository graffRepository;
    private ColorPalette colorPallete;
    private CommandManager commandManager;
    private TemplateManager templateManager;
    private Serializer serializer;
    private ImageLibrary imageLibrary;
    private SpaceChecker spaceChecker;
    private WindowModeManager windowModeManager;
    private ClipboardManager clipboardManager;

    private ApplicationFramework(){
        instance = this;
        initialize();
    }

    public static ApplicationFramework getInstance(){
        if(instance == null)
            instance = new ApplicationFramework();

        return instance;
    }

    public void initialize(){
        messageGenerator = new MessageGenerator();
        loggerFactory = new LoggerFactory();
        colorPallete = new ColorPalette();
        graffRepository = new GraffRepositoryImpl();
        commandManager = new CommandManager();
        serializer = new JacksonSerializer();
        templateManager = new TemplateManager();
        imageLibrary = new ImageLibrary();
        spaceChecker = new SpaceChecker();
        windowModeManager = new WindowModeManager();
        clipboardManager = new ClipboardManager();

        MainFrame mainFrame = MainFrame.getInstance();

        messageGenerator.addSubscriber(loggerFactory.createLogger("console"));
        messageGenerator.addSubscriber(loggerFactory.createLogger("file"));
        messageGenerator.addSubscriber(mainFrame);

        mainFrame.setVisible(true);
    }
}
