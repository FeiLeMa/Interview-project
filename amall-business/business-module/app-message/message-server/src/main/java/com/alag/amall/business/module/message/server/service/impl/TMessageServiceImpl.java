package com.alag.amall.business.module.message.server.service.impl;


import com.alag.amall.business.core.common.Const;
import com.alag.amall.business.core.common.ServerResponse;
import com.alag.amall.business.core.page.ParamBean;
import com.alag.amall.business.module.message.api.model.TransactionMsg;
import com.alag.amall.business.module.message.server.exceptions.MessageBizException;
import com.alag.amall.business.module.message.server.mapper.TransactionMsgMapper;
import com.alag.amall.business.module.message.server.service.TMessageService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TMessageServiceImpl implements TMessageService {
    @Autowired
    private TransactionMsgMapper transactionMsgMapper;
    @Autowired
    private JmsTemplate jmsTemplate;

    @Override
    public ServerResponse saveMessage(TransactionMsg message) {
        message.setStatus(Const.MessageEnum.WAITING_CONFIRM.getCode());
        message.setAreadlyDead(Const.TMessage.AREADLY_DEAD_NO);
        message.setMessageSendTimes(Const.TMessage.RE_SENT_TIMES_ZEROTH);
        message.setUpdateTime(new Date());

        int row = transactionMsgMapper.insertSelective(message);
        if (row > 0) {
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByError();
        }
    }

    @Override
    public ServerResponse confirmAndSendMsg(String messageId) {
        final TransactionMsg message = getMsgByMsgId(messageId).getData();
        if (message == null) {
            throw new MessageBizException(MessageBizException.SAVA_MESSAGE_IS_NULL, "根据消息id查找的消息为空");
        }
        message.setStatus(Const.MessageEnum.SENDING.getCode());
        message.setUpdateTime(new Date());
        transactionMsgMapper.updateByPrimaryKeySelective(message);

        jmsTemplate.setDefaultDestinationName(message.getConsumerQueue());
        jmsTemplate.send(session -> session.createTextMessage(message.getMessageBody()));
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<Integer> saveAndSendMsg(TransactionMsg message) {
        message.setStatus(Const.MessageEnum.SENDING.getCode());
        message.setAreadlyDead(Const.TMessage.AREADLY_DEAD_NO);
        message.setMessageSendTimes(Const.TMessage.RE_SENT_TIMES_ZEROTH);
        message.setCreateTime(new Date());
        message.setUpdateTime(new Date());

        int row = transactionMsgMapper.insertSelective(message);

        jmsTemplate.setDefaultDestinationName(message.getConsumerQueue());
        jmsTemplate.send(session -> session.createTextMessage(message.getMessageBody()));

        return ServerResponse.createBySuccess(row);
    }

    @Override
    public void directMsg(TransactionMsg message) {
        jmsTemplate.setDefaultDestinationName(message.getConsumerQueue());
        jmsTemplate.send(session -> session.createTextMessage(message.getMessageBody()));
    }

    @Override
    public void reSendMsg(TransactionMsg message) {
        message.addSendTimes();
        message.setUpdateTime(new Date());
        transactionMsgMapper.updateByPrimaryKeySelective(message);

        jmsTemplate.setDefaultDestinationName(message.getConsumerQueue());
        jmsTemplate.send(session -> session.createTextMessage(message.getMessageBody()));
    }

    @Override
    public void rSendMsgByMsgId(String messageId) {
        final TransactionMsg message = getMsgByMsgId(messageId).getData();
        if (message == null) {
            throw new MessageBizException(MessageBizException.SAVA_MESSAGE_IS_NULL, "根据消息id查找的消息为空");
        }

        if (message.getMessageSendTimes() >= Const.TMessage.MESSAGE_MAX_SEND_TIMES) {
            message.setAreadlyDead(Const.TMessage.AREADLY_DEAD_YES);
        }

        message.setUpdateTime(new Date());
        message.addSendTimes();
        transactionMsgMapper.updateByPrimaryKeySelective(message);

        jmsTemplate.setDefaultDestinationName(message.getConsumerQueue());
        jmsTemplate.send(session -> session.createTextMessage(message.getMessageBody()));

    }

    @Override
    public void setMsgToDead(String messageId) {
        final TransactionMsg message = getMsgByMsgId(messageId).getData();
        if (message == null) {
            throw new MessageBizException(MessageBizException.SAVA_MESSAGE_IS_NULL, "根据消息id查找的消息为空");
        }

        message.setUpdateTime(new Date());
        message.setAreadlyDead(Const.TMessage.AREADLY_DEAD_YES);
        transactionMsgMapper.updateByPrimaryKeySelective(message);
    }


    @Override
    public ServerResponse<TransactionMsg> getMsgByMsgId(String messageId) {
        return ServerResponse.createBySuccess(transactionMsgMapper.getByMessageId(messageId));
    }

    @Override
    public void delMsgByMsgId(String messageId) {
        transactionMsgMapper.delByMsgId(messageId);
    }

    @Override
    public void rSendMsgByBatchSizeAndQName(int pageNum, int pageSize, String queueName) {
        PageHelper.startPage(pageNum, pageSize);
        List<TransactionMsg> TransactionMsgs = transactionMsgMapper.selectByQueueName(queueName);
        PageInfo pageInfo = new PageInfo(TransactionMsgs);


        TransactionMsgs = pageInfo.getList();
        TransactionMsgs.forEach(message -> {
            message.setUpdateTime(new Date());
            message.addSendTimes();
            transactionMsgMapper.updateByPrimaryKeySelective(message);

            jmsTemplate.setDefaultDestinationName(queueName);
            jmsTemplate.send(session -> session.createTextMessage(message.getMessageBody()));
        });

    }

    @Override
    public ServerResponse<PageInfo> listPage(ParamBean paramBean) {
        PageHelper.startPage(paramBean.getPageNum(), paramBean.getPageSize());
        List<TransactionMsg> TransactionMsgs = transactionMsgMapper.list(paramBean);
        PageInfo pageInfo = new PageInfo(TransactionMsgs);

        return ServerResponse.createBySuccess(pageInfo);
    }
}
