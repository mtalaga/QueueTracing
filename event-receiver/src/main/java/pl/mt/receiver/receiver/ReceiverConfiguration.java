package pl.mt.receiver.receiver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ReceiverConfiguration {

    @Bean
    MessageHandler messageHandler() {
        return new ReceiverService();
    }
}
