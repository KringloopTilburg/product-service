package nl.kringlooptilburg.productservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.product-image}")
    private String productImageQueueName;

    @Value("${rabbitmq.exchange.product-image}")
    private String productImageExchangeName;

    @Value("${rabbitmq.routing-key.product-image}")
    private String productImageRoutingKeyName;

    @Bean
    public Queue productImageQueue() {
        return new Queue(productImageQueueName);
    }

    @Bean
    public TopicExchange productImageExchange() {
        return new TopicExchange(productImageExchangeName);
    }

    @Bean
    public Binding productImageBinding(Queue productImageQueue, TopicExchange productImageExchange) {
        return BindingBuilder.
                bind(productImageQueue)
                .to(productImageExchange)
                .with(productImageRoutingKeyName);
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}
