package pl.mt.receiver.receiver;

import com.google.pubsub.v1.PubsubMessage;

public interface MessageHandler {

    String handleMessage(PubsubMessage message);

}
