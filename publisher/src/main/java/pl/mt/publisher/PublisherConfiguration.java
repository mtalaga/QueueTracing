package pl.mt.publisher;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
class PublisherConfiguration {

    @Bean
    PublisherService publisher(@Value("${publisher.projectid}") String projectName, @Value("${publisher.topicid}") String topicName) throws IOException {
        return new PublisherService(projectName, topicName);
    }
}
