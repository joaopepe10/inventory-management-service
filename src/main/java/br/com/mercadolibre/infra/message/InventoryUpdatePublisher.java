package br.com.mercadolibre.infra.message;

import br.com.mercadolibre.infra.message.model.UpdateInventoryMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

import static br.com.mercadolibre.core.constants.RabbitMQConstants.PROCESS_UPDATE_INVENTORY_QUEUE;

@Component
@RequiredArgsConstructor
public class InventoryUpdatePublisher {

    private final RabbitTemplate rabbitTemplate;

    public void sendMessageAsync(@Valid UpdateInventoryMessage message) {
        CompletableFuture.runAsync(() -> sendMessage(message));
    }

    private void sendMessage(UpdateInventoryMessage message) {
        rabbitTemplate.convertAndSend(PROCESS_UPDATE_INVENTORY_QUEUE, message);
    }
}
