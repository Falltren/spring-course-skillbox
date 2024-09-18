package com.fallt.order_service.controller;

import com.fallt.order_service.entity.Order;
import com.fallt.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/send")
    public String sendMessage(@RequestBody Order order){
        orderService.send(order);
        return "Message sent to kafka";
    }


}
