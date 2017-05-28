package com.android.demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/**
 * 作者:李鸿浩
 * 描述:
 * 时间:2017/5/25.
 */
public class GenerateValueDp {
  private int mBaseWidth = 640;// 基准值，设计图UI 是640*1136，那么就是以640位基准。
  private int destNum = 1280;// 生成多少份 基准单位值。

  private String dirStr = "./res";
  private final static String WTemplate = "<dimen name=\"wdp{0}\">{1}dip</dimen>\n";

  private String mSupportSw = "320,360,384,400,480,600,720,800";

  public GenerateValueDp(int baseWidth) {
    mBaseWidth = baseWidth;

    File dir = new File(dirStr);
    if (!dir.exists()) {
      dir.mkdir();

    }
    System.out.println(dir.getAbsoluteFile());
  }

  public void generate() {
    String[] vals = mSupportSw.split(",");
    for (String val : vals) {

      generateXmlFile(val);
    }
  }

  public void generateXmlFile(String swValue) {
    String fileName = "values-sw" + swValue + "dp";
    StringBuffer sbForWidth = new StringBuffer();
    sbForWidth.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
    sbForWidth.append("<resources>");

    // 将设备最小宽度 例如320dp 分成基准值 640 份。每份值 320/640 dp.
    float cellw = Integer.parseInt(swValue) * 1.0f / mBaseWidth;

    // 我们希望能有destNum份可供我们使用。
    // 当宽度为基准值640份时，宽度满屏，也就是320dp，超过基准宽度640的值可供高度使用。
    for (int i = 1; i <= destNum; i++) {
      sbForWidth.append(WTemplate.replace("{0}", i + "").replace("{1}",
          change(cellw * i) + ""));
    }
    sbForWidth.append("</resources>");

    File fileDir = new File(dirStr + File.separator + fileName);
    fileDir.mkdir();
    File layFile = new File(fileDir.getAbsolutePath(), "dimen.xml");

    try {
      PrintWriter pw = new PrintWriter(new FileOutputStream(layFile));
      pw.print(sbForWidth.toString());
      pw.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  public static float change(float a) {
    int temp = (int) (a * 100);
    return temp / 100f;
  }

  public static void main(String[] args) {
    new GenerateValueDp(640).generate();
  }
}
