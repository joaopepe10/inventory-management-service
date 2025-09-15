package br.com.mercadolibre.infra.message.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UpdateInventoryMessage(
        @NotBlank
        String eventId,

        @NotNull
        EventType eventType,

        @NotNull
        ChangeType changeType,

        @NotBlank
        String aggregateId,

        @NotBlank
        String source,

        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt,

        @NotNull
        Payload payload
) {
}
