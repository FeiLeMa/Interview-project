package com.alag.amall.business.module.message.server.service.impl;


import com.alag.amall.business.core.common.Const;
import com.alag.amall.business.core.page.ParamBean;
import com.alag.amall.business.core.util.DateTimeUtil;
import com.alag.amall.business.module.message.api.model.TransactionMsg;
import com.alag.amall.business.module.message.server.service.MsgTaskService;
import com.alag.amall.business.module.message.server.service.TMessageService;
import com.alag.amall.business.module.pay.api.model.ResultPayInfo;
import com.alag.amall.business.module.pay.feign.PayFeign;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class MsgTaskServiceImpl implements MsgTaskService {
    @Autowired
    private com.alag.amall.business.module.message.server.mapper.TransactionMsgMapper TransactionMsgMapper;
    @Autowired
    private TMessageService tMessageService;
    @Autowired
    private PayFeign payFeign;

    @Override
    public void handleWaitConfirmMessage() {
        ParamBean paramBean = ParamBean.newParamBean();
        Date createTimeBefore = this.getCreateTimeBefore();
        paramBean.set(pB -> {
            pB.setAreadlyDead(Const.TMessage.AREADLY_DEAD_NO);
            pB.setListPageSortType("ASC");
            pB.setStatus(Const.MessageEnum.WAITING_CONFIRM.getCode());
            pB.setPageNum(1);
            pB.setPageSize(100);
            pB.setCreateTimeBefore(createTimeBefore);
        });

        PageHelper.startPage(paramBean.getPageNum(), paramBean.getPageSize());
        List<TransactionMsg> TransactionMsgs = TransactionMsgMapper.list(paramBean);
        PageInfo pageInfo = new PageInfo(TransactionMsgs);

        List<TransactionMsg> TransactionMsgList = pageInfo.getList();
        log.info("===================Waiting----Message查询结果============================");
        TransactionMsgList.forEach(tMsg -> {
            log.info("TransactionMsg->{}" + tMsg);
            log.info("================================================================");
            String msgID = tMsg.getMessageId();
            log.info("msgID{}已被取出", msgID);

            String orderNo = msgID.replaceAll("Order", "");
            log.info("当前TransactionMsg中的OrderNo为{}", orderNo);
            ResultPayInfo resultPayInfo = payFeign.getByOrderNo(orderNo).getData();
            if (resultPayInfo == null) {
                //调用三方支付平台支付查询接口
                //...

                tMessageService.delMsgByMsgId(msgID);


            } else {
                tMessageService.confirmAndSendMsg(msgID);
            }
        });
    }


    @Override
    public void handleConfirmSendingMessage() {
        Map<Integer, Integer> timeoutMap = Maps.newHashMap();
        timeoutMap.put(0, Const.TMessage.FIRST_SEND_TIMES);
        timeoutMap.put(1, Const.TMessage.SECOND_SEND_TIMES);
        timeoutMap.put(2, Const.TMessage.THIRD_SEND_TIMES);
        timeoutMap.put(3, Const.TMessage.FOURTH_SEND_TIMES);
        timeoutMap.put(4, Const.TMessage.FIFTH_SEND_TIMES);

        ParamBean paramBean = ParamBean.newParamBean();
        Date createTimeBefore = this.getCreateTimeBefore();
        paramBean.set(pB -> {
            pB.setAreadlyDead(Const.TMessage.AREADLY_DEAD_NO);
            pB.setListPageSortType("ASC");
            pB.setStatus(Const.MessageEnum.SENDING.getCode());
            pB.setPageNum(1);
            pB.setPageSize(100);
            pB.setCreateTimeBefore(createTimeBefore);
        });

        PageHelper.startPage(paramBean.getPageNum(), paramBean.getPageSize());
        List<TransactionMsg> TransactionMsgs = TransactionMsgMapper.list(paramBean);
        PageInfo pageInfo = new PageInfo(TransactionMsgs);


        List<TransactionMsg> TransactionMsgList = pageInfo.getList();
        log.info("===================ConfirmSending----Message查询结果============================");
        TransactionMsgList.forEach(tMsg -> {
            log.info("TransactionMsg->{}" + tMsg);
            log.info("================================================================");
            int messageSendTimes = tMsg.getMessageSendTimes();
            if (messageSendTimes >= 5) {
                tMessageService.setMsgToDead(tMsg.getMessageId());
                return;
            }
            //本消息此次超时重发时间
            int currentMsgRSendTime = timeoutMap.get(messageSendTimes);
            Date hasSendTime = DateTimeUtil.addTime(tMsg.getUpdateTime(), currentMsgRSendTime);

            if (DateTimeUtil.before(new Date(), hasSendTime)) {
                log.info("这条消息应该在{}再次发送，当前时间{}", DateTimeUtil.dateToStr(hasSendTime), DateTimeUtil.dateToStr(new Date()));
                return;
            }

            tMessageService.reSendMsg(tMsg);
        });
    }

    private Date getCreateTimeBefore() {
        return DateTimeUtil.addTime(-Const.TMessage.MESSAGE_HANDLE_DURATION);
    }
}
