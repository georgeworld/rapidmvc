/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc.render;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自由渲染执行器接口
 *
 * @author George <Georgeinfo@163.com>
 */
public interface FreeRenderProcessor {

    public boolean doRender(HttpServletRequest reqeust, HttpServletResponse response);
}
