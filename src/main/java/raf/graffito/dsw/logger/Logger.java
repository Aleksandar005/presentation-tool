package raf.graffito.dsw.logger;

import raf.graffito.dsw.message.Message;
import raf.graffito.dsw.observer.Subscriber;

public interface Logger extends Subscriber {

    @Override
    public void update(Message message);
}
