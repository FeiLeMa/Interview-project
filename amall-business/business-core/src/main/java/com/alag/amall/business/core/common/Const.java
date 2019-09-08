package com.alag.amall.business.core.common;

import com.google.common.collect.Sets;

import java.util.Set;

public class Const {
    public static final String TOKENP_RREFIX = "token_";
    public static final String CURRENT_USER = "currentUser";

    public static final String EMAIL = "email";
    public static final String USERNAME = "username";
    public static final String PHONE = "phone";

    public interface ProductListOrderBy {
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc", "price_asc");
    }
    public interface Property{
        String MERCHANT_URL = "merchant.returnUrl";
        String MERCHANT_NO = "merchant_no";
    }
    public interface REDIS_LOCK {
        String CLOSE_ORDER_TASK_LOCK = "CLOSE_ORDER_TASK_LOCK";//关闭订单的分布式锁
    }

    public interface Cart {
        int CHECKED = 1;//即购物车选中状态
        int UN_CHECKED = 0;//购物车中未选中状态

        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
    }

    public interface Role {
        int ROLE_CUSTOMER = 0; //普通用户
        int ROLE_ADMIN = 1;//管理员
    }

    public enum ProductStatusEnum {
        ON_SALE(1, "在线");
        private String value;
        private int code;

        ProductStatusEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }
    }


    public enum OrderStatusEnum {
        CANCELED(0, "已取消"),
        NO_PAY(10, "未支付"),
        PAID(20, "已付款"),
        SHIPPED(40, "已发货"),
        ORDER_SUCCESS(50, "订单完成"),
        ORDER_CLOSE(60, "订单关闭");


        OrderStatusEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        private String value;
        private int code;

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }

        public static OrderStatusEnum codeOf(int code) {
            for (OrderStatusEnum orderStatusEnum : values()) {
                if (orderStatusEnum.getCode() == code) {
                    return orderStatusEnum;
                }
            }
            throw new RuntimeException("没有有找到对应的枚举");
        }
    }

    public interface AlipayCallback {
        String TRADE_STATUS_WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
        String TRADE_STATUS_TRADE_SUCCESS = "TRADE_SUCCESS";

        String RESPONSE_SUCCESS = "success";
        String RESPONSE_FAILED = "failed";
    }


    public enum PayPlatformEnum {
        ALIPAY(1, "支付宝");

        PayPlatformEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        private String value;
        private int code;

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }
    }

    public enum PaymentTypeEnum {
        ONLINE_PAY(1, "在线支付");

        PaymentTypeEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        private String value;
        private int code;

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }


        public static PaymentTypeEnum codeOf(int code) {
            for (PaymentTypeEnum paymentTypeEnum : values()) {
                if (paymentTypeEnum.getCode() == code) {
                    return paymentTypeEnum;
                }
            }
            throw new RuntimeException("没有找到对应的枚举");
        }

    }

    public interface TMessage {
        /**
         * messageId 前缀
         */
        String ORDER_MSG_ID_PRE = "Order";
        String PAYINFO_MSG_ID_PRE = "PayInfo";

        /**
         * remark
         */
        String REMARK = "测试版";
        /**
         * 消息体数据类型
         */
        String MESSAGE_DATA_TYPE = "json";
        /**
         * 保存的消息为空
         **/
        int SAVA_MESSAGE_IS_NULL = 100;

        /**
         * 消息的消费队列为空
         **/
        int MESSAGE_CONSUMER_QUEUE_IS_NULL = 110;

        int SAVA_MESSAGEID_IS_NULL = 120;
        /**
         * 已死亡
         */
        int AREADLY_DEAD_NO = 500;
        int AREADLY_DEAD_YES = 600;

        /**
         * 重发次数
         */
        int RE_SENT_TIMES_ZEROTH = 0;

        /**
         * 最大重发次数
         */
        int MESSAGE_MAX_SEND_TIMES = 5;

        /**
         * 消息队列名称
         */
        String ORDER_QUEUE_NAME = "ORDER_NOTIFY";
        String PAYINFO_QUEUE_NAME = "PAYINFO_NOTIFY";
        String MERCHANT_QUEUE_NAME = "MERCHANT_NOTIFY";
        /**
         * 创建者
         */
        String CREATOR_NAME = "alag";
        /**
         * 修改者
         */
        String EDITOR_NAME = "alag";

        /**
         * 消息超时时间s
         */
        int MESSAGE_HANDLE_DURATION = 60;

        /**
         * 重发超时时间间隔s
         */
        int FIRST_SEND_TIMES = 0;
        int SECOND_SEND_TIMES = 1;
        int THIRD_SEND_TIMES = 2;
        int FOURTH_SEND_TIMES = 5;
        int FIFTH_SEND_TIMES = 15;

    }

    public enum MessageEnum {
        WAITING_CONFIRM(300, "待确认"),
        SENDING(400, "发送中");

        MessageEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        private String value;
        private int code;

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }

        public static MessageEnum codeOf(int code) {
            for (MessageEnum messageEnum : values()) {
                if (messageEnum.getCode() == code) {
                    return messageEnum;
                }
            }
            throw new RuntimeException("没有找到对应的枚举");
        }
    }

}
