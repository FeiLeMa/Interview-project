package com.alag.amall.business.module.pay.feign;

import com.alag.amall.business.module.pay.api.controller.PayController;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RestController;

@RestController
@FeignClient(value = "app-pay")
public interface PayFeign extends PayController{
}
