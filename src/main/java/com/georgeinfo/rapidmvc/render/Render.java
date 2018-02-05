/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc.render;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * web请求处理结果渲染器
 *
 * @author George <Georgeinfo@163.com>
 */
public interface Render {

    /**
     * 执行渲染
     */
    public boolean doRender(HttpServletRequest reqeust, HttpServletResponse response);
}
