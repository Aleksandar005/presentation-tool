package raf.graffito.dsw.logger;

import raf.graffito.dsw.message.Message;

public class ConsoleLogger implements Logger{

    @Override
    public void update(Message message) {
        System.out.println(message.toString());
    }
}
