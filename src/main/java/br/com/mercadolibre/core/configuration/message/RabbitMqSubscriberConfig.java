package br.com.mercadolibre.core.configuration.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class RabbitMqSubscriberConfig {

    @Value("${queue.subscriber.exchange}")
    private String exchangeSubscriber;

    @Value("${queue.subscriber.queue}")
    private String subscriberQueue;

    @Bean
    @Qualifier("subscriberQueue")
    public Queue subscriberQueue() {
        return QueueBuilder
                .durable(subscriberQueue)
                .withArgument("x-dead-letter-exchange", "core-exchange-dlx")
                .withArgument("x-dead-letter-routing-key", "dlx-routing-key")
                .build();
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(exchangeSubscriber);
    }

    @Bean
    public Binding bindingSub(
            @Qualifier("subscriberQueue") Queue subscriberQueue,
            FanoutExchange exchange
    ) {
        return BindingBuilder.bind(subscriberQueue).to(exchange);
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange("core-exchange-dlx");
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable("process-update-inventory-queue-dlq").build();
    }

    @Bean
    public Binding deadLetterBinding(
            Queue deadLetterQueue,
            DirectExchange deadLetterExchange
    ) {
        return BindingBuilder
                .bind(deadLetterQueue)
                .to(deadLetterExchange)
                .with("dlx-routing-key");
    }
}
