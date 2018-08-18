package com.mmall.common;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * @author Luyue
 * @date 2018/7/29 20:58
 **/
public class Constants {

    public static final String CURRENT_USER = "current_user";

    public static final String EMAIL = "email";

    public static final String USERNAME = "username";

    public static final String TOKEN_PREFIX = "token_";

    public interface RedisExTime {
        int EX_TIME = 60 * 20;
    }

    public enum SALE_STATUS {
        /**
         * 商品状态：上架
         */
        STATUS_ONLINE(1, "商品上架中");

        private Integer status;
        private String desc;

        SALE_STATUS(Integer status, String desc) {
            this.status = status;
            this.desc = desc;
        }

        public Integer getStatus() {
            return status;
        }

        public String getDesc() {
            return desc;
        }
    }

    public enum OrderStatusEnum {
        /**
         * 订单状态：已取消
         */
        CANCELED(0, "已取消"),
        NO_PAY(10, "未支付"),
        PAID(20, "已付款"),
        SHIPPED(40, "已发货"),
        ORDER_SUCCESS(50, "订单完成"),
        ORDER_CLOSE(60, "订单关闭");

        private Integer status;
        private String desc;

        OrderStatusEnum(Integer status, String desc) {
            this.status = status;
            this.desc = desc;
        }

        public static OrderStatusEnum codeOf(Integer code) {
            for (OrderStatusEnum orderStatusEnum : values()) {
                if (orderStatusEnum.getStatus().equals(code)) {
                    return orderStatusEnum;
                }
            }
            throw new RuntimeException("未找到该枚举");
        }

        public Integer getStatus() {
            return status;
        }

        public String getDesc() {
            return desc;
        }
    }

    public enum PayPlatFormEnum {
        /*
        支付工具
         */
        ALIPAY(1, "支付宝");

        private Integer status;
        private String desc;

        PayPlatFormEnum(Integer status, String desc) {
            this.status = status;
            this.desc = desc;
        }

        public Integer getStatus() {
            return status;
        }

        public String getDesc() {
            return desc;
        }
    }

    public enum PayMentTypeEnum {
        /**
         * 付款方式
         */
        ONLINE_PAY(1, "在线支付");

        private Integer status;
        private String desc;

        PayMentTypeEnum(Integer status, String desc) {
            this.status = status;
            this.desc = desc;
        }

        public static PayMentTypeEnum codeOf(Integer code) {
            for (PayMentTypeEnum payMentTypeEnum : values()) {
                if (payMentTypeEnum.getStatus().equals(code)) {
                    return payMentTypeEnum;
                }
            }
            throw new RuntimeException("未找到该枚举");
        }

        public Integer getStatus() {
            return status;
        }

        public String getDesc() {
            return desc;
        }
    }

    public interface Cart {
        /**
         * 选中
         */
        int CHECKED = 1;
        /**
         * 未选中
         */
        int UN_CHECKED = 0;

        String LIMIT_QUANTITY_SUCCESS = "LIMIT_QUANTITY_SUCCESS";
        String LIMIT_QUANTITY_FAIL = "LIMIT_QUANTITY_FAIL";
    }

    public interface ProductListOrderBy {
        Set<String> sets = Sets.newHashSet("price_asc", "price_desc");
    }

    public interface ROLE {
        int ROLE_CUSTOMER = 0;
        int ROLE_ADMIN = 1;
    }

    public interface AliPayCallBack {
        String TRADE_STATUS_WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
        String TRADE_STATUS_TRADE_SUCCESS = "TRADE_SUCCESS";

        String RESPONSE_SUCCESS = "success";
        String RESPONSE_FAILED = "failed";
    }

    public interface RedisLock {
        String CLOSE_ORDER_LOCK_KEY = "CLOSE_ORDER_LOCK_KEY";
    }
}
