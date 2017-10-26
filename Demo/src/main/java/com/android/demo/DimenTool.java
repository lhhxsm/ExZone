package com.android.demo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;

/**
 * 作者:lhh
 * 描述:
 * 时间:2017/5/24.
 */
public class DimenTool {
  public static void gen() {

    File file = new File("./demo/src/main/res/values/dimens.xml");
    BufferedReader reader = null;
    StringBuilder sw320 = new StringBuilder();
    StringBuilder sw360 = new StringBuilder();
    StringBuilder sw384 = new StringBuilder();
    StringBuilder sw400 = new StringBuilder();
    StringBuilder sw480 = new StringBuilder();
    StringBuilder sw600 = new StringBuilder();
    StringBuilder sw640 = new StringBuilder();
    StringBuilder sw720 = new StringBuilder();
    StringBuilder sw800 = new StringBuilder();
    StringBuilder sw820 = new StringBuilder();

    try {
      System.out.println("生成不同分辨率：");
      reader = new BufferedReader(new FileReader(file));
      String tempString;
      int line = 1;
      DecimalFormat df = new DecimalFormat("0.00000");
      while ((tempString = reader.readLine()) != null) {

        if (tempString.contains("</dimen>")) {
          String start = tempString.substring(0, tempString.indexOf(">") + 1);
          String end = tempString.substring(tempString.lastIndexOf("<") - 2);
          float num = Float.valueOf(tempString.substring(tempString.indexOf(">") + 1,
              tempString.indexOf("</dimen>") - 2));
          sw320.append(start).append(df.format(num * 1.00000f)).append(end).append("\n");
          sw360.append(start).append(df.format(num * 1.12500f)).append(end).append("\n");
          sw384.append(start).append(df.format(num * 1.20000f)).append(end).append("\n");
          sw400.append(start).append(df.format(num * 1.25000f)).append(end).append("\n");
          sw480.append(start).append(df.format(num * 1.5000f)).append(end).append("\n");
          sw600.append(start).append(df.format(num * 1.87500f)).append(end).append("\n");
          sw640.append(start).append(df.format(num * 2.00000f)).append(end).append("\n");
          sw720.append(start).append(df.format(num * 2.25000f)).append(end).append("\n");
          sw800.append(start).append(df.format(num * 2.50000f)).append(end).append("\n");
          sw820.append(start).append(df.format(num * 2.56250f)).append(end).append("\n");
        } else {
          sw320.append(tempString).append("\n");
          sw360.append(tempString).append("\n");
          sw384.append(tempString).append("\n");
          sw400.append(tempString).append("\n");
          sw480.append(tempString).append("\n");
          sw600.append(tempString).append("\n");
          sw640.append(tempString).append("\n");
          sw720.append(tempString).append("\n");
          sw800.append(tempString).append("\n");
          sw820.append(tempString).append("\n");
        }
        line++;
      }
      reader.close();

      String sw320File = "./demo/src/main/res/values-sw320dp/dimens.xml";
      String sw360File = "./demo/src/main/res/values-sw360dp/dimens.xml";
      String sw384File = "./demo/src/main/res/values-sw384dp/dimens.xml";
      String sw400File = "./demo/src/main/res/values-sw400dp/dimens.xml";
      String sw480File = "./demo/src/main/res/values-sw480dp/dimens.xml";
      String sw600File = "./demo/src/main/res/values-sw600dp/dimens.xml";
      String sw640File = "./demo/src/main/res/values-sw640dp/dimens.xml";
      String sw720File = "./demo/src/main/res/values-sw720dp/dimens.xml";
      String sw800File = "./demo/src/main/res/values-sw800dp/dimens.xml";
      String sw820File = "./demo/src/main/res/values-sw820dp/dimens.xml";
      writeFile(sw320File, sw320.toString());
      writeFile(sw360File, sw360.toString());
      writeFile(sw384File, sw384.toString());
      writeFile(sw400File, sw400.toString());
      writeFile(sw480File, sw480.toString());
      writeFile(sw600File, sw600.toString());
      writeFile(sw640File, sw640.toString());
      writeFile(sw720File, sw720.toString());
      writeFile(sw800File, sw800.toString());
      writeFile(sw820File, sw820.toString());
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
