package com.alag.amall.business.module.order.feign.controller;

import com.alag.amall.business.module.order.api.controller.OrderController;
import com.alag.amall.business.module.order.feign.fallback.KeepErrMsgConfiguration;
import com.alag.amall.business.module.order.feign.fallback.OrderFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Controller;

@Controller
@FeignClient(value = "app-order",fallbackFactory = OrderFallbackFactory.class,configuration = KeepErrMsgConfiguration.class)
public interface OrderFeign extends OrderController {
}

