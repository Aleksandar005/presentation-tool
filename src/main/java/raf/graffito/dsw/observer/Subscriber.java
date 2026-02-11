package raf.graffito.dsw.observer;

import raf.graffito.dsw.message.Message;

public interface Subscriber {
    void update(Message message);
}
