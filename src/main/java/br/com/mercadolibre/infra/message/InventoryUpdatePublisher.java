package br.com.mercadolibre.infra.message;

import br.com.mercadolibre.core.configuration.message.RabbitMQConfig;
import br.com.mercadolibre.infra.message.model.UpdateInventoryMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class InventoryUpdatePublisher {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQConfig rabbitMQConfig;

    public void sendMessageAsync(UpdateInventoryMessage message) {
        CompletableFuture.runAsync(() -> sendMessage(message));
    }

    private void sendMessage(UpdateInventoryMessage message) {
        rabbitTemplate.convertAndSend(rabbitMQConfig.getQueueName(), message);
    }
}
