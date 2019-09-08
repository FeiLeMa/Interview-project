package com.alag.amall.business.module.order.server.service;

import com.alag.amall.business.core.common.ServerResponse;
import com.alag.amall.business.module.order.api.model.Order;

public interface OrderService {
    ServerResponse<Order> getOrderByOrderNo(String orderNo);

    ServerResponse updateOrderStatus(String orderNo);
}
