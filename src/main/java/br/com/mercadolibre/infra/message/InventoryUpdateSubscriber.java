package br.com.mercadolibre.infra.message;

import br.com.mercadolibre.domain.stock.StockService;
import br.com.mercadolibre.infra.message.model.UpdateInventoryMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import static br.com.mercadolibre.core.constants.RabbitMQConstants.SEND_EVENT_INVENTORY_QUEUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryUpdateSubscriber {

    private final StockService stockService;

    @Value("${info.storeName}")
    private String currentStoreName;

    @RabbitListener(queues = SEND_EVENT_INVENTORY_QUEUE)
    public void receiveMessage(@Valid Message<UpdateInventoryMessage> message) {
        var eventPayload = message.getPayload();

//        if (currentStoreName.equals(eventPayload.messageOrigin())) {
//            log.info("Ignorando mensagem da própria loja: {}", currentStoreName);
//            return;
//        }

        if (currentStoreName.equals(eventPayload.messageOrigin())) {
            throw new RuntimeException("A loja de origem da mensagem não pode ser a mesma que a loja atual.");
        }


        log.info("Processando mensagem da loja: {}", eventPayload.messageOrigin());
        stockService.update(eventPayload);
    }}
