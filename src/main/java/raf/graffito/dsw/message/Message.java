package raf.graffito.dsw.message;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.*;

public class Message {
    @Getter @Setter
    private String content;
    @Getter @Setter
    private MessageType type;
    @Getter
    private final LocalDateTime timestamp;
    @Getter @Setter
    private Object source;

    public Message(String content, MessageType type, Object source) {
        this.content = content;
        this.type = type;
        this.timestamp = LocalDateTime.now();
        this.source = source;
    }

    @Override
    public String toString() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yy. HH:mm");
        return "[" + type.toString() + "][" + timestamp.format(format) + "] " + content;
    }
}
