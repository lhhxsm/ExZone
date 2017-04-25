package com.exzone.lib.util;

import android.media.MediaRecorder;
import java.io.File;
import java.io.IOException;

/**
 * 作者:李鸿浩
 * 描述:录音工具类
 * 时间:2017/4/3.
 */
public class RecordUtils {
  private static final int SAMPLE_RATE_IN_HZ = 8000;
  private MediaRecorder recorder = new MediaRecorder();
  private String mPath;

  public RecordUtils(String path) {
    this.mPath = path;
  }

  public void start() throws IOException {
    String state = android.os.Environment.getExternalStorageState();
    if (!state.equals(android.os.Environment.MEDIA_MOUNTED)) {
      throw new IOException("SD Card is not mounted,It is  " + state + ".");
    }
    File directory = new File(mPath).getParentFile();
    if (!directory.exists() && !directory.mkdirs()) {
      throw new IOException("Path to file could not be created");
    }
    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);

    recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

    recorder.setAudioSamplingRate(SAMPLE_RATE_IN_HZ);
    recorder.setOutputFile(mPath);
    recorder.prepare();
    recorder.start();
  }

  public void stop() throws IOException {
    if (recorder != null) {
      recorder.stop();
      recorder.release();
    }
  }

  public double getAmplitude() {
    if (recorder != null) {
      return (recorder.getMaxAmplitude());
    }
    return 0;
  }
}
