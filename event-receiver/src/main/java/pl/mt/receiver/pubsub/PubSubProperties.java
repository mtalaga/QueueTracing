package pl.mt.receiver.pubsub;

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.rpc.TransportChannelProvider;
import com.google.cloud.pubsub.v1.SubscriptionAdminSettings;
import lombok.Getter;
import org.threeten.bp.Duration;

@Getter
public class PubSubProperties {

    private static final int MAX_INBOUND_MESSAGE_SIZE =
            20 * 1024 * 1024; // 20MB API maximum message size.

    String projectId;
    String subscriptionId;
    TransportChannelProvider transportChannelProviderOptional;
    CredentialsProvider credentialsProvider;

    private PubSubProperties(String projectId, String subscriptionId, TransportChannelProvider transportChannelProviderOptional, CredentialsProvider credentialsProvider) {
        this.projectId = projectId;
        this.subscriptionId = subscriptionId;
        this.transportChannelProviderOptional = transportChannelProviderOptional;
        this.credentialsProvider = credentialsProvider;
    }

    public static PubSubProperties createWithDefaults(String projectId, String subscriptionId) {
        var defaultChannelProvider = SubscriptionAdminSettings.defaultGrpcTransportProviderBuilder()
                .setMaxInboundMessageSize(MAX_INBOUND_MESSAGE_SIZE)
                .setKeepAliveTime(Duration.ofMinutes(5))
                .build();

        var defaultCredentialsProvider = SubscriptionAdminSettings.defaultCredentialsProviderBuilder().build();

        return new PubSubProperties(projectId, subscriptionId, defaultChannelProvider, defaultCredentialsProvider);
    }

    public static PubSubProperties create(String projectId, String subscriptionId, TransportChannelProvider transportChannelProviderOptional, CredentialsProvider credentialsProvider) {
        return new PubSubProperties(projectId, subscriptionId, transportChannelProviderOptional, credentialsProvider);
    }

}
