package br.com.mercadolibre.core.exception;

public class ProductNotFoundException extends RuntimeException {

    private final static String MESSAGE = "Produto não encontrado.";

    public ProductNotFoundException() {
        super(MESSAGE);
    }
}
