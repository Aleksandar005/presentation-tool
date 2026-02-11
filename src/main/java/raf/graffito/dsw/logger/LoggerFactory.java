package raf.graffito.dsw.logger;

public class LoggerFactory {

    public Logger createLogger(String type) {
        if (type == null)
            throw new IllegalArgumentException("Tip loggera ne može biti null.");

        switch (type.toLowerCase()) {
            case "console":
                return new ConsoleLogger();
            case "file":
                return new FileLogger();
            default:
                throw new IllegalArgumentException("Nepoznat tip loggera: " + type);
        }
    }
}