package com.fallt.order_service.service;

import com.fallt.order.OrderEvent;
import com.fallt.order_service.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    @Value("${app.kafka.kafkaOrderTopic}")
    private String topicName;

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public void send(Order order) {
        kafkaTemplate.send(topicName, Order.toEvent(order));
    }
}
