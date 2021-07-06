package pl.mt.publisher;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.io.IOException;

import static com.google.cloud.spring.logging.StackdriverTraceConstants.MDC_FIELD_TRACE_ID;

@Slf4j
class PublisherService {

    private final Publisher publisher;

    PublisherService(String projectName, String topicName) throws IOException {
        var projectTopic = ProjectTopicName.of(projectName, topicName);
        this.publisher = Publisher.newBuilder(projectTopic).build();
    }

    public void publishMessage(String message) {
        log.info("Event got from controller, passing further...");
        var data = ByteString.copyFromUtf8(message);
        var pubsubMessage = PubsubMessage.newBuilder()
                .setData(data)
                .putAttributes(MDC_FIELD_TRACE_ID, MDC.get(MDC_FIELD_TRACE_ID))
                .build();
        ApiFuture<String> messageIdFuture = publisher.publish(pubsubMessage);
        ApiFutures.addCallback(messageIdFuture, new ApiFutureCallback<>() {
            public void onSuccess(String messageId) {
                log.info("Published with message id: " + messageId);
            }
            public void onFailure(Throwable t) {
                log.error("Failed to publish: " + t);
            }
        }, MoreExecutors.directExecutor());
    }
}
