package com.example.greenproject.rabbitmq;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.context.support.GenericWebApplicationContext;

@Configuration
@EnableRabbit
@EnableAutoConfiguration(exclude = RabbitAutoConfiguration.class)
@RequiredArgsConstructor
public class RabbitMQConfig {
    private final GenericWebApplicationContext context;

    private String rabbitHost="localhost";


    private int rabbitPort=5672;


    private String rabbitUsername="guest";


    private String rabbitPassword="guest";


    private String rabbitVirtualHost="payment";



    private CachingConnectionFactory getCachingConnectionFactoryCommon() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(this.rabbitHost, this.rabbitPort);
        connectionFactory.setUsername(this.rabbitUsername);
        connectionFactory.setPassword(this.rabbitPassword);
        return connectionFactory;
    }

    @Primary
    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Primary
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }

    @Primary
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = this.getCachingConnectionFactoryCommon();
        connectionFactory.setVirtualHost(this.rabbitVirtualHost);
        return connectionFactory;
    }

    @Primary
    @Bean("rabbitListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory containerFactory(ConnectionFactory connectionFactory) {
        final SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setDefaultRequeueRejected(false);
        factory.setConnectionFactory(connectionFactory);
        factory.setDefaultRequeueRejected(false);
        return factory;
    }

    @Bean
    public Queue paymentQueue() {
        return new Queue("payment-queue", false);
    }

    @Bean
    public TopicExchange paymentExchange() {
        return new TopicExchange("payment-exchange");
    }

    @Bean
    public Binding paymentBinding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("payment-key");
    }

}
