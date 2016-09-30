package com.exzone.lib.util;

import java.util.regex.Pattern;

/**
 * 作者:李鸿浩
 * 描述:正则表达式
 * 时间: 2016/9/30.
 */
public class Validator {
    /**
     * 正则表达式：验证密码
     */
    public static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,16}$";

    /**
     * 正则表达式：验证手机号
     */
    //public static final String REGEX_MOBILE = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
    public static final String REGEX_MOBILE = "^[1][0-9]{10}$";

    /**
     * 正则表达式：验证固话
     */
    public static final String REGEX_PHONE = "(\\+\\d+)?(\\d{3,4}\\-?)?\\d{7,8}$";

    /**
     * 正则表达式：验证邮箱
     */
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    /**
     * 正则表达式：验证身份证
     */
    public static final String REGEX_ID_CARD = "(^\\d{18}$)|(^\\d{15}$)";

    /**
     * 正则表达式：验证邮编
     */
    public static final String REGEX_ID_POST_CARD = "[1-9]\\d{5}";

    /**
     * 校验密码
     */
    public static boolean isPassword(String password) {
        return Pattern.matches(REGEX_PASSWORD, password);
    }

    /**
     * 校验手机号
     */
    public static boolean isMobile(String mobile) {
        return Pattern.matches(REGEX_MOBILE, mobile);
    }
    /**
     * 验证固定电话号码
     *
     * @param phone 电话号码，格式：国家（地区）电话代码 + 区号（城市代码） + 电话号码，如：+86075582378888
     *              国家（地区）代码 ：标识电话号码的国家（地区）的标准国家（地区）代码。它包含从 0 到 9 的一位或多位数字，
     *              数字之后是空格分隔的国家（地区）代码。
     *              区号（城市代码）：这可能包含一个或多个从 0 到 9 的数字，地区或城市代码放在圆括号——
     *              对不使用地区或城市代码的国家（地区），则省略该组件。
     *              电话号码：这包含从 0 到 9 的一个或多个数字
     */
    public static boolean isPhone(String phone) {
        return Pattern.matches(REGEX_PHONE, phone);
    }

    /**
     * 校验邮箱
     */
    public static boolean isEmail(String email) {
        return Pattern.matches(REGEX_EMAIL, email);
    }

    /**
     * 校验身份证
     */
    public static boolean isIDCard(String idCard) {
        return Pattern.matches(REGEX_ID_CARD, idCard);
    }

    /**
     * 匹配中国邮政编码
     */
    public static boolean isPostCode(String postcode) {
        return Pattern.matches(REGEX_ID_POST_CARD, postcode);
    }
}
