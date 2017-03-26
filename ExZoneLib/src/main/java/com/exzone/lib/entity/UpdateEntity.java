package com.exzone.lib.entity;


/**
 * 作者:李鸿浩
 * 描述:版本信息实体类
 * 时间: 2017/3/25.
 */

public class UpdateEntity {
    private int versionCode;
    private String versionName;
    private String fileUrl;
    private String updateInfo;

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getUpdateInfo() {
        return updateInfo;
    }

    public void setUpdateInfo(String updateInfo) {
        this.updateInfo = updateInfo;
    }
}
