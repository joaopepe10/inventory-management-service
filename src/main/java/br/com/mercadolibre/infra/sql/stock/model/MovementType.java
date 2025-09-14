package br.com.mercadolibre.infra.sql.stock.model;

import lombok.Getter;

@Getter
public enum MovementType {
    INBOUND("Entrada"),
    OUTBOUND("Saída"),
    TRANSFER_IN("Transferência de entrada"),
    TRANSFER_OUT("Transferência de saída"),
    ADJUSTMENT("Reajuste"),
    RESERVATION("Reserva"),
    RELEASE("Liberação");

    private final String translation;

    MovementType(String translation) {
        this.translation = translation;
    }
}
