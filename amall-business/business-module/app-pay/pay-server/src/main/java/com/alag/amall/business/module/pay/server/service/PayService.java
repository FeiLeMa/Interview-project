package com.alag.amall.business.module.pay.server.service;

import com.alag.amall.business.core.common.ServerResponse;
import com.alag.amall.business.module.pay.api.model.ResultPayInfo;

import java.util.Map;

public interface PayService {

    ServerResponse alipayCallback(Map<String, String> requestParamMap);

    ServerResponse<ResultPayInfo> getPayInfoByOrderNo(String orderNo);
}
