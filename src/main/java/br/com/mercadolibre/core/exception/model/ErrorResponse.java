package br.com.mercadolibre.core.exception.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record ErrorResponse(
        String error,
        String detail,
        HttpStatus status
) {
}
