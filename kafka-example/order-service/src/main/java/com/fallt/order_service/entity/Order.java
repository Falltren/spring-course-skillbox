package com.fallt.order_service.entity;

import com.fallt.order.OrderEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    private String product;

    private Integer quantity;

    public static OrderEvent toEvent(Order order) {
        return new OrderEvent(order.getProduct(), order.quantity);
    }
}
