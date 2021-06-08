package pl.mt.receiver;

import com.google.api.core.ApiService;
import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.PubsubMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class ReceiverService {

    ReceiverService(String projectName, String subscriptionName) {
        var projectSubscriptionName = ProjectSubscriptionName.of(projectName, subscriptionName);
        MessageReceiver receiver = (message, consumer) -> {
            handleMessage(message);
            consumer.ack();
        };

        var subscriber = Subscriber.newBuilder(projectSubscriptionName, receiver).build();
        subscriber.addListener(new Subscriber.Listener() {
            public void failed(Subscriber.State from, Throwable failure) {
                log.error("Error when receiving message {}", failure.toString());
            }
        }, MoreExecutors.directExecutor());
        subscriber.startAsync().awaitRunning();
    }

    private void handleMessage(PubsubMessage message) {
        log.info("Received message through subscription: " + message);
    }
}
