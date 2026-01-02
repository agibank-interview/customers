package br.com.agibank.customers.infrastructure.configs;

import br.com.agibank.customers.infrastructure.exceptions.handler.MessageExceptionInterpolator;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import static java.nio.charset.StandardCharsets.UTF_8;

@Configuration
public class MessageInterpolatorConfig {

    @Bean
    public MessageSource messageSource() {
        final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:spec/validation_messages");
        messageSource.setDefaultEncoding(UTF_8.name());
        messageSource.setUseCodeAsDefaultMessage(false);
        return messageSource;
    }

    @Bean
    public MessageExceptionInterpolator messageExceptionInterpolator(final MessageSource messageSource) {
        return new MessageExceptionInterpolator(messageSource);
    }

    @Bean
    public Validator customValidator(final MessageExceptionInterpolator messageExceptionInterpolator) {
        return Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(messageExceptionInterpolator)
                .buildValidatorFactory()
                .getValidator();
    }
}