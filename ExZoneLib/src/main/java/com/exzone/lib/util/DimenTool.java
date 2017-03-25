package com.exzone.lib.util;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;

/**
 * 描述:平板是以最小宽度800为基准 手机是以最小宽度320为基准
 * 作者:李鸿浩
 * 时间:2016/12/7.
 */

public class DimenTool {

    public static void gen() {
        File file = new File("./lib/src/main/res/values/dimens.xml");
        BufferedReader reader = null;
        StringBuilder sw320land = new StringBuilder();
        StringBuilder sw480land = new StringBuilder();
        StringBuilder sw600land = new StringBuilder();
        StringBuilder sw720land = new StringBuilder();
        StringBuilder sw800land = new StringBuilder();
        StringBuilder sw1028land = new StringBuilder();
        StringBuilder sw1280land = new StringBuilder();

        StringBuilder w240 = new StringBuilder();
        StringBuilder w320 = new StringBuilder();
        StringBuilder w480 = new StringBuilder();
        StringBuilder w600 = new StringBuilder();
        StringBuilder w720 = new StringBuilder();
        StringBuilder w800 = new StringBuilder();
        //        StringBuilder w820 = new StringBuilder();
        try {
            System.out.println("生成不同分辨率：");
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                if (tempString.contains("</dimen>")) {
                    String start = tempString.substring(0, tempString.indexOf(">") + 1);
                    String end = tempString.substring(tempString.lastIndexOf("<") - 2);
                    int num = Integer.parseInt(tempString.substring(tempString.indexOf(">") + 1, tempString.indexOf("</dimen>") - 2));
                    DecimalFormat df = new DecimalFormat("0.000");
                    sw320land.append(start).append(df.format(1.0f * num * (1.0f * 320 / 800))).append(end).append("\n");
                    sw480land.append(start).append(df.format(1.0f * num * (1.0f * 480 / 800))).append(end).append("\n");
                    sw600land.append(start).append(df.format(1.0f * num * (1.0f * 600 / 800))).append(end).append("\n");
                    sw720land.append(start).append(df.format(1.0f * num * (1.0f * 720 / 800))).append(end).append("\n");
                    sw800land.append(start).append(df.format(1.0f * num * (1.0f * 800 / 800))).append(end).append("\n");
                    sw1028land.append(start).append(df.format(1.0f * num * (1.0f * 1028 / 800))).append(end).append("\n");
                    sw1280land.append(start).append(df.format(1.0f * num * (1.0f * 1280 / 800))).append(end).append("\n");
                    //                    w820.append(tempString).append("\n");
                    w240.append(start).append(df.format(1.0f * num * (1.0f * 240 / 320))).append(end).append("\n");
                    w320.append(start).append(df.format(1.0f * num * (1.0f * 320 / 320))).append(end).append("\n");
                    w480.append(start).append(df.format(1.0f * num * (1.0f * 480 / 320))).append(end).append("\n");
                    w600.append(start).append(df.format(1.0f * num * (1.0f * 600 / 320))).append(end).append("\n");
                    w720.append(start).append(df.format(1.0f * num * (1.0f * 720 / 320))).append(end).append("\n");
                    w800.append(start).append(df.format(1.0f * num * (1.0f * 800 / 320))).append(end).append("\n");
                } else {
                    sw320land.append(tempString).append("\n");
                    sw480land.append(tempString).append("\n");
                    sw600land.append(tempString).append("\n");
                    sw720land.append(tempString).append("\n");
                    sw800land.append(tempString).append("\n");
                    sw1028land.append(tempString).append("\n");
                    sw1280land.append(tempString).append("\n");
                    //                                        w820.append(tempString).append("\n");
                    w240.append(tempString).append("\n");
                    w320.append(tempString).append("\n");
                    w480.append(tempString).append("\n");
                    w600.append(tempString).append("\n");
                    w720.append(tempString).append("\n");
                    w800.append(tempString).append("\n");
                }
                line++;
            }
            reader.close();
            System.out.println("<!--  sw320land -->");
            System.out.println(sw480land);
            System.out.println("<!--  sw320land -->");
            System.out.println(sw480land);
            System.out.println("<!--  sw600land -->");
            System.out.println(sw600land);
            System.out.println("<!--  sw720land -->");
            System.out.println(sw720land);
            System.out.println("<!--  sw800land -->");
            System.out.println(sw800land);
            System.out.println("<!--  sw1028land -->");
            System.out.println(sw1028land);
            System.out.println("<!--  sw1280land -->");
            System.out.println(sw1280land);

            System.out.println("<!--  w240 -->");
            System.out.println(w240);
            System.out.println("<!--  w320 -->");
            System.out.println(w320);
            System.out.println("<!--  w480 -->");
            System.out.println(w480);
            System.out.println("<!--  w600 -->");
            System.out.println(w600);
            System.out.println("<!--  w720 -->");
            System.out.println(w720);
            System.out.println("<!--  w800 -->");
            System.out.println(w800);

            String sw320landfile = "./lib/src/main/res/values-sw320dp-land/dimens.xml";
            String sw480landfile = "./lib/src/main/res/values-sw480dp-land/dimens.xml";
            String sw600landfile = "./lib/src/main/res/values-sw600dp-land/dimens.xml";
            String sw720landfile = "./lib/src/main/res/values-sw720dp-land/dimens.xml";
            String sw800landfile = "./lib/src/main/res/values-sw800dp-land/dimens.xml";
            String sw1028landfile = "./lib/src/main/res/values-sw1028dp-land/dimens.xml";
            String sw1280landfile = "./lib/src/main/res/values-sw1280dp-land/dimens.xml";
            String w240file = "./lib/src/main/res/values-w240dp/dimens.xml";
            String w320file = "./lib/src/main/res/values-w320dp/dimens.xml";
            String w480file = "./lib/src/main/res/values-w480dp/dimens.xml";
            String w600file = "./lib/src/main/res/values-w600dp/dimens.xml";
            String w720file = "./lib/src/main/res/values-w720dp/dimens.xml";
            String w800file = "./lib/src/main/res/values-w800dp/dimens.xml";
            //            String w820file = "./lib/src/main/res/values-w820dp/dimens.xml";
            writeFile(sw320landfile, sw320land.toString());
            writeFile(sw480landfile, sw480land.toString());
            writeFile(sw600landfile, sw600land.toString());
            writeFile(sw720landfile, sw720land.toString());
            writeFile(sw800landfile, sw800land.toString());
            writeFile(sw1028landfile, sw1028land.toString());
            writeFile(sw1280landfile, sw1280land.toString());
            writeFile(w240file, w240.toString());
            writeFile(w320file, w320.toString());
            writeFile(w480file, w480.toString());
            writeFile(w600file, w600.toString());
            writeFile(w720file, w720.toString());
            writeFile(w800file, w800.toString());
            //            writeFile(w820file, w820.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public static void writeFile(String file, String text) {
        if (TextUtils.isEmpty(file) || TextUtils.isEmpty(text)) {
            return;
        }
        PrintWriter out = null;
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            out.println(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (out != null) {
            out.close();
        }
    }

    public static void main(String[] args) {
        gen();
    }
}
