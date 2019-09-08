package com.alag.amall.business.module.message.server.service;


import com.alag.amall.business.core.common.ServerResponse;
import com.alag.amall.business.core.page.ParamBean;
import com.alag.amall.business.module.message.api.model.TransactionMsg;
import com.github.pagehelper.PageInfo;

public interface TMessageService {
    ServerResponse saveMessage(TransactionMsg message);

    ServerResponse confirmAndSendMsg(String messageId);

    ServerResponse<Integer> saveAndSendMsg(TransactionMsg message);

    void directMsg(TransactionMsg message);

    void reSendMsg(TransactionMsg message);

    void rSendMsgByMsgId(String messageId);

    void setMsgToDead(String messageId);

    ServerResponse<TransactionMsg> getMsgByMsgId(String messageId);

    void delMsgByMsgId(String messageId);

    void rSendMsgByBatchSizeAndQName(int pageNum, int pageSize, String queueName);

    ServerResponse<PageInfo> listPage(ParamBean paramBean);
}
