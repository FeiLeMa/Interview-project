package com.alag.amall.business.module.pay.server.service.impl;

import com.alag.amall.business.core.common.Const;
import com.alag.amall.business.core.common.ServerResponse;
import com.alag.amall.business.core.util.DateTimeUtil;
import com.alag.amall.business.core.util.IDGenerator;
import com.alag.amall.business.module.message.api.model.TransactionMsg;
import com.alag.amall.business.module.message.feign.controller.TransactionMsgFeign;
import com.alag.amall.business.module.order.api.model.Order;
import com.alag.amall.business.module.order.feign.controller.OrderFeign;
import com.alag.amall.business.module.pay.api.model.ResultPayInfo;
import com.alag.amall.business.module.pay.server.mapper.ResultPayInfoMapper;
import com.alag.amall.business.module.pay.server.service.PayService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

@Service
@Slf4j
public class PayServiceImpl implements PayService {
    @Autowired
    private ResultPayInfoMapper resultPayInfoMapper;
    @Autowired
    private OrderFeign orderFeign;
    @Autowired
    private TransactionMsgFeign transactionMsgFeign;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse alipayCallback(Map<String, String> requestParamMap) {

        //将接收到Map需要的值取出来
        final String orderNo = requestParamMap.get("out_trade_no");
        final String tradeNo = requestParamMap.get("trade_no");
        final String tradeStatus = requestParamMap.get("trade_status");
        final String gmtPayment = requestParamMap.get("gmt_payment");

        //判断订单是否存在
        ServerResponse<Order> queryOrderRespon = orderFeign.queryOrderByOrderNo(orderNo);
        if (!queryOrderRespon.isSuccess()) {
            log.info("没有查询到该订单-orderNo:{}",orderNo);
            return queryOrderRespon;
        }
        //幂等判断
        Order order = queryOrderRespon.getData();
        if (order.getStatus() == Const.OrderStatusEnum.PAID.getCode()) {
            log.info("订单已经支付支付成功-orderNo:{}",orderNo);
            return ServerResponse.createByErrorMessage("重复通知，订单已经支付");
        }
        if (!Const.AlipayCallback.TRADE_STATUS_TRADE_SUCCESS.equals(tradeStatus)) {
           //支付结果失败，只添加支付回调结果记录
           this.addResultPayInfo(gmtPayment,orderNo,tradeStatus,tradeNo);
           return ServerResponse.createBySuccess();
        }

        TransactionMsg msg = this.newTransactionMsg(orderNo);
        //发送预存储消息
        ServerResponse saveMessageWaitingConfirmResponse = transactionMsgFeign.saveMessageWaitingConfirm(msg);
        log.info("预存储结果-success:{}",saveMessageWaitingConfirmResponse.getMsg());

        //消息预存储成功，则进行本地业务处理
        if (!saveMessageWaitingConfirmResponse.isSuccess()) {
            return saveMessageWaitingConfirmResponse;
        }
        //添加支付结果信息
        boolean addResultPayInfoSuccess = this.addResultPayInfo(gmtPayment,orderNo,tradeStatus,tradeNo);

        if (addResultPayInfoSuccess) {
            //异步确认并发送消息
            log.info("确认消息已发送");
            transactionMsgFeign.confirmAndSendMessage(msg.getMessageId());
            log.info("正在修改订单-orderNo:{}",orderNo);
        }

        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<ResultPayInfo> getPayInfoByOrderNo(String orderNo) {
        ResultPayInfo resultPayInfo = resultPayInfoMapper.selectByOrderNo(orderNo);
        if (resultPayInfo == null) {
            log.info("未查到resultPayInfo-orderNo:{}",orderNo);
            return ServerResponse.createByError();
        }
        return ServerResponse.createBySuccess(resultPayInfo);
    }



    private TransactionMsg newTransactionMsg(String orderNo) {
        TransactionMsg msg = new TransactionMsg();
        msg.setCreator(Const.TMessage.CREATOR_NAME);
        msg.setEditor(Const.TMessage.EDITOR_NAME);
        msg.setId(IDGenerator.tMIDBuilder());
        msg.setMessageId(Const.TMessage.ORDER_MSG_ID_PRE + orderNo);
        msg.setMessageBody(JSONObject.toJSONString(orderNo));
        msg.setCreateTime(new Date());
        msg.setUpdateTime(new Date());
        msg.setConsumerQueue(Const.TMessage.ORDER_QUEUE_NAME);
        msg.setVersion(1);
        msg.setMessageDataType(Const.TMessage.MESSAGE_DATA_TYPE);
        msg.setRemark(Const.TMessage.REMARK);
        return msg;
    }

    private boolean addResultPayInfo(String gmtPayment,String orderNo,String tradeStatus,String tradeNo) {
        ResultPayInfo resultPayInfo = new ResultPayInfo();
        resultPayInfo.setGmtPayment(DateTimeUtil.strToDate(gmtPayment));
        resultPayInfo.setId(IDGenerator.alipayInfoIDBuilder());
        resultPayInfo.setOrderNo(orderNo);
        resultPayInfo.setTradeStatus(tradeStatus);
        resultPayInfo.setTradeNo(tradeNo);
        resultPayInfo.setField1(DateTimeUtil.dateToStr(new Date()));
        resultPayInfo.setField2(DateTimeUtil.dateToStr(new Date()));
        resultPayInfo.setUserId(1);

        int row = resultPayInfoMapper.insertSelective(resultPayInfo);
        log.info("添加支付结果信息-row:{}",row);
        if (row > 0) {
            return true;
        }
        return false;
    }
}
