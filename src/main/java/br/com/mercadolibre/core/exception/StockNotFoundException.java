package br.com.mercadolibre.core.exception;

public class StockNotFoundException extends RuntimeException {

    private final static String MESSAGE = "Estoque não encontrado para o produto.";

    public StockNotFoundException() {
        super(MESSAGE);
    }
}
