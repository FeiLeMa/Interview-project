package com.alag.amall.business.module.order.server.controller;

import com.alag.amall.business.core.common.ServerResponse;
import com.alag.amall.business.module.order.api.model.Order;
import com.alag.amall.business.module.order.server.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("amall/order")
@Slf4j
public class OrderServerController {
    @Autowired
    private OrderService orderService;

    @GetMapping("query_order_by_order_no")
    public ServerResponse<Order> queryOrderByOrderNo(@RequestParam("orderNo") String orderNo) {
        if (StringUtils.isBlank(orderNo)) {
            log.info("orderNo不可为空");
            return ServerResponse.createByError();
        }
        return orderService.getOrderByOrderNo(orderNo);
    }

    @PutMapping("modify_order_status")
    ServerResponse modifyOrderStatus(@RequestParam("orderNo") String orderNo) {
        if (StringUtils.isBlank(orderNo)) {
            log.info("orderNo不可为空");
            return ServerResponse.createByError();
        }
        return orderService.updateOrderStatus(orderNo);
    }
}
