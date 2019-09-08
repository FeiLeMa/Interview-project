package com.alag.amall.business.module.message.server.controller;


import com.alag.amall.business.core.common.Const;
import com.alag.amall.business.core.common.ServerResponse;
import com.alag.amall.business.core.page.ParamBean;
import com.alag.amall.business.module.message.api.model.TransactionMsg;
import com.alag.amall.business.module.message.server.exceptions.MessageBizException;
import com.alag.amall.business.module.message.server.service.TMessageService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("pay_transaction")
public class TMessageServerController {
    @Autowired
    private TMessageService tMessageService;

    @PostMapping("prestore_message")
    ServerResponse saveMessageWaitingConfirm(@RequestBody TransactionMsg message) throws MessageBizException {

        ServerResponse ret = verify(message, true,"message","ConsumerQueue");
        if (!ret.isSuccess()) {
            return ret;
        }
        return tMessageService.saveMessage(message);

    }

    @PostMapping("confirm_and_send_message")
    ServerResponse confirmAndSendMessage(@RequestParam("messageId") String messageId) throws MessageBizException {
        return tMessageService.confirmAndSendMsg(messageId);
    }

    @PostMapping("save_and_send_message")
    ServerResponse saveAndSendMessage(@RequestBody TransactionMsg message) {
        ServerResponse ret = verify(message, true,"message","ConsumerQueue");
        if (!ret.isSuccess()) {
            return ret;
        }

        return tMessageService.saveAndSendMsg(message);
    }

    @PostMapping("direct_send_message")
    void directSendMessage(@RequestBody TransactionMsg message) {
        verify(message, false,"message","ConsumerQueue");
        tMessageService.directMsg(message);
    }

    @PostMapping("r_send_message")
    void reSendMessage(@RequestBody TransactionMsg message) {
        verify(message, false,"message","ConsumerQueue");
        tMessageService.reSendMsg(message);
    }

    @PostMapping("r_send_message_by_id")
    void reSendMessageByMessageId(@RequestParam("messageId") String messageId) {
        tMessageService.rSendMsgByMsgId(messageId);
    }

    @PutMapping("set_message_status")
    void setMessageToAreadlyDead(@RequestParam("messageId") String messageId) {
        tMessageService.setMsgToDead(messageId);
    }


    @GetMapping("get_message")
    ServerResponse<TransactionMsg> getMessageByMessageId(@RequestParam("messageId") String messageId) {
        return tMessageService.getMsgByMsgId(messageId);
    }

    @DeleteMapping("del_message")
    void deleteMessageByMessageId(@RequestParam("messageId") String messageId) {
        tMessageService.delMsgByMsgId(messageId);
    }

    @PostMapping("r_send_all_message")
    void reSendAllDeadMessageByQueueName(@RequestParam("queueName") String queueName,
                                         @RequestParam("batchSize") int batchSize) {

        int pageNum = 1;
        int pageSize = 50;
        if (batchSize > 100 && batchSize < 5000){
            pageSize = batchSize;
        } else if (batchSize > 5000){
            pageSize = 5000;
        } else {
            pageSize = 100;
        }
        tMessageService.rSendMsgByBatchSizeAndQName(pageNum,pageSize,queueName);
    }

    @PostMapping("list_page")
    ServerResponse<PageInfo> listPage(@RequestBody ParamBean paramBean){
        return tMessageService.listPage(paramBean);
    }







    /**
     * @param types  传入类型
     * @param message 传入值
     * @param b     抛出异常和返回消息二选一，false表示不返回消息,抛出异常
     * @return
     */
    private ServerResponse verify(TransactionMsg message, boolean b, String... types) throws MessageBizException {

        for (int i = 0; i < types.length; i++) {
            if (StringUtils.equalsIgnoreCase("message", types[i])) {
                if (message == null) {
                    if (b) {
                        return ServerResponse.createByErrorCodeMessage(Const.TMessage.SAVA_MESSAGE_IS_NULL, "保存的消息为空");
                    } else {
                        throw new MessageBizException(MessageBizException.SAVA_MESSAGE_IS_NULL, "该消息为空");
                    }
                }
            }
            if (StringUtils.equalsIgnoreCase("ConsumerQueue", types[i])) {
                if (StringUtils.isBlank(message.getConsumerQueue())) {
                    if (b) {
                        return ServerResponse.createByErrorCodeMessage(Const.TMessage.MESSAGE_CONSUMER_QUEUE_IS_NULL, "消息的消费队列不能为空");
                    } else {
                        throw new MessageBizException(MessageBizException.MESSAGE_CONSUMER_QUEUE_IS_NULL, "消息队列为空");
                    }
                }
            }
        }

        return ServerResponse.createBySuccess();
    }
}
