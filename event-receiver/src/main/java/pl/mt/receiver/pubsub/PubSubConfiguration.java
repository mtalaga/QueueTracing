package pl.mt.receiver.pubsub;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.mt.receiver.receiver.MessageHandler;

@Configuration
public class PubSubConfiguration {

    @Bean
    public PubSubSubscription pubSubSubscription(@Value("${receiver.projectid}") String projectId, @Value("${receiver.subscriptionid}") String subscriptionName, MessageHandler messageHandler) {
        var properties = PubSubProperties.createWithDefaults(projectId, subscriptionName);
        return new PubSubSubscription(properties, messageHandler);
    }

}
