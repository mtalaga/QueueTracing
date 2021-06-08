package pl.mt.publisher;

import com.google.cloud.opentelemetry.trace.TraceConfiguration;
import com.google.cloud.opentelemetry.trace.TraceExporter;
import com.google.cloud.spring.core.GcpProjectIdProvider;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
class PublisherConfiguration {

    public PublisherConfiguration(GcpProjectIdProvider gcpProjectIdProvider) throws IOException {
        var traceExporter = TraceExporter.createWithConfiguration(
                TraceConfiguration.builder().setProjectId("pubsub-313916").build()
        );
        var sdkTracerProvider = SdkTracerProvider.builder()
                .addSpanProcessor(SimpleSpanProcessor.create(traceExporter))
                .build();
        OpenTelemetrySdk.builder()
                .setTracerProvider(sdkTracerProvider)
                .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
                .buildAndRegisterGlobal();
    }

    @Bean
    PublisherService publisher() throws IOException {
        return new PublisherService("pubsub-313916", "EventTopic");
    }
}
