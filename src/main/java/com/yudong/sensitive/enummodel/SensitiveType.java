package com.yudong.sensitive.enummodel;

/**
 * 脱敏的类型
 * user:zyd.
 * date:2016/8/17.
 * time:15:40.
 */
public enum SensitiveType {
    /**
     * 中文名
     */
    CHINESE_NAME,

    /**
     * 身份证号
     */
    ID_CARD,
    /**
     * 座机号
     */
    FIXED_PHONE,
    /**
     * 手机号
     */
    MOBILE_PHONE,
    /**
     * 地址
     */
    ADDRESS,
    /**
     * 电子邮件
     */
    EMAIL,
    /**
     * 银行卡
     */
    BANK_CARD,
    /**
     * 公司开户银行联号
     */
    CNAPS_CODE,

    /**
     * CVV2脱敏
     */
    CVV2,
    /**
     * 卡有效期
     */
    BANK_CARD_DATE,
    /**
     * 隐藏全部
     */
    ALL;
}
