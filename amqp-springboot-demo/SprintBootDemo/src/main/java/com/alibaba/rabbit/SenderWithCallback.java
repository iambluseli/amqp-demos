package com.alibaba.rabbit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.UUID;


@Component
public class SenderWithCallback {
    Logger log= LoggerFactory.getLogger(SenderWithCallback.class);
    @Autowired
    private  RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void initRabbitTemplate() {
        // 设置生产者消息确认
        rabbitTemplate.setConfirmCallback(new RabbitConfirmCallback());
        rabbitTemplate.setReturnCallback(new RabbitReturnCallback());
    }


    public void send() {
        String exchange = "exchange-rabbit-springboot-advance5";
        String routingKey = "product";
        String unRoutingKey = "norProduct";


        // 1.发送一条正常的消息 CorrelationData唯一（可以在ConfirmListener中确认消息）
        //IntStream.rangeClosed(0, 10).forEach(num -> {
        //    String message = LocalDateTime.now().toString() + "发送第" + (num + 1) + "条消息.";
        //    rabbitTemplate.convertAndSend(exchange, routingKey, message, new CorrelationData("routing" + UUID.randomUUID().toString()));
        //    log.info("发送一条消息,exchange:[{}],routingKey:[{}],message:[{}]", exchange, routingKey, message);
        //});
        // 2.发送一条未被路由的消息，此消息将会进入备份交换器（alternate exchange）
        String message = LocalDateTime.now().toString() + "发送一条消息.";
        rabbitTemplate.convertAndSend(exchange, unRoutingKey, message, new CorrelationData("unRouting-" + UUID.randomUUID().toString()));
        log.info("发送一条消息,exchange:[{}],routingKey:[{}],message:[{}]", exchange, unRoutingKey, message);



    }

}
