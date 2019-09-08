package com.alag.amall.business.module.message.feign.fallback;

import com.alag.amall.business.core.common.ServerResponse;
import com.alag.amall.business.core.page.ParamBean;
import com.alag.amall.business.module.message.api.model.TransactionMsg;
import com.alag.amall.business.module.message.feign.controller.TransactionMsgFeign;
import com.github.pagehelper.PageInfo;
import com.netflix.hystrix.HystrixCommandGroupKey;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import com.netflix.hystrix.HystrixCommand.Setter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageFallbackFactory implements FallbackFactory<TransactionMsgFeign> {
    private static final Setter hystrixCommandGroupKey =
            Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("TransactionMessageFeign"));
    @Override
    public TransactionMsgFeign create(Throwable throwable) {
        return new TransactionMsgFeign() {
            @Override
            public ServerResponse saveMessageWaitingConfirm(TransactionMsg TransactionMsg) {
                log.error("saveMessageWaitingConfirm:errMsg:{}",throwable.getMessage());
                
                return ServerResponse.createByErrorMessage(throwable.getMessage());
            }

            @Override
            public ServerResponse confirmAndSendMessage(String messageId) {
                log.error("confirmAndSendMessage:errMsg:{}",throwable.getMessage());
                return ServerResponse.createByErrorMessage(throwable.getMessage());
            }

            @Override
            public ServerResponse<Integer> saveAndSendMessage(TransactionMsg TransactionMsg) {
                log.error("saveAndSendMessage:errMsg:{}",throwable.getMessage());
                return ServerResponse.createByErrorMessage(throwable.getMessage());
            }

            @Override
            public void directSendMessage(TransactionMsg TransactionMsg) {
                log.error("directSendMessage:errMsg:{}",throwable.getMessage());
            }

            @Override
            public void reSendMessage(TransactionMsg TransactionMsg) {
                log.error("reSendMessage:errMsg:{}",throwable.getMessage());
            }

            @Override
            public void reSendMessageByMessageId(String messageId) {
                log.error("reSendMessageByMessageId:errMsg:{}",throwable.getMessage());
            }

            @Override
            public void setMessageToAreadlyDead(String messageId) {
                log.error("setMessageToAreadlyDead:errMsg:{}",throwable.getMessage());
            }

            @Override
            public ServerResponse<TransactionMsg> getMessageByMessageId(String messageId) {
                log.error("getMessageByMessageId:errMsg:{}",throwable.getMessage());
                return ServerResponse.createByErrorMessage(throwable.getMessage());
            }

            @Override
            public ServerResponse deleteMessageByMessageId(String messageId) {
                log.error("deleteMessageByMessageId:errMsg:{}",throwable.getMessage());
                return ServerResponse.createByErrorMessage(throwable.getMessage());
            }

            @Override
            public void reSendAllDeadMessageByQueueName(String queueName, int batchSize) {
                log.error("setMessageToAreadlyDead:errMsg:{}",throwable.getMessage());
            }

            @Override
            public ServerResponse<PageInfo> listPage(ParamBean paramBean) {
                log.error("listPage:errMsg:{}",throwable.getMessage());
                return ServerResponse.createByErrorMessage(throwable.getMessage());
            }
        };
    }
}
