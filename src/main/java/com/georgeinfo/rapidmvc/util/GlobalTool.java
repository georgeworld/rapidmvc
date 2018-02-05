/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc.util;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 全局工具类
 *
 * @author George <Georgeinfo@163.com>
 */
public class GlobalTool {

    private final ObjectMapper jsonMapper;

    private GlobalTool() {
        jsonMapper = new ObjectMapper();
    }
    // 线程安全的惰性加载单例模式 开始

    private static class GlobalToolHolder {

        private static final GlobalTool instance = new GlobalTool();
    }

    public static GlobalTool getInstance() {
        return GlobalToolHolder.instance;
    }
    // 线程安全的惰性加载单例模式 结束

    public ObjectMapper getJsonMapper() {
        return jsonMapper;
    }

}
