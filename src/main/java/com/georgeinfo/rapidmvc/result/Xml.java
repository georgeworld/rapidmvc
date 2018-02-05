/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc.result;

/**
 * xml渲染器结果
 *
 * @author George <Georgeinfo@163.com>
 */
public class Xml implements Result {

    private String xmlContext;

    public Xml() {
    }

    public Xml(String xmlContext) {
        this.xmlContext = xmlContext;
    }

    public String getXmlContext() {
        return xmlContext;
    }

    public Xml setXmlContext(String xmlContext) {
        this.xmlContext = xmlContext;
        return this;
    }

}
