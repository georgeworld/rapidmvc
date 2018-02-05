/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc.result;

/**
 * html内容渲染器结果
 *
 * @author George <Georgeinfo@163.com>
 */
public class Html implements Result {

    private String htmlContent;

    public Html() {
    }

    public Html(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public Html setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
        return this;
    }

}
