package com.alag.amall.business.module.order.api.controller;

import com.alag.amall.business.core.common.ServerResponse;
import com.alag.amall.business.module.order.api.model.Order;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("amall/order")
public interface OrderController {

    /**
     * 根据OrderNo查询Order
     * @param orderNo
     * @return
     */
    @GetMapping("query_order_by_order_no")
    ServerResponse<Order> queryOrderByOrderNo(@RequestParam("orderNo") String orderNo);

    @PutMapping("modify_order_status")
    ServerResponse modifyOrderStatus(@RequestParam("orderNo") String orderNo);
}
