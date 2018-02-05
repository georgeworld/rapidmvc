/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc.result;

/**
 * JavaScript渲染器结果
 *
 * @author George <Georgeinfo@163.com>
 */
public class JavaScript implements Result {

    private String javascript;

    public JavaScript() {
    }

    public JavaScript(String javascript) {
        this.javascript = javascript;
    }

    public String getJavascript() {
        return javascript;
    }

    public void setJavascript(String javascript) {
        this.javascript = javascript;
    }
}
