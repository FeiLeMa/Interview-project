package com.alag.amall.business.module.order.server.service.impl;

import com.alag.amall.business.core.common.Const;
import com.alag.amall.business.core.common.ServerResponse;
import com.alag.amall.business.module.message.feign.controller.TransactionMsgFeign;
import com.alag.amall.business.module.order.api.model.Order;
import com.alag.amall.business.module.order.server.mapper.OrderMapper;
import com.alag.amall.business.module.order.server.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private TransactionMsgFeign transactionMsgFeign;


    @Override
    public ServerResponse<Order> getOrderByOrderNo(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null) {
            log.info("订单不存在-orderNo:{}",orderNo);
            return ServerResponse.createByError();
        }
        return ServerResponse.createBySuccess(order);
    }

    @Override
    public ServerResponse updateOrderStatus(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null) {
            log.info("订单不存在-orderNo:{}",orderNo);
            return ServerResponse.createByError();
        }
        if (order.getStatus() == Const.OrderStatusEnum.PAID.getCode()) {
            log.info(("订单状态已经修改，请忽略！"));
            transactionMsgFeign.deleteMessageByMessageId(Const.TMessage.ORDER_MSG_ID_PRE + orderNo);
            return ServerResponse.createBySuccess();
        }
        int row = orderMapper.updateOrderStatusByOrderNo(orderNo);
        if (row > 0) {
            transactionMsgFeign.deleteMessageByMessageId(Const.TMessage.ORDER_MSG_ID_PRE + orderNo);
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }
}
