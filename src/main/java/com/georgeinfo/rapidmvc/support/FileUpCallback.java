/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc.support;

/**
 * 文件上传后，向浏览器返回的Json对象定义
 *
 * @author George <Georgeinfo@163.com>
 */
public class FileUpCallback {

    private String originalFileName;
    private String targetFileName;
    private String note;

    public FileUpCallback() {
    }

    public FileUpCallback(String originalFileName, String targetFileName) {
        this.originalFileName = originalFileName;
        this.targetFileName = targetFileName;
    }

    public FileUpCallback(String originalFileName, String targetFileName, String note) {
        this.originalFileName = originalFileName;
        this.targetFileName = targetFileName;
        this.note = note;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getTargetFileName() {
        return targetFileName;
    }

    public void setTargetFileName(String targetFileName) {
        this.targetFileName = targetFileName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

}
