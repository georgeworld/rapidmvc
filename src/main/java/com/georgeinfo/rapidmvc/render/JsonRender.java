/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc.render;

import com.georgeinfo.base.util.logger.GeorgeLogger;
import com.georgeinfo.rapidmvc.result.Json;
import com.georgeinfo.rapidmvc.util.GlobalTool;
import gbt.config.GeorgeLoggerFactory;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 视图渲染器
 *
 * @author George <Georgeinfo@163.com>
 */
public class JsonRender implements Render {

    private static final GeorgeLogger logger = GeorgeLoggerFactory.getLogger(JsonRender.class);
    private Json json;

    public JsonRender() {
    }

    public JsonRender(Json json) {
        this.json = json;
    }

    public Json getJson() {
        return json;
    }

    public void setJson(Json json) {
        this.json = json;
    }

    @Override
    public boolean doRender(HttpServletRequest reqeust, HttpServletResponse response) {
        try {
            String contentType = "application/json";
            response.setContentType(contentType);

            String jsonString = GlobalTool.getInstance().getJsonMapper().writeValueAsString(json.getObject());
            PrintWriter writer = response.getWriter();
            writer.flush();
            writer.write(jsonString);
            writer.flush();
            writer.close();

            return true;
        } catch (IOException ex) {
            logger.error("## IOException when print json string.", ex);
            return false;
        }
    }

}
