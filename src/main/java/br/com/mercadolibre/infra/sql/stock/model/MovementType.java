package br.com.mercadolibre.infra.sql.stock.model;

public enum MovementType {
    INBOUND,
    OUTBOUND,
    TRANSFER_IN,
    TRANSFER_OUT,
    ADJUSTMENT,
    RESERVATION,
    RELEASE;
}
