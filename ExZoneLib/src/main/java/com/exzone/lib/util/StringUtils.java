package com.exzone.lib.util;

import android.content.Context;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作者:李鸿浩
 * 描述:
 * 时间:2016/7/9.
 */
public class StringUtils {

    private StringUtils() {
        throw new AssertionError();
    }

    /**
     * 获取UUID
     *
     * @return 32UUID小写字符串
     */
    public static String getUUID() {
        String strUUID = UUID.randomUUID().toString();
        strUUID = strUUID.replaceAll("-", "").toLowerCase();
        return strUUID;
    }

    /**
     * 如果字符串为空或它的大小为0,或者是有空格,返回true,否则返回false.
     */
    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }


    /**
     * 两个字符串比较
     */
    public static boolean isEquals(String actual, String expected) {
        return actual.equals(expected);
    }

    /**
     * 字符串长度
     */
    public static int length(CharSequence str) {
        return str == null ? 0 : str.length();
    }

    /**
     * 计算给定的字符串的长度，计算规则是：一个汉字的长度为2，一个字符的长度为1
     *
     * @param string 给定的字符串
     * @return 长度
     */
    public static int length(String string) {
        int length = 0;
        char[] chars = string.toCharArray();
        for (int w = 0; w < string.length(); w++) {
            char ch = chars[w];
            if (ch >= '\u0391' && ch <= '\uFFE5') {
                length++;
                length++;
            } else {
                length++;
            }
        }
        return length;
    }

    /**
     * 验证字符串的长度是在指定范围内
     *
     * @param string    待验证的字符串
     * @param minLength 最小长度（包括）
     * @param maxLength 最大长度（包括）
     */
    public static boolean isLength(String string, int minLength, int maxLength) {
        int length = string.trim().length();
        return (length >= minLength && length <= maxLength);
    }

    /**
     * 验证字符串的长度的最小值
     *
     * @param string    待验证的字符串
     * @param minLength 最小长度（包括）
     */
    public static boolean minLength(String string, int minLength) {
        int length = string.trim().length();
        return (length >= minLength);
    }

    /**
     * 验证字符串的长度的最大值
     *
     * @param string    待验证的字符串
     * @param maxLength 最大长度（包括）
     */
    public static boolean maxLength(String string, int maxLength) {
        int length = string.trim().length();
        return (length <= maxLength);
    }


    /**
     * 字符串链接转换为字符串HTML
     */
    public static String getHtml(String href) {
        if (TextUtils.isEmpty(href)) {
            return "";
        }

        String hrefReg = ".*<[\\s]*a[\\s]*.*>(.+?)<[\\s]*/a[\\s]*>.*";
        Pattern hrefPattern = Pattern.compile(hrefReg, Pattern.CASE_INSENSITIVE);
        Matcher hrefMatcher = hrefPattern.matcher(href);
        if (hrefMatcher.matches()) {
            return hrefMatcher.group(1);
        }
        return href;
    }

    /**
     * 将流转成字符串
     *
     * @param is 输入流
     */
    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 将文件转成字符串
     *
     * @param file 文件
     */
    public static String getStringFromFile(File file) {
        FileInputStream fis = null;
        String string = null;
        try {
            fis = new FileInputStream(file);
            string = convertStreamToString(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            //            if (fis != null) {
            //                try {
            //                    fis.close();
            //                } catch (IOException e) {
            //                    e.printStackTrace();
            //                }
            //            }
            IOUtils.close(fis);
        }
        return string;
    }

    /**
     * 时间转换(hh:mm:ss)
     */
    public static String generateTime(long time) {
        int totalSeconds = (int) (time / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        return hours > 0 ? String.format(Locale.CHINA, "%02d:%02d:%02d", hours, minutes, seconds) : String.format(Locale.CHINA, "%02d:%02d", minutes, seconds);
    }

    /**
     * 为给定的字符串添加HTML红色标记，当使用Html.fromHtml()方式显示到TextView 的时候其将是红色的
     *
     * @param source 给定的字符串
     */
    public static String addHtmlRedFlag(String source) {
        return "<font color=\"red\">" + source + "</font>";
    }

    /**
     * 将给定的字符串中所有给定的关键字标红
     *
     * @param source  给定的字符串
     * @param keyword 给定的关键字
     * @return 返回的是带Html标签的字符串，在使用时要通过Html.fromHtml()转换为Spanned对象再传递给TextView对象
     */
    public static String keywordMadeRed(String source, String keyword) {
        String result = "";
        if (!TextUtils.isEmpty(source)) {
            if (!TextUtils.isEmpty(keyword)) {
                result = source.replaceAll(keyword, "<font color=\"red\">" + keyword + "</font>");
            } else {
                result = source;
            }
        }
        return result;
    }

    /**
     * 在给定的字符串中，用新的字符替换所有旧的字符
     *
     * @param source   给定的字符串
     * @param original 旧的字符
     * @param target   新的字符
     * @return 替换后的字符串
     */
    public static String replace(String source, char original, char target) {
        if (TextUtils.isEmpty(source)) {
            return null;
        }
        char chars[] = source.toCharArray();
        if (chars.length <= 0) {
            return null;
        }
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == original) {
                chars[i] = target;
                break;
            }
        }
        return new String(chars);
    }

    /**
     * 把给定的字符串用给定的字符分割
     *
     * @param source 给定的字符串
     * @param ch     给定的字符
     * @return 分割后的字符串数组
     */
    public static String[] split(String source, char ch) {
        if (TextUtils.isEmpty(source)) {
            return null;
        }
        ArrayList<String> list = new ArrayList<>();
        char chars[] = source.toCharArray();
        int nextStart = 0;
        for (int i = 0; i < chars.length; i++) {
            if (ch == chars[i]) {
                list.add(new String(chars, nextStart, i - nextStart));
                nextStart = i + 1;
                if (nextStart == chars.length) {    //当最后一位是分割符的话，就再添加一个空的字符串到分割数组中去
                    list.add("");
                }
            }
        }
        if (nextStart < chars.length) {    //如果最后一位不是分隔符的话，就将最后一个分割符到最后一个字符中间的左右字符串作为一个字符串添加到分割数组中去
            list.add(new String(chars, nextStart, chars.length - 1 - nextStart + 1));
        }
        return list.toArray(new String[list.size()]);
    }

    /**
     * 删除给定字符串中所有的旧的字符
     *
     * @param source 源字符串
     * @param target 要删除的字符
     * @return 删除后的字符串
     */
    public static String removeChar(String source, char target) {
        if (TextUtils.isEmpty(source)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (char cha : source.toCharArray()) {
            if (cha != target) {
                sb.append(cha);
            }
        }
        return sb.toString();
    }

    /**
     * 删除给定字符串中所有的旧的字符
     *
     * @param source 源字符串
     * @param index  要删除的字符位置
     * @return 删除后的字符串
     */
    public static String removeChar(String source, int index) {
        if (TextUtils.isEmpty(source)) {
            return null;
        }
        String result;
        char[] chars = source.toCharArray();
        if (index == 0) {
            result = new String(chars, 1, chars.length - 1);
        } else if (index == chars.length - 1) {
            result = new String(chars, 0, chars.length - 1);
        } else {
            result = new String(chars, 0, index) + new String(chars, index + 1, chars.length - index);
        }
        return result;
    }

    /**
     * 删除给定字符串中给定位置处的字符
     *
     * @param source 给定字符串
     * @param index  给定位置
     * @param target 如果同给定位置处的字符相同，则将给定位置处的字符删除
     */
    public static String removeChar(String source, int index, char target) {
        if (TextUtils.isEmpty(source)) {
            return null;
        }
        String result;
        char[] chars = source.toCharArray();
        if (chars.length > 0 && chars[index] == target) {
            if (index == 0) {
                result = new String(chars, 1, chars.length - 1);
            } else if (index == chars.length - 1) {
                result = new String(chars, 0, chars.length - 1);
            } else {
                result = new String(chars, 0, index) + new String(chars, index + 1, chars.length - index);
            }
        } else {
            result = source;
        }
        return result;
    }

    /**
     * 将给定字符串中给定的区域的字符转换成小写
     *
     * @param str        给定字符串中
     * @param beginIndex 开始索引（包括）
     * @param endIndex   结束索引（不包括）
     * @return 新的字符串
     */
    public static String toLowerCase(String str, int beginIndex, int endIndex) {
        return str.replaceFirst(str.substring(beginIndex, endIndex), str.substring(beginIndex, endIndex).toLowerCase(Locale.getDefault()));
    }

    /**
     * 将给定字符串中给定的区域的字符转换成大写
     *
     * @param str        给定字符串中
     * @param beginIndex 开始索引（包括）
     * @param endIndex   结束索引（不包括）
     * @return 新的字符串
     */
    public static String toUpperCase(String str, int beginIndex, int endIndex) {
        return str.replaceFirst(str.substring(beginIndex, endIndex), str.substring(beginIndex, endIndex).toUpperCase(Locale.getDefault()));
    }

    /**
     * 将给定字符串的首字母转为小写
     *
     * @param str 给定字符串
     * @return 新的字符串
     */
    public static String firstLetterToLowerCase(String str) {
        return toLowerCase(str, 0, 1);
    }

    /**
     * 将给定字符串的首字母转为大写
     *
     * @param str 给定字符串
     * @return 新的字符串
     */
    public static String firstLetterToUpperCase(String str) {
        return toUpperCase(str, 0, 1);
    }

    /**
     * 判断给定的字符串是否以一个特定的字符串开头，忽略大小写
     *
     * @param source 给定的字符串
     * @param target 一个特定的字符串
     */
    public static boolean startsWithIgnoreCase(String source, String target) {
        int targetLength = target.length();
        int sourceLength = source.length();
        if (targetLength == sourceLength) {
            return target.equalsIgnoreCase(source);
        } else if (targetLength < sourceLength) {
            char[] targetChars = new char[targetLength];
            source.getChars(0, targetLength, targetChars, 0);
            return target.equalsIgnoreCase(String.valueOf(targetChars));
        } else {
            return false;
        }
    }

    /**
     * 判断给定的字符串是否以一个特定的字符串结尾，忽略大小写
     *
     * @param source 给定的字符串
     * @param target 一个特定的字符串
     */
    public static boolean endsWithIgnoreCase(String source, String target) {
        int targetLength = target.length();
        int sourceLength = source.length();
        if (targetLength == sourceLength) {
            return target.equalsIgnoreCase(source);
        } else if (targetLength < sourceLength) {
            char[] targetChars = new char[targetLength];
            source.getChars(sourceLength - targetLength, sourceLength, targetChars, 0);
            return target.equalsIgnoreCase(String.valueOf(targetChars));
        } else {
            return false;
        }
    }

    /**
     * 将byte数组加密成字符串
     */
    public static String encodeToString(byte[] input) {
        try {
            return new String(Base64Utils.encode(input, 0, input.length), "US-ASCII");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new AssertionError(e);
        }
    }

    /**
     * 将加密的字符串解密成byte数组
     */
    public static byte[] decode(String str) {
        byte[] bytes = str.getBytes(Charset.forName("UTF-8"));
        return Base64Utils.decode(bytes, 0, bytes.length);
    }

    /**
     * 使用UTF-8编码
     * <p>
     * <pre>
     * utf8Encode(null)        =   null
     * utf8Encode("")          =   "";
     * utf8Encode("aa")        =   "aa";
     * utf8Encode("啊啊啊啊")   = "%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A";
     * </pre>
     */
    public static String utf8Encode(String str) {
        if (!TextUtils.isEmpty(str) && str.getBytes(Charset.forName("UTF-8")).length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                throw new RuntimeException("UnsupportedEncodingException occurred. ", e);
            }
        }
        return str;
    }

    /**
     * 使用UTF-8编码
     *
     * @param str           需要编码的字符串
     * @param defaultReturn 如果异常,默认的返回字符串
     */
    public static String utf8Encode(String str, String defaultReturn) {
        if (!TextUtils.isEmpty(str) && str.getBytes(Charset.forName("UTF-8")).length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return defaultReturn;
            }
        }
        return str;
    }

    public static String getMoney(float cost) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return "￥" + decimalFormat.format(cost);
    }

    /**
     * 去掉一些特殊字符串
     * String s = "你要去除的字符串";
     * 1.去除空格：s = s.replace('\\s','');
     * 2.去除回车：s = s.replace('\n','');     这样也可以把空格和回车去掉，其他也可以照这样做。
     * 注：\n 回车(\u000a)
     * \t 水平制表符(\u0009)
     * \s 空格(\u0008)
     * \r 换行(\u000d)
     */
    public String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    public static SpannableString format(Context context, String text, int style) {
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new TextAppearanceSpan(context, style), 0, text.length(),
                0);
        return spannableString;
    }


    /**
     * 将字符串进行md5转换
     */
    public static String md5(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(str.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(str.hashCode());
        }
        return cacheKey;
    }

    /**
     * byte数组转换成十六进制字符串
     */
    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(0xFF & aByte);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 清除文本里面的HTML标签
     */
    public static String clearHTMLTag(String htmlStr) {
        if (TextUtils.isEmpty(htmlStr)) {
            return null;
        }
        String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
        String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
        try {
            Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            Matcher m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); // 过滤script标签

            Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            Matcher m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); // 过滤style标签

            Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            Matcher m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); // 过滤html标签
        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }
        return htmlStr; // 返回文本字符串
    }

    /**
     * 字符串转整数
     */
    public static int toInt(String str, int defValue) {
        if (TextUtils.isEmpty(str)) {
            return defValue;
        }
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            e.printStackTrace();
            return defValue;
        }
    }

    /**
     * 对象转整数
     *
     * @return 转换异常返回 0
     */
    public static int toInt(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        return toInt(str, 0);
    }

    /**
     * 对象转整数
     *
     * @return 转换异常返回 0
     */
    public static long toLong(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        try {
            return Long.parseLong(str);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 字符串转布尔值
     *
     * @return 转换异常返回 false
     */
    public static boolean toBool(String b) {
        if (TextUtils.isEmpty(b)) {
            return false;
        }

        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
