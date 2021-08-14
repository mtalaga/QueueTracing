package pl.mt.receiver.pubsub;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.mt.receiver.receiver.MessageHandler;

@Configuration
public class PubSubConfiguration {

    @Bean
    public PubSubProperties pubSubProperties(@Value("${receiver.projectid}") String projectId, @Value("${receiver.subscriptionid}") String subscriptionName) {
        return PubSubProperties.createWithDefaults(projectId, subscriptionName);
    }

    @Bean
    public PubSubSubscription pubSubSubscription(PubSubProperties properties, MessageHandler messageHandler) {
        return new PubSubSubscription(properties, messageHandler);
    }

}
