package br.com.mercadolibre.core.exception;

import br.com.mercadolibre.core.exception.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static java.util.stream.Collectors.joining;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Slf4j
public class ApplicationExceptionHandler {

    private static final String GENERIC_ERROR_MESSAGE = "Ocorreu um erro inesperado. Tente novamente mais tarde.";
    private static final String INTERNAL_ERROR = "Erro Interno";

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> globalExceptionHandler(Exception exception) {
        log.error("Erro inesperado: ", exception);

        final var applicationErrorResponse = ErrorResponse.builder()
                .status(INTERNAL_SERVER_ERROR)
                .error(INTERNAL_ERROR)
                .detail(GENERIC_ERROR_MESSAGE)
                .build();

        return new ResponseEntity<>(applicationErrorResponse, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException exception) {
        final var errorResponse = ErrorResponse.builder()
                .status(BAD_REQUEST)
                .error(exception.getClass().getSimpleName())
                .detail(exception.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, errorResponse.status());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        final var errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> String.format("%s %s", fieldError.getField(), fieldError.getDefaultMessage()))
                .distinct()
                .collect(joining(" | "));

        final var errorResponse = ErrorResponse.builder()
                .status(BAD_REQUEST)
                .error(exception.getClass().getSimpleName())
                .detail(errors)
                .build();

        return new ResponseEntity<>(errorResponse, errorResponse.status());
    }

    @ExceptionHandler(DuplicateRequestException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateRequestException(DuplicateRequestException exception) {
        final var errorResponse = ErrorResponse.builder()
                .status(BAD_REQUEST)
                .error(exception.getClass().getSimpleName())
                .detail(exception.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, errorResponse.status());
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientStockException(InsufficientStockException exception) {
        final var errorResponse = ErrorResponse.builder()
                .status(UNPROCESSABLE_ENTITY)
                .error(exception.getClass().getSimpleName())
                .detail(exception.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, errorResponse.status());
    }

    @ExceptionHandler(StockNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleStockNotFoundException(StockNotFoundException exception) {
        final var errorResponse = ErrorResponse.builder()
                .status(NOT_FOUND)
                .error(exception.getClass().getSimpleName())
                .detail(exception.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, errorResponse.status());
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(ProductNotFoundException exception) {
        final var errorResponse = ErrorResponse.builder()
                .status(NOT_FOUND)
                .error(exception.getClass().getSimpleName())
                .detail(exception.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, errorResponse.status());
    }
}
