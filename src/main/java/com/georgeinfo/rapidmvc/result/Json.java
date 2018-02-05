/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc.result;

/**
 * Json渲染器结果
 *
 * @author George <Georgeinfo@163.com>
 */
public class Json implements Result {

    private Object object;

    public Json() {
    }

    public Json(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    public Json setObject(Object object) {
        this.object = object;
        return this;
    }

}
