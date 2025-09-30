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


@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryUpdateSubscriber {

    private final StockService stockService;

    @Value("${info.storeName}")
    private String currentStoreName;

    @RabbitListener(queues = "${queue.subscriber.queue}")
    public void receiveMessage(@Valid Message<UpdateInventoryMessage> message) {
        var eventPayload = message.getPayload();

        if (currentStoreName.equals(eventPayload.messageOrigin())) {
            log.info("Ignorando mensagem da própria loja: {}", currentStoreName);
            return;
        }

        if (eventPayload.eventId().contains("TESTE")) {
            log.warn("Mensagem de teste recebida, lançando exceção para testar retry");
            throw new RuntimeException("Erro proposital para teste de retry");
        }

        log.info("Processando mensagem da loja: {}", eventPayload.messageOrigin());
        stockService.update(eventPayload);
    }
}
