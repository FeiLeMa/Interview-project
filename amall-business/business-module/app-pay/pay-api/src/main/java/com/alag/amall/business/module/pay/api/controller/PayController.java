package com.alag.amall.business.module.pay.api.controller;

import com.alag.amall.business.core.common.ServerResponse;
import com.alag.amall.business.module.pay.api.model.ResultPayInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("amall/pay")
public interface PayController {

    @GetMapping("query_result_pay_info_by_order_no")
    ServerResponse<ResultPayInfo> getByOrderNo(@RequestParam("orderNo") String orderNo);
}
