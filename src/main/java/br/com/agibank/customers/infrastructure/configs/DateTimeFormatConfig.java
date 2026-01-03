package br.com.agibank.customers.infrastructure.configs; // Ou o pacote de configuração do seu projeto

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.OffsetDateTimeSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;
import static com.fasterxml.jackson.datatype.jsr310.ser.OffsetDateTimeSerializer.INSTANCE;

@Configuration
public class DateTimeFormatConfig {

    private static final String DATE_TIME_FORMAT = "dd-MM-yyyy'T'HH:mm:ssXXX";

    @Value("${app.business-timezone}")
    private String businessTimezone;

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        final var formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT).withZone(businessZoneId());
        return builder -> {
            final var javaTimeModule = new JavaTimeModule();
            javaTimeModule.addSerializer(OffsetDateTime.class,
                    new OffsetDateTimeSerializer(INSTANCE, false, formatter, STRING));
            builder.modules(javaTimeModule);
            builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        };
    }

    @Bean
    public ZoneId businessZoneId() {
        return ZoneId.of(businessTimezone);
    }
}