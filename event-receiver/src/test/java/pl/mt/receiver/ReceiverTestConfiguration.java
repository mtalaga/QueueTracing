package pl.mt.receiver;

import com.google.api.gax.core.NoCredentialsProvider;
import com.google.api.gax.grpc.GrpcTransportChannel;
import com.google.api.gax.rpc.FixedTransportChannelProvider;
import com.google.api.gax.rpc.TransportChannelProvider;
import com.google.cloud.pubsub.v1.SubscriptionAdminClient;
import com.google.cloud.pubsub.v1.SubscriptionAdminSettings;
import com.google.cloud.pubsub.v1.TopicAdminClient;
import com.google.cloud.pubsub.v1.TopicAdminSettings;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.PushConfig;
import com.google.pubsub.v1.TopicName;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import pl.mt.receiver.pubsub.PubSubProperties;
import pl.mt.receiver.pubsub.PubSubSubscription;
import pl.mt.receiver.receiver.MessageHandler;

import java.io.IOException;

import static pl.mt.receiver.ReceiverServiceBaseTest.pubSubEmulatorContainer;

@org.springframework.boot.test.context.TestConfiguration
public class ReceiverTestConfiguration {

    public ReceiverTestConfiguration(@Value("${receiver.projectid}") String projectId, @Value("${receiver.subscriptionid}") String subscriptionId, @Value("${receiver.topicid}") String topicId) throws IOException {
        ManagedChannel channel = ManagedChannelBuilder.forTarget(pubSubEmulatorContainer.getEmulatorEndpoint()).usePlaintext().build();
        TransportChannelProvider channelProvider =
                FixedTransportChannelProvider.create(GrpcTransportChannel.create(channel));
        NoCredentialsProvider credentialsProvider = NoCredentialsProvider.create();
        TopicAdminSettings topicAdminSettings = TopicAdminSettings.newBuilder()
                .setTransportChannelProvider(channelProvider)
                .setCredentialsProvider(credentialsProvider)
                .build();
        try (TopicAdminClient topicAdminClient = TopicAdminClient.create(topicAdminSettings)) {
            TopicName topicName = TopicName.of(projectId, topicId);
            topicAdminClient.createTopic(topicName);
        }
        SubscriptionAdminSettings subscriptionAdminSettings = SubscriptionAdminSettings.newBuilder()
                .setTransportChannelProvider(channelProvider)
                .setCredentialsProvider(credentialsProvider)
                .build();
        SubscriptionAdminClient subscriptionAdminClient = SubscriptionAdminClient.create(subscriptionAdminSettings);
        ProjectSubscriptionName subscriptionName = ProjectSubscriptionName.of(projectId, subscriptionId);
        subscriptionAdminClient.createSubscription(subscriptionName, TopicName.of(projectId, topicId), PushConfig.getDefaultInstance(), 10);
    }

    @Bean
    @Primary
    public PubSubSubscription pubSubTestSubscription(@Value("${receiver.projectid}") String projectId, @Value("${receiver.subscriptionid}") String subscriptionId, MessageHandler messageHandler) {
        ManagedChannel channel = ManagedChannelBuilder.forTarget(pubSubEmulatorContainer.getEmulatorEndpoint()).usePlaintext().build();
        TransportChannelProvider channelProvider =
                FixedTransportChannelProvider.create(GrpcTransportChannel.create(channel));

        var properties = PubSubProperties.create(projectId, subscriptionId, channelProvider, NoCredentialsProvider.create());
        return new PubSubSubscription(properties, messageHandler);
    }
}
