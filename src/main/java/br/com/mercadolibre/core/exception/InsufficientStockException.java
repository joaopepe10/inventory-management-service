package br.com.mercadolibre.core.exception;

public class InsufficientStockException extends RuntimeException {

    private static final String MESSAGE = "Estoque não disponível para o produto e loja informados.";

    public InsufficientStockException() {
        super(MESSAGE);
    }
}
