package raf.graffito.dsw.message;

import raf.graffito.dsw.observer.Publisher;
import raf.graffito.dsw.observer.Subscriber;

import java.util.ArrayList;
import java.util.List;

public class MessageGenerator implements Publisher {
    private List<Subscriber> subscribers = new ArrayList<>();

    public MessageGenerator() {
    }


    public void addSubscriber(Subscriber s){
        if(s == null){
            throw new IllegalArgumentException("Subskrajber ne može da bude prazan");
        }
        subscribers.add(s);
    }

    public void removeSubscriber(Subscriber s){
        subscribers.remove(s);
    }

    @Override
    public void notify(Message message){
        for(Subscriber subscriber : subscribers){
            subscriber.update(message);
        }
    }
}
