package br.com.mercadolibre.core.configuration.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import static br.com.mercadolibre.core.constants.RabbitMQConstants.*;

@Configuration
@Slf4j
@RequiredArgsConstructor
@EnableRabbit
public class RabbitMQConfig {

    @Value("${queue.publisher.exchange}")
    private String exchangePublisher;

    @Value("${queue.subscriber.exchange}")
    private String exchangeSubscriber;

    @Value("${queue.publisher.routing-key}")
    private String routingKeyPublisher;

    private static final String DLQ_SUFFIX = ".dlq";

    @Bean
    @Qualifier("publisherQueue")
    public Queue publisherQueue() {
        return QueueBuilder
                .durable(PROCESS_UPDATE_INVENTORY_QUEUE)
                .withArgument("x-dead-letter-exchange", exchangePublisher + DLQ_SUFFIX)
                .withArgument("x-dead-letter-routing-key", PROCESS_UPDATE_INVENTORY_QUEUE_DLQ)
                .build();
    }

    @Bean
    public Queue publisherDlq() {
        return QueueBuilder
                .durable(PROCESS_UPDATE_INVENTORY_QUEUE_DLQ)
                .build();
    }

    @Bean
    @Qualifier("subscriberQueue")
    public Queue subscriberQueue() {
        return new Queue(SEND_EVENT_INVENTORY_QUEUE, true);
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(exchangeSubscriber);
    }

    @Bean
    public DirectExchange publisherExchange() {
        return new DirectExchange(exchangePublisher);
    }

    @Bean
    public DirectExchange publisherDlqExchange() {
        return new DirectExchange(exchangePublisher + DLQ_SUFFIX);
    }

    @Bean
    @Primary
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
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

    @Bean
    public Binding bindingPub(
            @Qualifier("publisherQueue") Queue publisherQueue,
            DirectExchange publisherExchange
    ) {
        return BindingBuilder.bind(publisherQueue).to(publisherExchange).with(routingKeyPublisher);
    }

    @Bean
    public Binding bindingSub(
            @Qualifier("subscriberQueue") Queue subscriberQueue,
            FanoutExchange exchange
    ) {
        return BindingBuilder.bind(subscriberQueue).to(exchange);
    }

    @Bean
    public Binding bindingDlq(
            Queue publisherDlq,
            DirectExchange publisherDlqExchange
    ) {
        return BindingBuilder.bind(publisherDlq).to(publisherDlqExchange).with(PROCESS_UPDATE_INVENTORY_QUEUE_DLQ);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory,
            MessageConverter messageConverter
    ) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            RabbitTemplate rabbitTemplate,
            MessageConverter messageConverter
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);

        factory.setAdviceChain(RetryInterceptorBuilder.stateless()
                .maxAttempts(3)
                .recoverer(new RepublishMessageRecoverer(
                        rabbitTemplate,
                        "core-exchange-dlx",
                        "dlx-routing-key"
                ))
                .build());

        return factory;
    }
}
