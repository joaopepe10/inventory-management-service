package br.com.mercadolibre.core.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RabbitMQConstants {

    public static final String PROCESS_UPDATE_INVENTORY_QUEUE = "PROCESS_UPDATE_INVENTORY_QUEUE";
    public static final String PROCESS_UPDATE_INVENTORY_QUEUE_DLQ = "PROCESS_UPDATE_INVENTORY_QUEUE_DLQ";
}
