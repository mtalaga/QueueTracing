package pl.mt.publisher;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
class PublisherController {

    private final PublisherService publisherService;

    PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @PostMapping("/event")
    public ResponseEntity<String> postEvent(@RequestBody String eventContent) {
        log.info("Received event, start handling...");
        publisherService.publishMessage(eventContent);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
