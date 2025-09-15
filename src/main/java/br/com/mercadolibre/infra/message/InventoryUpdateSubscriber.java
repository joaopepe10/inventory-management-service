package br.com.mercadolibre.infra.message;

import br.com.mercadolibre.domain.stock.StockService;
import br.com.mercadolibre.infra.message.model.UpdateInventoryMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import static br.com.mercadolibre.core.constants.RabbitMQConstants.SEND_EVENT_INVENTORY_QUEUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryUpdateSubscriber {

    private final StockService stockService;

    @RabbitListener(queues = SEND_EVENT_INVENTORY_QUEUE)
    public void receiveMessage(@Valid Message<UpdateInventoryMessage> message) {
        var eventPayload = message.getPayload();
        stockService.increase(eventPayload);
    }
}
