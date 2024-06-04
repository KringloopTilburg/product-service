package nl.kringlooptilburg.productservice.config;

import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class TestConfig {
    @Bean
    @Primary
    public OtlpGrpcSpanExporter otlpHttpSpanExporter() {
        return Mockito.mock(OtlpGrpcSpanExporter.class);
    }

    @Bean
    @Primary
    public BatchSpanProcessor batchSpanProcessor() {
        return Mockito.mock(BatchSpanProcessor.class);
    }
}
