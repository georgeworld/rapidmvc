/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc.render;

import com.georgeinfo.base.util.logger.GeorgeLogger;
import com.georgeinfo.rapidmvc.result.Html;
import gbt.config.GeorgeLoggerFactory;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * html内容渲染器
 *
 * @author George <Georgeinfo@163.com>
 */
public class HtmlRender implements Render {

    private static final GeorgeLogger logger = GeorgeLoggerFactory.getLogger(HtmlRender.class);
    private Html html;

    public HtmlRender() {
    }

    public HtmlRender(Html html) {
        this.html = html;
    }

    public Html getHtml() {
        return html;
    }

    public void setHtml(Html html) {
        this.html = html;
    }

    @Override
    public boolean doRender(HttpServletRequest reqeust, HttpServletResponse response) {
        try {
            String contentType = "text/html";
            response.setContentType(contentType);

            PrintWriter writer = response.getWriter();
            writer.flush();
            writer.write(html.getHtmlContent());
            writer.flush();
            writer.close();

            return true;
        } catch (IOException ex) {
            logger.error("## IOException when print html string.", ex);
            return false;
        }
    }

}
