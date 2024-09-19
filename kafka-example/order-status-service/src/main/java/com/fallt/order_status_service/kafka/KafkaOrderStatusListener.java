package com.fallt.order_status_service.kafka;

import com.fallt.order_status_service.service.OrderStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaOrderStatusListener {

    private final OrderStatusService orderStatusService;

    @KafkaListener(topics = "${app.kafka.kafkaOrderTopic}",
            containerFactory = "kafkaMessageConcurrentKafkaListenerContainerFactory")
    public void listen() {
        orderStatusService.send();
    }
}
