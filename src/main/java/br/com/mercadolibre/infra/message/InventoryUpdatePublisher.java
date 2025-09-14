package br.com.mercadolibre.infra.message;

import br.com.mercadolibre.infra.message.model.EventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

import static br.com.mercadolibre.core.configuration.message.RabbitMQConfig.PROCESS_UPDATE_INVENTORY_QUEUE;

@Component
@RequiredArgsConstructor
public class InventoryUpdatePublisher {

    private final RabbitTemplate rabbitTemplate;

    public void sendMessageAsync(EventPayload message) {
        CompletableFuture.runAsync(() -> sendMessage(message));
    }

    private void sendMessage(EventPayload message) {
        rabbitTemplate.convertAndSend(PROCESS_UPDATE_INVENTORY_QUEUE, message);
    }
}
