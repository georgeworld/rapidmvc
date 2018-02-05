/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc.render;

import com.georgeinfo.base.util.logger.GeorgeLogger;
import com.georgeinfo.rapidmvc.result.JavaScript;
import gbt.config.GeorgeLoggerFactory;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JavaScript视图渲染器
 *
 * @author George <Georgeinfo@163.com>
 */
public class JavaScriptRender implements Render {

    private static final GeorgeLogger logger = GeorgeLoggerFactory.getLogger(JavaScriptRender.class);
    private JavaScript javascript;

    public JavaScriptRender() {
    }

    public JavaScriptRender(JavaScript json) {
        this.javascript = json;
    }

    public JavaScript getJavascript() {
        return javascript;
    }

    public void setJavascript(JavaScript javascript) {
        this.javascript = javascript;
    }

    @Override
    public boolean doRender(HttpServletRequest reqeust, HttpServletResponse response) {
        try {
            String contentType = "text/html";
            response.setContentType(contentType);

            String content = javascript.getJavascript();
            if (content != null && !content.trim().isEmpty() && !content.contains("<script") && !content.contains("</script>")) {
                content = "<script type=\"text/javascript\">" + content + "</script>";
            }
            PrintWriter writer = response.getWriter();
            writer.flush();
            writer.write(content);
            writer.flush();
            writer.close();

            return true;
        } catch (IOException ex) {
            logger.error("## IOException when print json string.", ex);
            return false;
        }
    }

}
