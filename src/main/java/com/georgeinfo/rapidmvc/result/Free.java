/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc.result;

import com.georgeinfo.rapidmvc.render.FreeRenderProcessor;

/**
 * 视图渲染器结果对象
 *
 * @author George <Georgeinfo@163.com>
 */
public class Free implements Result {

    private FreeRenderProcessor freeRenderProcessor;

    public Free() {
    }

    public Free(FreeRenderProcessor freeRenderProcessor) {
        this.freeRenderProcessor = freeRenderProcessor;
    }

    public FreeRenderProcessor getFreeRenderProcessor() {
        return freeRenderProcessor;
    }

    public Free setFreeRenderProcessor(FreeRenderProcessor freeRenderProcessor) {
        this.freeRenderProcessor = freeRenderProcessor;
        return this;
    }

}
