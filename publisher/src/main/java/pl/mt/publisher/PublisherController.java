package pl.mt.publisher;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
class PublisherController {

    private PublisherService publisherService;
    private Tracer tracer =
            openTelemetry.getTracer("instrumentation-library-name", "1.0.0");

    PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @PostMapping("/event")
    public ResponseEntity<String> postEvent(@RequestBody String eventContent) {
        Span span = tracer.spanBuilder("my span").startSpan();
        log.info("Received event, start handling...");
        publisherService.publishMessage(eventContent);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
