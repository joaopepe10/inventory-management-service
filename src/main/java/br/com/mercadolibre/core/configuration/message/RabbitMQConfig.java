package br.com.mercadolibre.core.configuration.message;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import static br.com.mercadolibre.core.constants.RabbitMQConstants.PROCESS_UPDATE_INVENTORY_QUEUE;

@Configuration
public class RabbitMQConfig {

    @Value("${queue.publisher.exchange}")
    private String exchangePublisher;

    @Value("${queue.publisher.routing-key}")
    private String routingKeyPublisher;

    @Bean
    @Qualifier("publisherQueue")
    public Queue publisherQueue() {
        return new Queue(PROCESS_UPDATE_INVENTORY_QUEUE, true);
    }

    @Bean
    @Qualifier("subscriberQueue")
    public Queue subscriberQueue() {
        return new Queue(PROCESS_UPDATE_INVENTORY_QUEUE, true);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchangePublisher);
    }

    @Bean
    @Primary
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Binding binding(@Qualifier("publisherQueue")Queue publisherQueue, DirectExchange exchange) {
        return BindingBuilder.bind(publisherQueue).to(exchange).with(routingKeyPublisher);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }

}
