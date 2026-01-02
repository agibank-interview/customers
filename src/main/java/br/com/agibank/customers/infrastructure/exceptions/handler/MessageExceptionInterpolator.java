package br.com.agibank.customers.infrastructure.exceptions.handler;

import jakarta.validation.MessageInterpolator;
import jakarta.validation.Path;
import org.hibernate.validator.internal.engine.MessageInterpolatorContext;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.context.MessageSource;

import java.util.Locale;

public class MessageExceptionInterpolator implements MessageInterpolator {

    private static final Locale PT_BR_LOCALE = Locale.forLanguageTag("pt-BR");

    private final MessageSource messageSource;

    public MessageExceptionInterpolator(final MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public String interpolate(final String messageTemplate, final Context context) {
        return interpolate(messageTemplate, context, PT_BR_LOCALE);
    }

    @Override
    public String interpolate(final String messageTemplate, final Context context, final Locale locale) {
        if (context != null) {
            final Path propertyPath = ((MessageInterpolatorContext) context).getPropertyPath();
            final String propertyName = ((PathImpl) propertyPath).getLeafNode().toString();
            if (propertyName != null) {
                return messageSource.getMessage(
                        propertyName,
                        new Object[]{
                                propertyName.toUpperCase(),
                                "'" + context.getValidatedValue() + "'"},
                        locale);
            }
            throw new IllegalArgumentException("PropertyPath cannot be null");
        }
        throw new IllegalArgumentException("Context cannot be null");
    }
}