package com.alag.amall.business.module.message.server.mapper;


import com.alag.amall.business.core.page.ParamBean;
import com.alag.amall.business.module.message.api.model.TransactionMsg;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TransactionMsgMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table amall_transaction_message
     *
     * @mbggenerated Thu Sep 05 17:49:17 CST 2019
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table amall_transaction_message
     *
     * @mbggenerated Thu Sep 05 17:49:17 CST 2019
     */
    int insert(TransactionMsg record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table amall_transaction_message
     *
     * @mbggenerated Thu Sep 05 17:49:17 CST 2019
     */
    int insertSelective(TransactionMsg record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table amall_transaction_message
     *
     * @mbggenerated Thu Sep 05 17:49:17 CST 2019
     */
    TransactionMsg selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table amall_transaction_message
     *
     * @mbggenerated Thu Sep 05 17:49:17 CST 2019
     */
    int updateByPrimaryKeySelective(TransactionMsg record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table amall_transaction_message
     *
     * @mbggenerated Thu Sep 05 17:49:17 CST 2019
     */
    int updateByPrimaryKeyWithBLOBs(TransactionMsg record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table amall_transaction_message
     *
     * @mbggenerated Thu Sep 05 17:49:17 CST 2019
     */
    int updateByPrimaryKey(TransactionMsg record);


    TransactionMsg getByMessageId(String messageId);

    void delByMsgId(String messageId);

    List<TransactionMsg> selectByQueueName(String queueName);

    List<TransactionMsg> list(ParamBean paramBean);
}