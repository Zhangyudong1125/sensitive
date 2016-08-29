package com.yudong.sensitive.util;

import org.apache.commons.lang.StringUtils;

/**
 * 敏感属性脱敏
 * user:zyd.
 * date:2016/8/17.
 * time:13:20.
 */
public class EncryptStringUtil {
    private final static String SECRET_SIGN = "*";

    /**
     * 银行卡号脱敏
     *
     * @param bankCardNo 银行卡号
     * @return 脱敏银行卡号
     */
    public static String bankCardNo(String bankCardNo) {
        return encryptString(bankCardNo, 4, 4);
    }

    /**
     * 证件号脱敏
     *
     * @param certificateNo 证件号
     * @return 脱敏证件号
     */
    public static String certificateNo(String certificateNo) {
        return encryptString(certificateNo, 4, 4);
    }

    /**
     * 证件名脱敏
     *
     * @param certificateName 证件名
     * @return 脱敏证件名
     */
    public static String certificateName(String certificateName) {
        return encryptString(certificateName, 0, 1);
    }

    /**
     * 手机号码
     *
     * @param phone 银行预留手机号
     * @return 脱敏银行预留手机号
     */
    public static String phone(String phone) {
        return encryptString(phone, 3, 3);
    }

    /**
     * 信用卡有效期脱敏
     *
     * @param accountValidDate 信用卡有效期
     * @return 脱敏信用卡有效期
     */
    public static String accountValidDate(String accountValidDate) {
        return encryptString(accountValidDate, 1, 1);
    }

    /**
     * 信用卡CVV2脱敏
     *
     * @param cvv2 信用卡CVV2
     * @return 脱敏信用卡CVV2
     */
    public static String cvv2(String cvv2) {
        return encryptString(cvv2, 0, 1);
    }


    /**
     * 屏蔽字符串中间字符,默认以 * 符号屏蔽
     *
     * @param str        原字符串
     * @param leftCount  保留左边位数
     * @param rightCount 保留右边位数
     * @return 屏蔽后的字符串
     */
    public static String encryptString(String str, int leftCount, int rightCount) {
        return encryptString(str, leftCount, rightCount, SECRET_SIGN);
    }

    /**
     * 屏蔽字符串中间字符
     *
     * @param str         原字符串
     * @param leftCount   保留左边位数
     * @param rightCount  保留右边位数
     * @param replaceChar 中间替换的字符
     * @return 屏蔽后的字符串
     */
    public static String encryptString(String str, int leftCount, int rightCount, String replaceChar) {
        if (str == null || str.length() == 0) {
            return "";
        }
        StringBuffer cardNoEn = new StringBuffer();
        //字符串总长度
        int strLength = str.length();
        //请求保留左边位数 + 保留右边位数 长度
        int sumCount = leftCount + rightCount;
        if (strLength <= sumCount) {
            return str;
        }
        if (replaceChar == null || replaceChar.length() == 0) {
            replaceChar = SECRET_SIGN;
        }
        //取前leftCount位
        String cardNoLeft = str.substring(0, leftCount);
        //取后lightCount位
        String cardNoLight = str.substring(strLength - rightCount, strLength);
        //需要补充加密字符
        String strEn = String.format("%" + (strLength - sumCount) + "s", "").replaceAll("\\s", replaceChar);

        return cardNoEn.append(cardNoLeft).append(strEn).append(cardNoLight).toString();
    }

    //----------------------------------------------------------------------------------------------另一种写法

    /**
     * [中文姓名] 只显示第一个汉字，其他隐藏为2个星号<例子：李**>
     *
     * @param fullName
     * @return
     */
    public static String chineseName(String fullName) {
        if (StringUtils.isBlank(fullName)) {
            return "";
        }
        String name = StringUtils.left(fullName, 1);
        return StringUtils.rightPad(name, StringUtils.length(fullName), "*");
    }

    /**
     * [中文姓名] 只显示第一个汉字，其他隐藏为2个星号<例子：李**>
     *
     * @param familyName
     * @param givenName
     * @return
     */
    public static String chineseName(String familyName, String givenName) {
        if (StringUtils.isBlank(familyName) || StringUtils.isBlank(givenName)) {
            return "";
        }
        return chineseName(familyName + givenName);
    }

    /**
     * [身份证号] 显示最后四位，其他隐藏。共计18位或者15位。<例子：*************5762>
     *
     * @param id
     * @return
     */
    public static String idCardNum(String id) {
        if (StringUtils.isBlank(id)) {
            return "";
        }
        String num = StringUtils.right(id, 4);
        return StringUtils.leftPad(num, StringUtils.length(id), "*");
    }

    /**
     * [固定电话] 后四位，其他隐藏<例子：****1234>
     *
     * @param num
     * @return
     */
    public static String fixedPhone(String num) {
        if (StringUtils.isBlank(num)) {
            return "";
        }
        return StringUtils.leftPad(StringUtils.right(num, 4), StringUtils.length(num), "*");
    }

    /**
     * [手机号码] 前三位，后四位，其他隐藏<例子:138******1234>
     *
     * @param num
     * @return
     */
    public static String mobilePhone(String num) {
        if (StringUtils.isBlank(num)) {
            return "";
        }
        return StringUtils.left(num, 3).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(num, 4), StringUtils.length(num), "*"), "***"));
    }

    /**
     * [地址] 只显示到地区，不显示详细地址；我们要对个人信息增强保护<例子：北京市海淀区****>
     *
     * @param address
     * @param sensitiveSize 敏感信息长度
     * @return
     */
    public static String address(String address, int sensitiveSize) {
        if (StringUtils.isBlank(address)) {
            return "";
        }
        int length = StringUtils.length(address);
        return StringUtils.rightPad(StringUtils.left(address, length - sensitiveSize), length, "*");
    }

    /**
     * [电子邮箱] 邮箱前缀仅显示第一个字母，前缀其他隐藏，用星号代替，@及后面的地址显示<例子:g**@163.com>
     *
     * @param email
     * @return
     */
    public static String email(String email) {
        if (StringUtils.isBlank(email)) {
            return "";
        }
        int index = StringUtils.indexOf(email, "@");
        if (index <= 1)
            return email;
        else
            return StringUtils.rightPad(StringUtils.left(email, 1), index, "*").concat(StringUtils.mid(email, index, StringUtils.length(email)));
    }

    /**
     * [银行卡号] 前六位，后四位，其他用星号隐藏每位1个星号<例子:6222600**********1234>
     *
     * @param cardNum
     * @return
     */
    public static String bankCard(String cardNum) {
        if (StringUtils.isBlank(cardNum)) {
            return "";
        }
        return StringUtils.left(cardNum, 6).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(cardNum, 4), StringUtils.length(cardNum), "*"), "******"));
    }

    /**
     * [公司开户银行联号] 公司开户银行联行号,显示前两位，其他用星号隐藏，每位1个星号<例子:12********>
     *
     * @param code
     * @return
     */
    public static String cnapsCode(String code) {
        if (StringUtils.isBlank(code)) {
            return "";
        }
        return StringUtils.rightPad(StringUtils.left(code, 2), StringUtils.length(code), "*");
    }

    /**
     * [银行卡有效期] 前1位，后1位，其他隐藏<例子:“0**6”>
     *
     * @param date
     * @return
     */
    public static String cardValidDate(String date) {
        if (StringUtils.isBlank(date)) {
            return "";
        }
        return StringUtils.left(date, 1).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(date, 1), StringUtils.length(date), "*"), "*"));
    }

    /**
     * 全部隐藏
     *
     * @param all
     * @return
     */
    public static String all(String all) {
        if (StringUtils.isBlank(all)) {
            return "";
        }
        return StringUtils.repeat("*", StringUtils.length(all));
    }
}
