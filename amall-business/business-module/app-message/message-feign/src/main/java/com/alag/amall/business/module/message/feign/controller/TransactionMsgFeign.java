package com.alag.amall.business.module.message.feign.controller;

import com.alag.amall.business.module.message.api.controller.TransactionMsgController;
import com.alag.amall.business.module.message.feign.fallback.KeepErrMsgConfiguration;
import com.alag.amall.business.module.message.feign.fallback.MessageFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Controller;

@Controller
@FeignClient(value = "app-message",fallbackFactory = MessageFallbackFactory.class,configuration = KeepErrMsgConfiguration.class)
public interface TransactionMsgFeign extends TransactionMsgController {
}
