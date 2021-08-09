package pl.mt.receiver.receiver;

import com.google.pubsub.v1.PubsubMessage;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.UUID;

import static com.google.cloud.spring.logging.StackdriverTraceConstants.MDC_FIELD_SPAN_ID;
import static com.google.cloud.spring.logging.StackdriverTraceConstants.MDC_FIELD_TRACE_ID;

@Slf4j
class ReceiverService implements MessageHandler {

    protected ReceiverService() {
    }
    
    public String handleMessage(PubsubMessage message) {
        var traceId = message.getAttributesOrDefault(MDC_FIELD_TRACE_ID, UUID.randomUUID().toString());
        MDC.put(MDC_FIELD_TRACE_ID, traceId);
        MDC.put(MDC_FIELD_SPAN_ID, UUID.randomUUID().toString());
        log.info("Received message through subscription: " + message);
        return message.getMessageId();
    }
}
