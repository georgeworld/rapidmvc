/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc;

import com.georgeinfo.rapidmvc.render.FreeRenderProcessor;
import com.georgeinfo.rapidmvc.result.Html;
import com.georgeinfo.rapidmvc.result.JavaScript;
import com.georgeinfo.rapidmvc.result.Json;
import com.georgeinfo.rapidmvc.result.Redirect;
import com.georgeinfo.rapidmvc.result.View;
import com.georgeinfo.rapidmvc.result.Xml;

/**
 *
 * @author George <Georgeinfo@163.com>
 */
public class RenderFactoryProxy implements RenderFactory {

    @Override
    public View view() {
        return DefaultRenderFactory.view();
    }

    @Override
    public View view(String viewName) {
        return DefaultRenderFactory.view(viewName);
    }

    @Override
    public Json json() {
        return DefaultRenderFactory.json();
    }

    @Override
    public Json json(Object object) {
        return DefaultRenderFactory.json(object);
    }

    @Override
    public JavaScript javascript() {
        return DefaultRenderFactory.javascript();
    }

    @Override
    public JavaScript javascript(String javascript) {
        return DefaultRenderFactory.javascript(javascript);
    }

    @Override
    public Html html() {
        return DefaultRenderFactory.html();
    }

    @Override
    public Html html(String htmlContent) {
        return DefaultRenderFactory.json(htmlContent);
    }

    @Override
    public Xml xml() {
        return DefaultRenderFactory.xml();
    }

    @Override
    public Xml xml(String xmlContent) {
        return DefaultRenderFactory.xml(xmlContent);
    }

    @Override
    public Redirect redirect() {
        return DefaultRenderFactory.redirect();
    }

    @Override
    public Redirect redirect(String targetUrl) {
        return DefaultRenderFactory.redirect(targetUrl);
    }

    @Override
    public com.georgeinfo.rapidmvc.result.Free Free() {
        return DefaultRenderFactory.Free();
    }

    @Override
    public com.georgeinfo.rapidmvc.result.Free Free(FreeRenderProcessor freeRenderProcessor) {
        return DefaultRenderFactory.Free(freeRenderProcessor);
    }

}
