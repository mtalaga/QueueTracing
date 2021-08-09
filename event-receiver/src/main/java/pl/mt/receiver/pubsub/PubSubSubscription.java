package pl.mt.receiver.pubsub;

import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.PubsubMessage;
import lombok.extern.slf4j.Slf4j;
import pl.mt.receiver.receiver.MessageHandler;

@Slf4j
public class PubSubSubscription {

    private final MessageHandler messageHandler;

    public PubSubSubscription(PubSubProperties properties, MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
        initializeSubscription(properties);
    }

    private void initializeSubscription(PubSubProperties properties) {
        var projectSubscriptionName = ProjectSubscriptionName.of(properties.getProjectId(), properties.getSubscriptionId());
        MessageReceiver receiver = (message, consumer) -> {
            handleMessage(message);
            consumer.ack();
        };

        var subscriber = Subscriber.newBuilder(projectSubscriptionName, receiver)
                .setCredentialsProvider(properties.getCredentialsProvider())
                .setChannelProvider(properties.getTransportChannelProviderOptional())
                .build();

        subscriber.addListener(new Subscriber.Listener() {
            @Override
            public void failed(Subscriber.State from, Throwable failure) {
                log.error("Error when receiving message {}", failure.toString());
            }
        }, MoreExecutors.directExecutor());
        subscriber.startAsync().awaitRunning();
    }

    private void handleMessage(PubsubMessage message) {
        messageHandler.handleMessage(message);
    }
}
