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
public interface RenderFactory {

    public View view();

    public View view(String viewName);

    public Json json();

    public Json json(Object object);

    public JavaScript javascript();

    public JavaScript javascript(String javascript);

    public Html html();

    public Html html(String htmlContent);

    public Xml xml();

    public Xml xml(String xmlContent);

    public Redirect redirect();

    public Redirect redirect(String targetUrl);

    public Free Free();

    public Free Free(FreeRenderProcessor freeRenderProcessor);
}
