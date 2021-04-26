package com.trilogyed.commentqueueconsumer;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CommentQueueConsumerApplication {

    public static final String TOPIC_EXCHANGE_NAME = "queue-demo-exchange";
    public static final String QUEUE_NAME = "comment-add-queue";
    public static final String ROUTING_KEY = "comment.add.#";

    @Bean
    Queue queue() {  // The Queue you need here is import org.springframework.amqp.core.Queue;
        return new Queue(QUEUE_NAME, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(TOPIC_EXCHANGE_NAME);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    public static void main(String[] args) {
		SpringApplication.run(CommentQueueConsumerApplication.class, args);
	}

}
