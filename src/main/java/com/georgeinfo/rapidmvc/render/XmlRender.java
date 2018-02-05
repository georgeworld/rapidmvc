/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc.render;

import com.georgeinfo.base.util.logger.GeorgeLogger;
import com.georgeinfo.rapidmvc.result.Xml;
import gbt.config.GeorgeLoggerFactory;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * xml内容渲染器
 *
 * @author George <Georgeinfo@163.com>
 */
public class XmlRender implements Render {

    private static final GeorgeLogger logger = GeorgeLoggerFactory.getLogger(XmlRender.class);
    private Xml xml;

    public XmlRender() {
    }

    public XmlRender(Xml xml) {
        this.xml = xml;
    }

    public Xml getXml() {
        return xml;
    }

    public void setXml(Xml xml) {
        this.xml = xml;
    }

    @Override
    public boolean doRender(HttpServletRequest reqeust, HttpServletResponse response) {
        try {
            String contentType = "text/xml";
            response.setContentType(contentType);
            
            PrintWriter writer = response.getWriter();
            writer.flush();
            writer.write(xml.getXmlContext());
            writer.flush();
            writer.close();
            
            return true;
        } catch (IOException ex) {
            logger.error("## IOException when print html content.", ex);
            return false;
        }
    }

}
