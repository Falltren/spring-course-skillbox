package com.fallt.order_status_service.service;

import com.fallt.order.OrderStatusEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class OrderStatusService {

    private final KafkaTemplate<String, OrderStatusEvent> kafkaTemplate;

    @Value("${app.kafka.kafkaOrderStatusTopic}")
    private String topic;

    public void send() {
        kafkaTemplate.send(topic, new OrderStatusEvent("PROCESS", Instant.now()));
    }

}
