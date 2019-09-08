package com.alag.amall.business.module.pay.server.controller;

import com.alag.amall.business.core.common.ServerResponse;
import com.alag.amall.business.module.pay.api.model.ResultPayInfo;
import com.alag.amall.business.module.pay.server.service.PayService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("amall/pay")
public class PayController {

    @Autowired
    private PayService payService;

    /**
     * 模拟支付回调
     * @param requestParamMap
     * @return
     */
    @RequestMapping("notify_alipay_callback_test")
    public ServerResponse alipayCallback(@RequestBody Map<String, String> requestParamMap) {
        if (requestParamMap == null || requestParamMap.size() == 0) {
            return ServerResponse.createByError();
        }
        return payService.alipayCallback(requestParamMap);
    }


    @GetMapping("query_result_pay_info_by_order_no")
    ServerResponse<ResultPayInfo> getByOrderNo(@RequestParam("orderNo") String orderNo) {
        if (StringUtils.isBlank(orderNo)) {

            return ServerResponse.createByError();
        }
        return payService.getPayInfoByOrderNo(orderNo);
    }
}
