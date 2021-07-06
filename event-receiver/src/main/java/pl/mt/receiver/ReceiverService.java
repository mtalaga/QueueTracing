package pl.mt.receiver;

import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.PubsubMessage;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.UUID;

import static com.google.cloud.spring.logging.StackdriverTraceConstants.MDC_FIELD_SPAN_ID;
import static com.google.cloud.spring.logging.StackdriverTraceConstants.MDC_FIELD_TRACE_ID;

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
            @Override
            public void failed(Subscriber.State from, Throwable failure) {
                log.error("Error when receiving message {}", failure.toString());
            }
        }, MoreExecutors.directExecutor());
        subscriber.startAsync().awaitRunning();
    }

    private void handleMessage(PubsubMessage message) {
        var traceId = message.getAttributesOrDefault(MDC_FIELD_TRACE_ID, UUID.randomUUID().toString());
        MDC.put(MDC_FIELD_TRACE_ID, traceId);
        MDC.put(MDC_FIELD_SPAN_ID, UUID.randomUUID().toString());
        log.info("Received message through subscription: " + message);
    }
}
