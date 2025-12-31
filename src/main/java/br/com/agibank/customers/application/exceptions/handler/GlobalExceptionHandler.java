package br.com.agibank.customers.application.exceptions.handler;


import br.com.agibank.customers.api.v1.model.ErrorResponseDTO;
import br.com.agibank.customers.application.exceptions.BusinessConflictException;
import br.com.agibank.customers.application.exceptions.ResourceNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Set.of;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String GENERIC_VALIDATION_ERROR_MESSAGE = "The payload contains validation errors";
    private static final String PROCESSING_REQUEST_ERROR_MESSAGE = "Error processing request";
    private static final String DATA_CONVERSION_ERROR_MESSAGE = "Some data conversion error occurred. Check if the payload data matches the expected types.";
    private static final String PARAMETER_NOT_PRESENT_ERROR_MESSAGE = "Required request parameter '%s' is not present";
    private static final String TYPE_MISMATCH_FOR_PROPERTY_ERROR_MESSAGE = "Value '%s' mismatched for property type '%s'";
    private static final String INVALID_REQUEST_PARAMETERS = "Invalid request parameters";

    @ExceptionHandler(BusinessConflictException.class)
    public ResponseEntity<ErrorResponseDTO> handleBusinessConflictException(final BusinessConflictException exception) {
        return buildErrorResponse(
                PROCESSING_REQUEST_ERROR_MESSAGE,
                of(exception.getMessage()),
                exception,
                exception.getHttpStatus());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(final ResourceNotFoundException exception) {
        return buildErrorResponse(PROCESSING_REQUEST_ERROR_MESSAGE,
                of(exception.getMessage()),
                exception,
                exception.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValidException(final MethodArgumentNotValidException exception) {
        final Set<String> details = exception.getBindingResult()
                .getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.toCollection(TreeSet::new));
        return buildErrorResponse(
                GENERIC_VALIDATION_ERROR_MESSAGE,
                details,
                exception,
                BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleHttpMessageNotReadableException(final HttpMessageNotReadableException exception) {
        final Set<String> details = new LinkedHashSet<>();
        details.add(DATA_CONVERSION_ERROR_MESSAGE);
        details.add(exception.getMessage());
        return buildErrorResponse(
                GENERIC_VALIDATION_ERROR_MESSAGE,
                details,
                exception,
                BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponseDTO> handleMissingServletRequestParameterException(final MissingServletRequestParameterException exception) {
        return buildErrorResponse(
                GENERIC_VALIDATION_ERROR_MESSAGE,
                of(format(PARAMETER_NOT_PRESENT_ERROR_MESSAGE, exception.getParameterName())),
                exception,
                BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException exception) {
        return buildErrorResponse(
                INVALID_REQUEST_PARAMETERS,
                of(format(
                        TYPE_MISMATCH_FOR_PROPERTY_ERROR_MESSAGE,
                        exception.getValue(),
                        exception.getPropertyName())),
                exception,
                BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleConstraintViolation(final ConstraintViolationException exception) {
        final Set<String> details = exception.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());
        return buildErrorResponse(
                INVALID_REQUEST_PARAMETERS,
                details,
                exception,
                BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgumentException(final IllegalArgumentException exception) {
        return buildErrorResponse(
                PROCESSING_REQUEST_ERROR_MESSAGE,
                of(exception.getMessage()),
                exception,
                BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(final Exception exception) {
        return buildErrorResponse(
                PROCESSING_REQUEST_ERROR_MESSAGE,
                of(exception.getMessage()),
                exception,
                INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponseDTO> buildErrorResponse(final String message,
                                                                final Set<String> details,
                                                                final Exception exception,
                                                                final HttpStatus status) {
        log.error(message, exception);
        final ErrorResponseDTO errorResponse = new ErrorResponseDTO().message(message);
        errorResponse.setDetails(details);
        return new ResponseEntity<>(errorResponse, status);
    }
}