package br.com.agibank.customers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.TimeZone;

import static br.com.agibank.customers.infrastructure.configs.DateTimeProviderConfig.OFFSET_DATE_TIME_PROVIDER;

@SpringBootApplication
@EnableJpaAuditing(dateTimeProviderRef = OFFSET_DATE_TIME_PROVIDER)
public class CustomersApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SpringApplication.run(CustomersApplication.class, args);
    }
}
