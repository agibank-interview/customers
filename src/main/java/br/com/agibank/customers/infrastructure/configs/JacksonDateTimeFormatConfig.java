package br.com.agibank.customers.infrastructure.configs; // Ou o pacote de configuração do seu projeto

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.OffsetDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;
import static com.fasterxml.jackson.datatype.jsr310.ser.OffsetDateTimeSerializer.INSTANCE;

@Configuration
public class JacksonDateTimeFormatConfig {

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH:mm:ssXXX").withZone(ZoneOffset.UTC);

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> {
            final JavaTimeModule javaTimeModule = new JavaTimeModule();
            javaTimeModule.addSerializer(OffsetDateTime.class,
                    new OffsetDateTimeSerializer(INSTANCE, false, DATE_TIME_FORMATTER, STRING));
            builder.modules(javaTimeModule);
            builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        };
    }
}