package com.exzone.lib.util;

import java.util.regex.Pattern;

/**
 * 作者:李鸿浩
 * 描述:正则表达式
 * 时间: 2016/9/30.
 */
public class Validator {
    /**
     * 正则表达式：验证用户名
     */
    public static final String REGEX_USERNAME = "^[a-zA-Z0-9_\u4e00-\u9fa5]+$";
    /**
     * 正则表达式：验证密码
     */
    public static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,16}$";

    /**
     * 正则表达式：验证手机号
     */
    public static final String REGEX_MOBILE = "^[1][0-9]{10}$";

    /**
     * 正则表达式：验证固话
     */
    public static final String REGEX_PHONE = "(\\+\\d+)?(\\d{3,4}\\-?)?\\d{7,8}$";

    /**
     * 正则表达式：验证邮箱
     */
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
//    public static final String REGEX_EMAIL = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

    /**
     * 正则表达式：验证身份证
     */
    public static final String REGEX_ID_CARD = "(^\\d{18}$)|(^\\d{15}$)";
//    public static final String REGEX_ID_CARD = "^(^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$)|(^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[Xx])$)$";

    /**
     * 正则表达式：验证邮编
     */
    public static final String REGEX_ID_POST_CARD = "[1-9]\\d{5}";

    /**
     * 正则表达式:验证汉字,个数限制为一个或多个
     */
    public static final String REGEX_CHINESE = "^[\u4e00-\u9f5a]+$";
    /**
     * 正则表达式：验证正整数,个数限制为一个或多个
     */
    public static final String REGEX_POSITIVE_INTEGER = "^\\d+$";
    /**
     * 正则表达式：验证URL
     */
    public static final String REGEX_URL = "^(([hH][tT]{2}[pP][sS]?)|([fF][tT][pP]))\\:\\/\\/[wW]{3}\\.[\\w-]+\\.\\w{2,4}(\\/.*)?$";
    /**
     * 正则表达式:验证全网IP
     */
    public static final String REGEX_IP = "^((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))$";

    /**
     * 校验用户名：用户名只能由汉字、数字、字母、下划线组成，且不能为空.
     */
    public static boolean isUsername(String username) {
        return Pattern.matches(REGEX_USERNAME, username);
    }

    /**
     * 校验密码:
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

    /**
     * 匹配给定的字符串是否是一个全网IP
     */
    public static boolean isIp(String string) {
        return Pattern.matches(REGEX_IP, string);
    }

    /**
     * 匹配给定的字符串是否全部由汉子组成
     */
    public static boolean isChinese(String string) {
        return Pattern.matches(REGEX_CHINESE, string);
    }

    /**
     * 验证给定的字符串是否全部由正整数组成
     */
    public static boolean isPositiveInteger(String string) {
        return Pattern.matches(REGEX_POSITIVE_INTEGER, string);
    }

    /**
     * 验证给定的字符串是否是URL，仅支持http、https、ftp
     */
    public static boolean isURL(String string) {
        return Pattern.matches(REGEX_URL, string);
    }
}
