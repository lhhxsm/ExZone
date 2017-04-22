package com.exzone.lib.util;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * 作者:李鸿浩
 * 描述:日期工具类
 * 时间：2016/10/9.
 */
public class DateUtils {

    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat(
            "yyyy-MM-dd");

    private DateUtils() {
        throw new AssertionError();
    }

    /**
     * 获取当前时间戳
     *
     * @return 时间戳
     */
    public static long getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis();
    }

    /**
     * 格式化12小时制<br>
     * 格式：yyyy-MM-dd hh-MM-ss
     */
    public static String format12Time(long time) {
        return format(time, "yyyy-MM-dd hh:MM:ss");
    }

    /**
     * 格式化24小时制<br>
     * 格式：yyyy-MM-dd HH-MM-ss
     */
    public static String format24Time(long time) {
        return format(time, "yyyy-MM-dd HH:MM:ss");
    }

    /**
     * 格式化时间,自定义标签
     *
     * @param time    时间
     * @param pattern 格式化时间用的标签
     */
    public static String format(long time, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.CHINA);
        return sdf.format(new Date(time));
    }

    /**
     * 获取当前天
     *
     * @return 天
     */
    @SuppressWarnings("static-access")
    public static int getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        return calendar.DAY_OF_MONTH;
    }

    /**
     * 获取当前月
     *
     * @return 月
     */
    @SuppressWarnings("static-access")
    public static int getCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.MONTH;
    }

    /**
     * 获取当前年
     *
     * @return 年
     */
    @SuppressWarnings("static-access")
    public static int getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.YEAR;
    }

    /**
     * 获取当天日期
     *
     * @return
     */
    public static String getCurrDate() {
        Date currDate = new Date();
        return new SimpleDateFormat("yyyy-MM-dd").format(currDate);
    }

    /**
     * 获取日期字符串
     *
     * @return
     */
    public static String getDate(Date currDate) {
        return new SimpleDateFormat("yyyy-MM-dd").format(currDate);
    }

    /**
     * 获取当前时间 精确到秒
     */
    public static String getCurrentDateToSecond() {
        Date currDate = new Date();
        return new SimpleDateFormat("yyyyMMddHHmmss").format(currDate);
    }

    /**
     * 获取当前时间 精确到秒
     */
    public static String getCurrentDateToSecond1() {
        Date currDate = new Date();
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currDate);
    }

    /**
     * 获取当前时间 精确到毫秒
     */
    public static String getCurrentDateToMillisecond() {
        Date currDate = new Date();
        return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(currDate);
    }

    /**
     * 获取两个日期之间的时间差是否超过N天
     *
     * @return 不超过返回true 超过返回false
     */
    public static boolean getTimeDifference(String startDate, String endDate,
                                            int n) {
        Date start = new Date();
        Date end = new Date();
        try {
            start = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
            end = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(start);
        calendar.add(Calendar.DATE, n);
        start = calendar.getTime();

        return start.after(end);
    }

    public static boolean getTimeDifferenceByYear(String startDate,
                                                  String endDate) {
        Date start = new Date();
        Date end = new Date();
        try {
            start = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
            end = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(start);
        calendar.add(Calendar.YEAR, 1);
        start = calendar.getTime();
        return start.after(end);
    }

    /**
     * 获取日期是否超过当前日期前一年
     *
     * @return 未超过返回true ，超过返回false
     */
    public static boolean getTimeDifferenceByYear(String date) {

        return getTimeDifference(date, getCurrDate(), 365);
    }

    /**
     * 获取三个月前的第一天
     *
     * @return
     */
    public static String getThreeMonthAgoDate(String dateStr) {
        Date today = new Date();
        try {
            today = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) - 2, 1);
        return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
    }

    /**
     * 获取1个月前的第一天
     *
     * @return
     */
    public static String getoneMonthAgoDate(String dateStr) {
        Date today = new Date();
        try {
            today = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                1);
        return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
    }

    public static String getoneMonthAgoDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar date = Calendar.getInstance(Locale.CHINA);
        date.add(Calendar.MONTH, -1);
        date.add(Calendar.DAY_OF_MONTH, 1);
        return sdf.format(date.getTime());
    }

    /**
     * 获取当月的第一天
     *
     * @return
     */
    public static String getCurrMonthDate(String dateStr) {
        Date today = new Date();
        try {
            today = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                1);
        return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
    }

    /**
     * 获取下个月的第一天
     *
     * @return
     */
    public static String getNextMonthDate(String dateStr) {
        Date today = new Date();
        try {
            today = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1, 1);
        return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
    }

    /**
     * 获取n天后的日期
     *
     * @return
     */
    public static String getNDaysDate(String dateStr, int n) {
        // dateStr
        Date date = new Date();
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, n);
        date = calendar.getTime();
        SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formate.format(date);
        return dateString;
    }

    /**
     * 获取n天前的日期
     *
     * @return
     */
    public static String getNDays_PreDate(String dateStr, int n) {
        // dateStr
        Date date = new Date();
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.roll(Calendar.DATE, n);
        date = calendar.getTime();
        SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formate.format(date);
        return dateString;
    }

    /**
     * 比较两个日期的大小
     *
     * @param oldDateStr
     * @param newDateStr
     * @return true newDateStr小于先于oldDateStr
     */
    public static boolean compareDate(String oldDateStr, String newDateStr) {
        Date date1 = new Date();
        Date date2 = new Date();
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .parse(newDateStr);
            date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .parse(oldDateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (date1.before(date2)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 比较两个日期的大小(去除时分秒)
     *
     * @param oldDateStr
     * @param newDateStr
     * @return true newDateStr小于先于oldDateStr
     */
    public static boolean compareDateIgnoreHMS(String oldDateStr,
                                               String newDateStr) {
        if (StringUtils.isBlank(oldDateStr) || StringUtils.isBlank(newDateStr)) {
            return false;
        }
        // if (oldDateStr.length() > 8) {
        // oldDateStr = oldDateStr.substring(0, 7);
        // }
        // if (newDateStr.length() > 8) {
        // newDateStr = newDateStr.substring(0, 7);
        // }
        Date date1 = new Date();
        Date date2 = new Date();
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd").parse(newDateStr);
            date2 = new SimpleDateFormat("yyyy-MM-dd").parse(oldDateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (date1.before(date2)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 计算两个时期时间差(时分秒)
     *
     * @param oldDateStr
     * @param newDateStr
     * @return 时间差
     */
    public static String countDate(String oldDateStr, String newDateStr) {

        Date date1 = new Date();
        Date date2 = new Date();
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .parse(newDateStr);
            date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .parse(oldDateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long diff;
        if (date1.getTime() > date2.getTime()) {
            diff = date1.getTime() - date2.getTime();
        } else {
            diff = 0;
        }
        String diffDate = millisToDate(diff).toString();
        return diffDate;
    }

    // 格式化日期和时间函数
    public static String formatDateTime(String sourceDate) {
        String resultDate = "";
        try {
            String year = sourceDate.substring(0, 4);
            String month = sourceDate.substring(4, 6);
            String day = sourceDate.substring(6, 8);
            String hour = sourceDate.substring(8, 10);
            String min = sourceDate.substring(10, 12);
            String sec = sourceDate.substring(12, 14);
            resultDate = year + "-" + month + "-" + day + "  " + hour + ":"
                    + min + ":" + sec;
        } catch (Exception e) {
            resultDate = "";
        } finally {

        }
        return resultDate;
    }

    // 格式化日期和时间函数
    public static String formatDateTime1(String sourceDate) {
        String resultDate = "";
        try {
            String year = sourceDate.substring(0, 4);
            String month = sourceDate.substring(4, 6);
            if (month.startsWith("0")) {
                month = month.substring(1);
            }
            String day = sourceDate.substring(6, 8);
            if (day.startsWith("0")) {
                day = day.substring(1);
            }
            String hour = sourceDate.substring(8, 10);
            String min = sourceDate.substring(10, 12);
            String sec = sourceDate.substring(12, 14);
            resultDate = year + "年" + month + "月" + day + "日  " + hour + "时"
                    + min + "分" + sec + "秒";
        } catch (Exception e) {
            resultDate = "";
        } finally {

        }
        return resultDate;
    }

    // 格式化日期函数
    public static String formatDate(String sourceDate) {
        String resultDate = "";
        if (StringUtils.isBlank(sourceDate)) {
            return "";
        }
        if (sourceDate.length() > 8) {
            sourceDate = sourceDate.substring(0, 8);
        }
        try {
            String year = sourceDate.substring(0, 4);
            String month = sourceDate.substring(4, 6);
            String day = sourceDate.substring(6, 8);
            resultDate = year + "-" + month + "-" + day;
        } catch (Exception e) {
            resultDate = "";
        } finally {

        }
        return resultDate;
    }

    // 格式化日期函数
    public static String formatDate1(String sourceDate) {
        String resultDate = "";
        try {
            String year = sourceDate.substring(0, 4);
            String month = sourceDate.substring(4, 6);
            if (month.startsWith("0")) {
                month = month.substring(1);
            }
            String day = sourceDate.substring(6, 8);
            if (day.startsWith("0")) {
                day = day.substring(1);
            }
            resultDate = year + "年" + month + "月" + day + "日";
        } catch (Exception e) {
            resultDate = "";
        } finally {

        }
        return resultDate;
    }

    // 格式化时间的函数
    public static String formatTime(String sourceDate) {
        String resultDate = "";
        try {
            String year = sourceDate.substring(0, 2);
            String month = sourceDate.substring(2, 4);
            String day = sourceDate.substring(4, 6);
            resultDate = year + "：" + month + "：" + day;
        } catch (Exception e) {
            resultDate = "";
        } finally {

        }
        return resultDate;
    }

    /**
     * 倒计时
     *
     * @param date
     * @param dateStr
     */
    public static void countDownTimer(final TextView date, String dateStr) {
        new CountDownTimer(dateToMillis(dateStr), 1000) {

            // 倒计时开始时的操作
            public void onTick(long millisUntilFinished) {
                SpannableStringBuilder style = new SpannableStringBuilder(
                        millisToDate(millisUntilFinished));
                int hour = millisToDate(millisUntilFinished).indexOf("小");
                int min = millisToDate(millisUntilFinished).indexOf("分");
                int sec = millisToDate(millisUntilFinished).indexOf("秒");
                // 设置字体颜色
                style.setSpan(new ForegroundColorSpan(Color.rgb(197, 116, 1)),
                        0, hour, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                style.setSpan(new ForegroundColorSpan(Color.rgb(197, 116, 1)),
                        hour + 2, min, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                style.setSpan(new ForegroundColorSpan(Color.rgb(197, 116, 1)),
                        min + 1, sec, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                // 设置字体大小
                style.setSpan(new RelativeSizeSpan(1.3f), 0, hour,
                        Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                style.setSpan(new RelativeSizeSpan(1.3f), hour + 2, min,
                        Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                style.setSpan(new RelativeSizeSpan(1.3f), min + 1, sec,
                        Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                date.setText(style);
            }

            // 做倒计时完成后的处理
            public void onFinish() {
                date.setVisibility(View.GONE);
            }
        }.start();
    }

    public static Long dateToMillis(String dateString) {
        // String hour = dateString.substring(0,dateString.indexOf("小"));
        // String minute = dateString.substring(dateString.indexOf("时") + 1,
        // dateString.indexOf("分"));
        // String second = dateString.substring(dateString.indexOf("分") + 1,
        // dateString.indexOf("秒"));
        // 截取字符串长度超过两位出错
        // String hour = dateString.substring(0,2);
        // String minute = dateString.substring(3,5);
        // String second = dateString.substring(6,8);
        String[] temp = dateString.split(":");
        String hour = temp[0];
        String minute = temp[1];
        String second = temp[2];

        long millisInFuture = ((Long.valueOf(hour)) * 60 + Long.valueOf(minute))
                * 60 + Long.valueOf(second);

        return millisInFuture * 1000;
    }

    public static String millisToDate(Long millis) {
        Long temp = millis / 1000;
        Long second = temp % 60;
        temp = temp / 60;
        Long minute = temp % 60;
        temp = temp / 60;
        Long hour = temp;

        // String date = String.format("%d小时%d分%d秒", hour, minute, second);
        String hourFormat = StringUtils.leftPad(String.valueOf(hour), 1, '0');
        String minuteFormat = StringUtils.leftPad(String.valueOf(minute), 1,
                '0');
        String secondFormat = StringUtils.leftPad(String.valueOf(second), 2,
                '0');
        String date = null;
        if (hourFormat.equals("0") && minuteFormat.equals("0")) {
            date = "2小时";
        } else if (hourFormat.equals("0")) {
            date = "1小时" + (60 - Integer.parseInt(minuteFormat)) + "分";
        } else if (minuteFormat.equals("0")) {
            date = "1小时";
        } else {
            date = (2 - Integer.parseInt(hourFormat)) + "小时"
                    + (60 - Integer.parseInt(minuteFormat)) + "分";
        }

        // else if(hourFormat.equals("2")){
        // date = hourFormat+"小时";
        // } else {
        // date = hourFormat+"小时"+minuteFormat+"分";
        // }

        return date;
    }

    /**
     * 倒计时处理最新
     *
     * @param date
     * @param dateStr
     */
    public static void countDownTimerNew(final TextView date, String dateStr) {
        new CountDownTimer(dateToMillis(dateStr), 1000) {

            // 倒计时开始时的操作
            public void onTick(long millisUntilFinished) {
                SpannableStringBuilder style = new SpannableStringBuilder(
                        millisToDate(millisUntilFinished));

                // 设置字体颜色
                style.setSpan(new ForegroundColorSpan(Color.rgb(197, 116, 1)),
                        0, 2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                style.setSpan(new ForegroundColorSpan(Color.rgb(197, 116, 1)),
                        3, 5, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                style.setSpan(new ForegroundColorSpan(Color.rgb(197, 116, 1)),
                        6, 8, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                // 设置字体大小
                style.setSpan(new RelativeSizeSpan(1.5f), 0, 2,
                        Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                style.setSpan(new RelativeSizeSpan(1.5f), 3, 5,
                        Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                style.setSpan(new RelativeSizeSpan(1.5f), 6, 8,
                        Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                date.setText(style);
            }

            // 做倒计时完成后的处理
            public void onFinish() {
                date.setVisibility(View.GONE);
            }
        }.start();
    }

    /**
     * long time to string
     *
     * @param timeInMillis
     * @param dateFormat
     * @return
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    /**
     * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @param timeInMillis
     * @return
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @return
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }

    /**
     * 获取岁数
     *
     * @param birthday
     * @return
     */
    public static String getAge(String birthday) {
        String date = getCurrDate();
        String year = date.split("-")[0];
        int age = Integer.parseInt(year)
                - Integer.parseInt(birthday.split("-")[0]);
        if (age == 0) {
            return "1";
        }
        return age + "";
    }
}
