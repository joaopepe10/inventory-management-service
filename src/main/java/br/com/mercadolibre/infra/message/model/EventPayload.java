package br.com.mercadolibre.infra.message.model;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record EventPayload (
        String eventId,
        EventType eventType,
        ChangeType changeType,
        String aggregateId,
        String source,
        LocalDateTime createdAt,
        Payload payload
) {
}
