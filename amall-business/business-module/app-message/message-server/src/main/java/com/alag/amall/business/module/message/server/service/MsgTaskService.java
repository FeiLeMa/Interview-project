package com.alag.amall.business.module.message.server.service;

public interface MsgTaskService {
    void handleWaitConfirmMessage();

    void handleConfirmSendingMessage();
}
