package com.code.design.order;

import com.code.design.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventHandler {

    private final CartService cartService;

    @Async // 비동기 처리 -> 서로 다른 트랜잭션으로 분리되어 영향을 주지 않는다. + 응답속도 개선 / 단, 순서 보장은 안된다.
    @EventListener
    // @Order()
    public void orderCompletedEventListener(OrderCompletedEvent event) {
        cartService.deleteCart(event.getOrder());
    }

}
