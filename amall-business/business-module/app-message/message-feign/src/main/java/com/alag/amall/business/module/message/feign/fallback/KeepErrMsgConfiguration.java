package com.alag.amall.business.module.message.feign.fallback;

import com.alag.amall.business.core.common.ServerResponse;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.util.HashMap;

@Slf4j
public class KeepErrMsgConfiguration {
    @Bean
    public ErrorDecoder errorDecoder() {
        return new UserErrorDecoder();
    }
    /**
     * 自定义错误
     */
    public class UserErrorDecoder implements ErrorDecoder {
        @Override
        public Exception decode(String methodKey, Response response) {
            Exception exception = null;
            try {
                // 获取原始的返回内容
                String json = Util.toString(response.body().asReader());
                log.info("errJson:{}",json);
                JSONObject obj = JSONObject.parseObject(json);
                Integer status = obj.getInteger("status");
                String trace = obj.getString("trace");
                String message = obj.getString("message");
                HashMap<String, String> msgMap = Maps.newHashMap();
                msgMap.put("trace", trace);
                msgMap.put("message", message);
                String msg = JSONObject.toJSONString(msgMap);

                ServerResponse result = ServerResponse.createByErrorCodeMessage(status, msg);
                exception = new RuntimeException(result.getMsg());
            } catch (IOException ex) {
                log.error(ex.getMessage());
            }
            return exception;
        }
    }
}
