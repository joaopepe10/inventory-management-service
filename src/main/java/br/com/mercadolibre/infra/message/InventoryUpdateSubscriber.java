package br.com.mercadolibre.infra.message;

import br.com.mercadolibre.domain.stock.StockService;
import br.com.mercadolibre.infra.message.model.UpdateInventoryMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryUpdateSubscriber {

    private final StockService stockService;

    @RabbitListener(queues = "${queue.name}")
    public void receiveMessage(@Valid Message<UpdateInventoryMessage> message) {
        var eventPayload = message.getPayload();
        stockService.update(eventPayload);
    }
}
