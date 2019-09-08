package com.alag.amall.business.module.message.api.controller;


import com.alag.amall.business.core.common.ServerResponse;
import com.alag.amall.business.core.page.ParamBean;
import com.alag.amall.business.module.message.api.model.TransactionMsg;
import com.github.pagehelper.PageInfo;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("pay_transaction")
public interface TransactionMsgController {

    /**
     * 预存储消息
     */
    @PostMapping("prestore_message")
    ServerResponse saveMessageWaitingConfirm(@RequestBody TransactionMsg TransactionMsg);

    /**
     * 确认并发送消息.
     */
    @PostMapping("confirm_and_send_message")
    ServerResponse confirmAndSendMessage(@RequestParam("messageId") String messageId);

    /**
     * 存储并发送消息.
     * 比如：银行通知，它存储不成功，银行会继续发送
     */
    @PostMapping("save_and_send_message")
    ServerResponse<Integer> saveAndSendMessage(@RequestBody TransactionMsg TransactionMsg);

    /**
     * 直接发送消息.
     * 透传到MQ
     */
    @PostMapping("direct_send_message")
    void directSendMessage(@RequestBody TransactionMsg TransactionMsg);


    /**
     * 重发消息.
     * 对消息恢复系统取到的确认发送的但消费方未消费的消息再次发送
     */
    @PostMapping("r_send_message")
    void reSendMessage(@RequestBody TransactionMsg TransactionMsg);


    /**
     * 根据messageId重发某条消息.
     */
    @PostMapping("r_send_message_by_id")
    void reSendMessageByMessageId(@RequestParam("messageId") String messageId);


    /**
     * 将消息标记为死亡消息.
     */
    @PutMapping("set_message_status")
    void setMessageToAreadlyDead(@RequestParam("messageId") String messageId);


    /**
     * 根据消息ID获取消息
     */
    @GetMapping("get_message")
    ServerResponse<TransactionMsg> getMessageByMessageId(@RequestParam("messageId") String messageId);

    /**
     * 根据消息ID删除消息
     */
    @DeleteMapping("del_message")
    ServerResponse deleteMessageByMessageId(@RequestParam("messageId") String messageId);


    /**
     * 重发某个消息队列中的全部已死亡的消息.
     */
    @PostMapping("r_send_all_message")
    void reSendAllDeadMessageByQueueName(@RequestParam("queueName") String queueName,
                                         @RequestParam("batchSize") int batchSize);

    /**
     * 获取分页数据
     */
    @PostMapping("list_page")
    ServerResponse<PageInfo> listPage(@RequestBody ParamBean paramBean);

}

