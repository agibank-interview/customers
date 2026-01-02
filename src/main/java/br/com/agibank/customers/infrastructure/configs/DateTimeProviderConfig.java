package br.com.agibank.customers.infrastructure.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;

import java.time.OffsetDateTime;
import java.util.Optional;

@Configuration
public class DateTimeProviderConfig {

    public static final String OFFSET_DATE_TIME_PROVIDER = "offsetDateTimeProvider";

    @Bean(OFFSET_DATE_TIME_PROVIDER)
    public DateTimeProvider offsetDateTimeProvider() {
        return () -> Optional.of(OffsetDateTime.now());
    }
}
