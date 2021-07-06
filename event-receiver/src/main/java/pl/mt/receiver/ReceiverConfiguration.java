package pl.mt.receiver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ReceiverConfiguration {

    @Bean
    ReceiverService receiverService(@Value("${receiver.projectid}") String projectId, @Value("${receiver.subscriptionid}") String subscriptionName) {
        return new ReceiverService(projectId, subscriptionName);
    }
}
