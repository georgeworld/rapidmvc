/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc.result;

/**
 * 跳转渲染器结果
 *
 * @author George <Georgeinfo@163.com>
 */
public class Redirect implements Result {

    private String targetUrl;

    public Redirect() {
    }

    public Redirect(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public Redirect setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
        return this;
    }

}
