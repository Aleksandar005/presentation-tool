package raf.graffito.dsw.logger;

import raf.graffito.dsw.message.Message;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileLogger implements Logger{
    public FileLogger() {
        File file = new File("src/main/resources/log.txt");
        try (FileWriter writer = new FileWriter(file, true)) {
            writer.write("\n===== NEW APPLICATION START =====\n");
        } catch (IOException e) {
            System.out.println("Error writing to log file: " + e.getMessage());
        }
    }
    @Override
    public void update(Message message) {
        File file = new File("src/main/resources/log.txt");

        try (FileWriter writer = new FileWriter(file, true)) {
            writer.write(message.toString() + "\n");
        }
        catch (Exception e){
            System.out.println("Error writing to log file: " + e.getMessage());
        }
    }
}
