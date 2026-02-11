package raf.graffito.dsw.observer;

import raf.graffito.dsw.message.Message;

public interface Publisher {
    public void notify(Message message);
}
