package pl.mt.receiver.receiver;

import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.system.CapturedOutput;
import pl.mt.receiver.ReceiverServiceBaseTest;

import java.util.UUID;

import static com.google.cloud.spring.logging.StackdriverTraceConstants.MDC_FIELD_TRACE_ID;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReceiverServiceTest extends ReceiverServiceBaseTest {

    @Test
    void assertMessageWasHandled(CapturedOutput capturedOutput) throws InterruptedException {
        var data = ByteString.copyFromUtf8("Hello from test");
        publisher.publish(PubsubMessage.newBuilder()
                .setData(data)
                .putAttributes(MDC_FIELD_TRACE_ID, UUID.randomUUID().toString())
                .build());
        Thread.sleep(5000L);
        assertTrue(capturedOutput.getOut().contains("Received message through subscription"));
    }
}