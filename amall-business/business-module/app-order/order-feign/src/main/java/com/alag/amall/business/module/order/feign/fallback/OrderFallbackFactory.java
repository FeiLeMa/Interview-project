package com.alag.amall.business.module.order.feign.fallback;

import com.alag.amall.business.core.common.ServerResponse;
import com.alag.amall.business.module.order.api.model.Order;
import com.alag.amall.business.module.order.feign.controller.OrderFeign;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderFallbackFactory implements FallbackFactory<OrderFeign> {
    private static final HystrixCommand.Setter hystrixCommandGroupKey =
            HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("TransactionMessageFeign"));
    @Override
    public OrderFeign create(Throwable throwable) {
        return new OrderFeign() {
            @Override
            public ServerResponse<Order> queryOrderByOrderNo(String orderNo) {
                log.error("queryOrderByOrderNo-errMsg:{}",throwable.getMessage());
                return ServerResponse.createByErrorMessage(throwable.getMessage());
            }

            @Override
            public ServerResponse modifyOrderStatus(String orderNo) {
                log.error("queryOrderByOrderNo-errMsg:{}",throwable.getMessage());
                return ServerResponse.createByErrorMessage(throwable.getMessage());
            }
        };
    }
}
