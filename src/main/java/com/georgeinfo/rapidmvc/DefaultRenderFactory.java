/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc;

import com.georgeinfo.rapidmvc.result.Free;
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
public class DefaultRenderFactory {

    public static View view() {
        return new View();
    }

    public static View view(String viewName) {
        return new View(viewName);
    }

    public static Json json() {
        return new Json();
    }

    public static Json json(Object object) {
        return new Json(object);
    }

    public static JavaScript javascript() {
        return new JavaScript();
    }

    public static JavaScript javascript(String javascript) {
        return new JavaScript(javascript);
    }

    public static Html html() {
        return new Html();
    }

    public static Html json(String htmlContent) {
        return new Html(htmlContent);
    }

    public static Xml xml() {
        return new Xml();
    }

    public static Xml xml(String xmlContent) {
        return new Xml(xmlContent);
    }

    public static Redirect redirect() {
        return new Redirect();
    }

    public static Redirect redirect(String targetUrl) {
        return new Redirect(targetUrl);
    }

    public static Free Free() {
        return new Free();
    }

    public static Free Free(FreeRenderProcessor freeRenderProcessor) {
        return new Free(freeRenderProcessor);
    }
}
