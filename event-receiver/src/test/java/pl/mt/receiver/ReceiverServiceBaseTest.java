package pl.mt.receiver;

import com.google.api.gax.core.NoCredentialsProvider;
import com.google.api.gax.grpc.GrpcTransportChannel;
import com.google.api.gax.rpc.FixedTransportChannelProvider;
import com.google.api.gax.rpc.TransportChannelProvider;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.pubsub.v1.TopicName;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PubSubEmulatorContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;

@ExtendWith(OutputCaptureExtension.class)
@Import(ReceiverTestConfiguration.class)
@SpringBootTest
@Testcontainers
public abstract class ReceiverServiceBaseTest {

    @Container
    protected static final PubSubEmulatorContainer pubSubEmulatorContainer = new PubSubEmulatorContainer(DockerImageName.parse("gcr.io/google.com/cloudsdktool/cloud-sdk:316.0.0-emulators"));

    protected static Publisher publisher;

    @BeforeAll
    public static void init(@Value("${receiver.projectid}") String projectId, @Value("${receiver.topicid}") String topicId) throws IOException {
        ManagedChannel channel = ManagedChannelBuilder.forTarget(pubSubEmulatorContainer.getEmulatorEndpoint()).usePlaintext().build();
        TransportChannelProvider channelProvider =
                FixedTransportChannelProvider.create(GrpcTransportChannel.create(channel));
        publisher = Publisher.newBuilder(TopicName.of(projectId, topicId))
                .setChannelProvider(channelProvider)
                .setCredentialsProvider(NoCredentialsProvider.create())
                .build();


    }

}
