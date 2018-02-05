/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc.render;

import com.georgeinfo.rapidmvc.result.Free;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自由渲染器
 *
 * @author George <Georgeinfo@163.com>
 */
public class FreeRender implements Render {

    private final Free result;

    public FreeRender(Free result) {
        this.result = result;
    }

    @Override
    public boolean doRender(HttpServletRequest reqeust, HttpServletResponse response) {
        return result.getFreeRenderProcessor().doRender(reqeust, response);
    }

}
