package br.com.mercadolibre.core.configuration.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static br.com.mercadolibre.core.constants.RabbitMQConstants.PROCESS_UPDATE_INVENTORY_QUEUE;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class RabbitMqPublisherConfig {

    @Value("${queue.publisher.routing-key}")
    private String routingKeyPublisher;

    @Value("${queue.publisher.exchange}")
    private String exchangePublisher;

    @Bean
    @Qualifier("publisherQueue")
    public Queue publisherQueue() {
        return QueueBuilder
                .durable(PROCESS_UPDATE_INVENTORY_QUEUE)
                .withArgument("x-dead-letter-exchange", "store-exchange-dlx")
                .withArgument("x-dead-letter-routing-key", "dlx-routing-key")
                .build();
    }

    @Bean
    public DirectExchange publisherExchange() {
        return new DirectExchange(exchangePublisher);
    }

    @Bean
    public Binding bindingPub(
            @Qualifier("publisherQueue") Queue publisherQueue,
            DirectExchange publisherExchange
    ) {
        return BindingBuilder.bind(publisherQueue).to(publisherExchange).with(routingKeyPublisher);
    }
}
