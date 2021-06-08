package pl.mt.receiver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ReceiverConfiguration {

    @Bean
    ReceiverService receiverService() {
        return new ReceiverService("pubsub-313916", "EventTopic-sub");
    }
}
