package br.com.mercadolibre.core.configuration.message;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import static br.com.mercadolibre.core.constants.RabbitMQConstants.PROCESS_UPDATE_INVENTORY_QUEUE;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    @Value("${queue.publisher.exchange}")
    private String exchangePublisher;

    @Value("${queue.subscriber.exchange}")
    private String exchangeSubscriber;

    @Value("${queue.publisher.routing-key}")
    private String routingKeyPublisher;

    @Value("${queue.name}")
    private String queueName;

    @Bean
    public String queueName(@Value("${queue.name}") String queueName) {
        return queueName;
    }

    @Bean
    @Qualifier("publisherQueue")
    public Queue publisherQueue() {
        return new Queue(PROCESS_UPDATE_INVENTORY_QUEUE, true);
    }

    @Bean
    @Qualifier("subscriberQueue")
    public Queue subscriberQueue() {
        return new Queue(queueName, true);
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(exchangeSubscriber);
    }

    @Bean
    @Qualifier("subscriberBinding")
    public Binding bindingSub(@Qualifier("subscriberQueue") Queue consumerQueue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(consumerQueue).to(fanoutExchange);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchangePublisher);
    }

    @Bean
    @Primary
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Binding bindingPub(
            @Qualifier("publisherQueue") Queue publisherQueue,
            DirectExchange exchange
    ) {
        return BindingBuilder.bind(publisherQueue).to(exchange).with(routingKeyPublisher);
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
            MessageConverter jsonMessageConverter
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter);
        return factory;
    }

}
